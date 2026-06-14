package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Membro;
import models.Cargo;
import dao.UsuarioDAO;
import dao.BibliotecarioDAO;
import dao.MembroDAO;
import java.sql.SQLException;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final BibliotecarioDAO bibliotecarioDAO = new BibliotecarioDAO();
    private final MembroDAO membroDAO = new MembroDAO();

    /**
     cadastra um novo bibliotecário no sistema.
     apenas bibliotecários com cargo de SUPERVISOR podem gerenciar outros bibliotecários.
     */
    public boolean cadastrarBibliotecario(Usuario usuarioLogado, String nome, String cpf, String login, String senha, Cargo cargo) {
        // validação de Regra de segurança
        if (!(usuarioLogado instanceof Bibliotecario) || ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Acesso negado. Apenas bibliotecários SUPERVISORES podem cadastrar bibliotecários.");
            return false;
        }

        // 2. Validação de dados consistentes
        if (nome == null || cpf == null || login == null || senha == null || cargo == null) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return false;
        }

        try {
            // 3. Executa a inserção através do DAO
            bibliotecarioDAO.criarBibliotecario(nome, cpf, login, senha, cargo);
            System.out.println(">> Bibliotecário '" + nome + "' cadastrado com sucesso no Banco de Dados!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar bibliotecário: " + e.getMessage());
            return false;
        }
    }

    /**
     * cadastra um novo membro cliente no sistema.
      apenas bibliotecários (qualquer cargo) podem gerenciar o cadastro de membros.
     */
    public boolean cadastrarMembro(Usuario usuarioLogado, String nome, String cpf, String login, String senha, String endereco, String telefone, String email) {
        // validação de Segurança
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Acesso negado. Apenas bibliotecários podem cadastrar membros.");
            return false;
        }

        // validação de campos obrigatórios
        if (nome == null || cpf == null || login == null || senha == null || endereco == null || telefone == null || email == null) {
            System.out.println("Erro: Dados incompletos. Todos os campos do membro são obrigatórios.");
            return false;
        }

        try {
            // inserção através do DAO
            membroDAO.criarMembro(nome, cpf, login, senha, endereco, telefone, email);
            System.out.println(">> Membro '" + nome + "' cadastrado com sucesso no Banco de Dados!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar membro: " + e.getMessage());
            return false;
        }
    }

    /**
     desativa logicamente a conta de um usuário no sistema.
     */
    public boolean desativarUsuario(Usuario usuarioLogado, Usuario usuarioAlvo) {
        // apenas bibliotecários podem desativar contas
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada. Apenas bibliotecários podem desativar contas.");
            return false;
        }

        if (usuarioAlvo == null) {
            System.out.println("Erro: Usuário alvo inválido.");
            return false;
        }

        try {
            // desativa a logica via UPDATE no Banco de Dados
            usuarioDAO.desativarUsuario(usuarioAlvo);
            System.out.println(">> A conta do usuário '" + usuarioAlvo.getNome() + "' foi desativada.");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao desativar conta: " + e.getMessage());
            return false;
        }
    }
}