package models;

import dao.BibliotecarioDAO;
import java.sql.SQLException;
import java.util.List;

public class Bibliotecario extends Usuario {

    private Cargo cargo;

    public Bibliotecario(String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        super(nome, cpf, login, senha, ativo);
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void verBibliotecario() {
        verUsuario();
        System.out.println("Cargo: " + cargo);
    }

    public static Bibliotecario criarBibliotecario(String nome, String cpf, String login,
            String senha, Cargo cargo) {
        try {
            return new BibliotecarioDAO().criarBibliotecario(nome, cpf, login, senha, cargo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf,
            String login, String senha, boolean ativo, Cargo cargo) {
        try {
            new BibliotecarioDAO().editarBibliotecario(bibliotecario, nome, cpf, login, senha, ativo, cargo);
            bibliotecario.setNome(nome);
            bibliotecario.setCpf(cpf);
            bibliotecario.setLogin(login);
            bibliotecario.setSenha(senha);
            bibliotecario.setAtivo(ativo);
            bibliotecario.setCargo(cargo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Bibliotecario> listarBibliotecarios() {
        try {
            return new BibliotecarioDAO().listarBibliotecarios();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
