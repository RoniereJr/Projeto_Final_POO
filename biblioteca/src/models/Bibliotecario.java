package models;

public class Bibliotecario extends Usuario {
    private Cargo cargo;

    public Bibliotecario(String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        super(nome, cpf, login, senha, ativo);
        this.cargo = cargo;
    }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public static boolean criarBibliotecario(String nome, String cpf, String login, String senha, Cargo cargo) {
        try {
            new dao.BibliotecarioDAO().criarBibliotecario(nome, cpf, login, senha, cargo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}