package controllers;

import java.time.LocalDate;
import java.util.List;

import dao.EmprestimoDAO;
import models.Bibliotecario;
import models.Emprestimo;
import models.Livro;
import models.Membro;
import models.Usuario;

public class EmprestimoController {

    private EmprestimoDAO dao = new EmprestimoDAO();

    public boolean realizarEmprestimo(Usuario usuarioLogado, Livro livro, Membro membro) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem realizar empréstimos.");
            return false;
        }
        if (livro == null || membro == null) {
            System.out.println("Erro: Livro ou membro inválido.");
            return false;
        }
        if (livro.getDisponiveis() <= 0) {
            System.out.println("Erro: Livro sem cópias disponíveis.");
            return false;
        }

        List<Membro> suspensos = Membro.listarMembrosSuspensos();
        boolean estaSuspenso = suspensos.stream().anyMatch(m -> m.getCpf().equals(membro.getCpf()));
        if (estaSuspenso) {
            System.out.println("Erro: Membro está suspenso.");
            return false;
        }
        List<Emprestimo> ativos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
        boolean jaTem = ativos.stream()
                .anyMatch(e -> e.getLivro().getIsbn().equals(livro.getIsbn()));
        if (jaTem) {
            System.out.println("Erro: Membro já possui este livro emprestado (não devolvido).");
            return false;
        }

        try {
            Emprestimo emp = Emprestimo.criarEmprestimo(livro, membro);
            if (emp != null) {
                System.out.println(">> Empréstimo realizado. Devolução prevista: " + emp.getDataDevolucaoPrevista());
                return true;
            }
        } catch (Exception e) {
            if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                System.out.println("Erro: Este membro já possui um empréstimo ativo para este livro na mesma data.");
                return false;
            }
            e.printStackTrace();
        }

        System.out.println("Erro: Falha ao criar empréstimo.");
        return false;
    }

    public boolean registrarDevolucao(Usuario usuarioLogado, Livro livro, Membro membro,
            LocalDate dataEmprestimo) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem registrar devoluções.");
            return false;
        }

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

        alvo.registrarDevolucao();

        try {
            int diasAtraso = dao.registrarDevolucao(
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

    public boolean editarEmprestimo(Usuario usuarioLogado, Emprestimo emprestimo,
            LocalDate dataEmprestimo, LocalDate dataPrevista,
            LocalDate dataReal, boolean devolvido) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar empréstimos.");
            return false;
        }
        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo inválido.");
            return false;
        }
        Emprestimo.editarEmprestimo(emprestimo, dataEmprestimo, dataPrevista, dataReal, devolvido);
        System.out.println(">> Empréstimo editado.");
        return true;
    }

    public boolean excluirEmprestimo(Usuario usuarioLogado, Emprestimo emprestimo) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem excluir empréstimos.");
            return false;
        }
        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo inválido.");
            return false;
        }
        Emprestimo.excluirEmprestimo(emprestimo);
        System.out.println(">> Empréstimo excluído.");
        return true;
    }

    public List<Emprestimo> listarEmprestimos() {
        return Emprestimo.listarEmprestimos();
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return Emprestimo.listarEmprestimosAtivos();
    }

    public List<Emprestimo> listarEmprestimosAtrasados() {
        return Emprestimo.listarEmprestimosAtrasados();
    }

    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        return Emprestimo.listarEmprestimosAtivosPorMembro(membro);
    }

    public List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        return Emprestimo.listarEmprestimosAtrasadosPorMembro(membro);
    }
}