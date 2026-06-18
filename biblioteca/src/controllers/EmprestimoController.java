package controllers;

import java.time.LocalDate;
import java.util.List;

import dao.EmprestimoDAO;
import models.Bibliotecario;
import models.Emprestimo;
import models.Livro;
import models.Membro;
import models.Usuario;

/**
 * Controller responsável por gerenciar as regras de negócio de empréstimos e devoluções.
 */
public class EmprestimoController {

    // Instância do DAO para persistência direta de dados conforme arquitetura adotada pelo grupo
    private java.io.Serializable dao = new EmprestimoDAO();

    /**
     * Valida os pré-requisitos e regras de negócio e registra um novo empréstimo no sistema.
     * * @param usuarioLogado Usuário que opera o sistema (deve ser um Bibliotecário).
     * @param livro Instância do livro a ser emprestado.
     * @param membro Instância do membro que solicita o empréstimo.
     * @return true se o empréstimo foi efetuado com sucesso, false em caso de impedimento ou erro.
     */
    public boolean realizarEmprestimo(Usuario usuarioLogado, Livro livro, Membro membro) {
        // Validação de acesso: apenas bibliotecários possuem autorização para realizar empréstimos
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem realizar empréstimos.");
            return false;
        }
        // Verifica a integridade dos parâmetros fornecidos pela View
        if (livro == null || membro == null) {
            System.out.println("Erro: Livro ou membro inválido.");
            return false;
        }
        // Regra de negócio: verifica se há cópias físicas disponíveis em estoque para empréstimo
        if (livro.getDisponiveis() <= 0) {
            System.out.println("Erro: Livro sem cópias disponíveis.");
            return false;
        }

        // Regra de negócio: verifica se o membro consta na lista de usuários suspensos por atraso anterior
        List<Membro> suspensos = Membro.listarMembrosSuspensos();
        boolean estaSuspenso = suspensos.stream().anyMatch(m -> m.getCpf().equals(membro.getCpf()));
        if (estaSuspenso) {
            System.out.println("Erro: Membro está suspenso.");
            return false;
        }
        
        // Regra de negócio: impede o membro de pegar o mesmo livro se já tiver um empréstimo ativo dele
        List<Emprestimo> ativos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
        boolean jaTem = ativos.stream()
                .anyMatch(e -> e.getLivro().getIsbn().equals(livro.getIsbn()));
        if (jaTem) {
            System.out.println("Erro: Membro já possui este livro emprestado (não devolvido).");
            return false;
        }

        try {
            // Delegação da criação do registro para o método estático da Model Emprestimo
            Emprestimo emp = Emprestimo.criarEmprestimo(livro, membro);
            if (emp != null) {
                System.out.println(">> Empréstimo realizado. Devolução prevista: " + emp.getDataDevolucaoPrevista());
                return true;
            }
        } catch (Exception e) {
            // Tratamento específico caso haja tentativa de inserção duplicada na mesma data pelo banco
            if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                System.out.println("Erro: Este membro já possui um empréstimo ativo para este livro na mesma data.");
                return false;
            }
            e.printStackTrace();
        }

        System.out.println("Erro: Falha ao criar empréstimo.");
        return false;
    }

    /**
     * Localiza um empréstimo ativo, processa a devolução e calcula eventuais dias de atraso.
     */
    public boolean registrarDevolucao(Usuario usuarioLogado, Livro livro, Membro membro,
            LocalDate dataEmprestimo) {
        // Validação de acesso: restrito a funcionários da biblioteca
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem registrar devoluções.");
            return false;
        }

        // Filtra os empréstimos em aberto do membro para localizar o registro correto com base no ISBN e data
        List<Emprestimo> ativos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
        Emprestimo alvo = ativos.stream()
                .filter(e -> e.getLivro().getIsbn().equals(livro.getIsbn()) &&
                        e.getDataEmprestimo().equals(dataEmprestimo))
                .findFirst()
                .orElse(null);

        if (alvo == null) {
            System.out.println("Erro: Empréstimo não encontrado ou já devolvido.");
            return false;
        }

        // Executa a lógica de negócio interna do objeto em memória para setar a data de entrega
        alvo.registrarDevolucao();

        try {
            // Sincroniza e encerra o registro diretamente no banco de dados via DAO e retorna os dias de atraso
            int diasAtraso = ((EmprestimoDAO) dao).registrarDevolucao(
                    alvo.getLivro().getIsbn(),
                    alvo.getMembro().getCpf(),
                    alvo.getDataEmprestimo());
            if (diasAtraso > 0) {
                System.out.println(">> Devolução com " + diasAtraso + " dias de atraso.");
            } else {
                System.out.println(">> Devolução registrada no prazo.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retorna a listagem de todos os empréstimos ocorridos no sistema.
     */
    public List<Emprestimo> listarEmprestimos() {
        return Emprestimo.listarEmprestimos();
    }

    /**
     * Retorna a listagem de empréstimos que estão atualmente em aberto (não devolvidos).
     */
    public List<Emprestimo> listarEmprestimosAtivos() {
        return Emprestimo.listarEmprestimosAtivos();
    }

    /**
     * Retorna a listagem de empréstimos em aberto cuja data prevista de devolução expirou.
     */
    public List<Emprestimo> listarEmprestimosAtrasados() {
        return Emprestimo.listarEmprestimosAtrasados();
    }

    /**
     * Filtra os empréstimos ativos vinculados a um membro específico.
     */
    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        return Emprestimo.listarEmprestimosAtivosPorMembro(membro);
    }

    /**
     * Filtra os empréstimos em atraso vinculados a um membro específico.
     */
    public List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        return Emprestimo.listarEmprestimosAtrasadosPorMembro(membro);
    }
}