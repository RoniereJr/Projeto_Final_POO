package controllers;

import models.Membro;
import models.Usuario;
import dao.MembroDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller responsável pelas regras de negócio e operações ligadas aos Membros (Leitores).
 */
public class MembroController {

    // Instância direta do DAO para comunicação com o banco de dados conforme definido pelo grupo
    private MembroDAO dao = new MembroDAO();

    /**
     * Valida as permissões do usuário logado e solicita o cadastro de um novo membro na Model.
     * * @param usuarioLogado Usuário que está operando o sistema (deve ser um Bibliotecário).
     * @param nome Nome do novo membro.
     * @param cpf CPF do novo membro.
     * @param login Login de acesso do novo membro.
     * @param senha Senha de acesso do novo membro.
     * @param endereco Endereço residencial do membro.
     * @param telefone Telefone de contato do membro.
     * @param email E-mail de contato do membro.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarMembro(Usuario usuarioLogado, String nome, String cpf, String login,
            String senha, String endereco, String telefone, String email) {
        // Restrição de acesso: apenas usuários do tipo Bibliotecário podem gerenciar cadastros de membros
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar membros.");
            return false;
        }
        // Validação preventiva contra campos obrigatórios nulos
        if (nome == null || cpf == null || login == null || senha == null) {
            System.out.println("Erro: Dados obrigatórios ausentes.");
            return false;
        }
        
        // Delegação da persistência para o método estático da Model correspondente
        Membro novo = Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);
        if (novo != null) {
            System.out.println(">> Membro cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (CPF ou login duplicado?).");
        return false;
    }

    /**
     * Valida as permissões e repassa os dados atualizados para alteração do registro através da Model.
     */
    public boolean editarMembro(Usuario usuarioLogado, Membro membro, String nome,
            String login, String senha, boolean ativo,
            String endereco, String telefone, String email) {
        // Restrição de acesso: operação permitida apenas para Bibliotecários
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar membros.");
            return false;
        }
        
        // Chamada do método estático da Model repassando o objeto para persistência e sincronização de dados
        Membro.editarMembro(membro, nome, membro.getCpf(), login, senha, ativo,
                endereco, telefone, email);
        System.out.println(">> Membro editado.");
        return true;
    }

    /**
     * Altera o status de atividade de um membro para falso (desativado).
     */
    public boolean desativarMembro(Usuario usuarioLogado, Membro membro) {
        // Restrição de acesso: apenas bibliotecários possuem permissão para desativar leitores
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem desativar membros.");
            return false;
        }
        
        // Executa o método estático da Model mãe (Usuario) para desativação lógica
        Usuario.desativarUsuario(membro);
        System.out.println(">> Membro desativado.");
        return true;
    }

    /**
     * Retorna a listagem de todos os membros registrados no sistema.
     */
    public List<Membro> listarMembros() {
        return Membro.listarMembros();
    }

    /**
     * Retorna a listagem de membros que estão suspensos devido a atrasos na devolução.
     */
    public List<Membro> listarMembrosSuspensos() {
        return Membro.listarMembrosSuspensos();
    }

    /**
     * Realiza a consulta direta no banco de dados via DAO para localizar um membro pelo CPF.
     * * @param cpf Identificador único do leitor.
     * @return O objeto Membro se encontrado, ou null em caso de erro ou inexistência.
     */
    public Membro buscarPorCpf(String cpf) {
        try {
            // Execução da busca através da instância local do DAO cadastrada
            return dao.buscarPorCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}