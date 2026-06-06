package models;

public class Bibliotecario extends Usuario{
    // ATRIBUTOS
    private Cargo cargo;

    // CONSTRUTOR
    public Bibliotecario(String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        super(nome, cpf, login, senha, ativo);
        this.cargo = cargo;
    }

    // METODOS
    public static Bibliotecario criaBibliotecario(String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo){
        return new Bibliotecario(nome, cpf, login, senha, ativo, cargo);
    }

    @Override
    public void verUsuario() {
        super.verUsuario();
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        bibliotecario.setNome(nome);
        bibliotecario.setCpf(cpf);
        bibliotecario.setLogin(login);
        bibliotecario.setSenha(senha);
        bibliotecario.setAtivo(ativo);
        bibliotecario.setCargo(cargo);
    }
}
