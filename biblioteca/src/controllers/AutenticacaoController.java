package controllers;

import java.sql.SQLException;

import dao.BibliotecarioDAO;
import dao.MembroDAO;
import dao.UsuarioDAO;
import models.Usuario;

public class AutenticacaoController {

    private Usuario usuarioLogado;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final BibliotecarioDAO bibliotecarioDAO = new BibliotecarioDAO();
    private final MembroDAO membroDAO = new MembroDAO();

    /**
     autentica o utilizador no sistema utilizando o banco de dados.
     contas desativadas são bloqueadas diretamente na query do DAO.
     */
    public Usuario login(String login, String senha) {
        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Erro: Login e senha não podem estar vazios.");
            return null;
        }

        try {
            // verifica se a combinação de login/senha existe e está ativa
            boolean autenticado = usuarioDAO.autenticar(login, senha);
            
            if (!autenticado) {
                System.out.println("Erro: Login, senha incorretos ou conta desativada.");
                return null;
            }

            // localiza o objeto completo para mapear a sessão
            for (Usuario u : usuarioDAO.listarUsuarios()) {
                if (u.getLogin().equals(login)) {
                    this.usuarioLogado = u;
                    System.out.println(">> Login efetuado com sucesso. Bem-vindo, " + u.getNome() + "!");
                    return u;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro de banco de dados ao autenticar: " + e.getMessage());
        }

        return null;
    }

    /**
     encerra a sessão atual.
     */
    public void logout() {
        if (this.usuarioLogado != null) {
            System.out.println(">> Sessão encerrada para o utilizador: " + this.usuarioLogado.getLogin());
            this.usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}