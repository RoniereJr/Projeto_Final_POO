package ProjetoDePoo;

public class Bibliotecario extends Usuario{
    // ATRIBUTOS
    private Cargo cargo;

    // CONSTRUTOR
    public Bibliotecario(String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        super(nome, cpf, login, senha, ativo);
        this.cargo = cargo;
    }

    // METODOS
    @Override
    public void verUsuario() {
        super.verUsuario();
    }

    public void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf, String login, String senha, boolean ativo, Cargo cargo) {
        //super(nome, cpf, login, senha, ativo);
        this.cargo = cargo;
    }

//    public List<Bibliotecario> listarBibliotecarios(){
//        // listar
//    }
}
