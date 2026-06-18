package models;

import dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public abstract class Usuario {

    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    public Usuario(String nome, String cpf, String login, String senha, boolean ativo) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void verUsuario() {
        System.out.println("Nome: " + nome + " | CPF: " + cpf +
                " | Login: " + login + " | Ativo: " + ativo);
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf,
            String login, String senha, boolean ativo) {
        try {
            new UsuarioDAO().editarUsuario(usuario, nome, cpf, login, senha, ativo);
            usuario.setNome(nome);
            usuario.setCpf(cpf);
            usuario.setLogin(login);
            usuario.setSenha(senha);
            usuario.setAtivo(ativo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void desativarUsuario(Usuario usuario) {
        try {
            new UsuarioDAO().desativarUsuario(usuario);
            usuario.setAtivo(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean autenticar(String login, String senha) {
        try {
            return new UsuarioDAO().autenticar(login, senha);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Usuario> listarUsuarios() {
        try {
            return new UsuarioDAO().listarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}