package controllers;

import models.Bibliotecario;
import models.Cargo;
import models.Usuario;
import dao.BibliotecarioDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller responsável pelas regras de negócio e operações ligadas aos Bibliotecários.
 */
public class BibliotecarioController {

    // Instância direta do DAO para comunicação com o banco de dados conforme definido pelo grupo
    private BibliotecarioDAO dao = new BibliotecarioDAO();

    /**
     * Valida as permissões do usuário logado e solicita a criação de um novo bibliotecário na Model.
     * * @param usuarioLogado Usuário que está tentando realizar a ação (deve ser SUPERVISOR).
     * @param nome Nome do novo bibliotecário.
     * @param cpf CPF do novo bibliotecário.
     * @param login Login de acesso do novo bibliotecário.
     * @param senha Senha de acesso do novo bibliotecário.
     * @param cargo Cargo atribuído ao novo bibliotecário.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarBibliotecario(Usuario usuarioLogado, String nome, String cpf,
            String login, String senha, Cargo cargo) {
        // Restrição de acesso: apenas bibliotecários com cargo de SUPERVISOR podem cadastrar outros
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem cadastrar bibliotecários.");
            return false;
        }
        // Validação básica para impedir campos nulos obrigatorios
        if (nome == null || cpf == null || login == null || senha == null || cargo == null) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return false;
        }
        
        // Delegação da criação para o método estático da Model correspondente
        Bibliotecario novo = Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);
        if (novo != null) {
            System.out.println(">> Bibliotecário cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (CPF ou login duplicado?).");
        return false;
    }

    /**
     * Valida as permissões do usuário logado e edita as informações de um bibliotecário existente através da Model.
     */
    public boolean editarBibliotecario(Usuario usuarioLogado, Bibliotecario bibliotecario,
            String nome, String login, String senha,
            boolean ativo, Cargo cargo) {
        // Restrição de acesso: apenas SUPERVISORES possuem permissão para alteração cadastral
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem editar bibliotecários.");
            return false;
        }
        
        // Chamada do método estático da Model repassando o objeto para persistência e sincronização de dados
        Bibliotecario.editarBibliotecario(bibliotecario, nome, bibliotecario.getCpf(),
                login, senha, ativo, cargo);
        System.out.println(">> Bibliotecário editado.");
        return true;
    }

    /**
     * Altera o status de atividade de um bibliotecário para falso (desativado).
     */
    public boolean desativarBibliotecario(Usuario usuarioLogado, Bibliotecario bibliotecario) {
        // Restrição de acesso: apenas SUPERVISORES podem desativar contas de funcionários
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem desativar bibliotecários.");
            return false;
        }
        
        // Executa o método estático da Model mãe (Usuario) para desativação lógica
        Usuario.desativarUsuario(bibliotecario);
        System.out.println(">> Bibliotecário desativado.");
        return true;
    }

    /**
     * Retorna a listagem completa de todos os bibliotecários cadastrados no sistema.
     */
    public List<Bibliotecario> listarBibliotecarios() {
        return Bibliotecario.listarBibliotecarios();
    }

    /**
     * Realiza a consulta direta no banco de dados via DAO para localizar um bibliotecário pelo CPF.
     * * @param cpf Identificador único do funcionário.
     * @return O objeto Bibliotecario se encontrado, ou null em caso de erro ou inexistência.
     */
    public Bibliotecario buscarPorCpf(String cpf) {
        try {
            // Execução da busca através da instância local do DAO cadastrada
            return dao.buscarPorCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}