package ProjetoDePoo;

public abstract class Membro extends Usuario{
    // ATRIBUTOS
    private String endereco;
    private String telefone;
    private String email;

    // CONSTRUTOR
    public Membro(String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email) {
        super(nome, cpf, login, senha, ativo);
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    // METODOS
//    public void editarMembro(Membro membro, String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email) {
//        super(nome, cpf, login, senha, ativo);
//        this.endereco = endereco;
//        this.telefone = telefone;
//        this.email = email;
//    }

//    public List<Membro> listarMembros(){
//        //listar
//    }

//    public List<Membro> listarMembrosSuspensos(){
//        //listar
//    }
}
