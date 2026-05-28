package ProjetoDePoo;

//+ criarUsuario(nome:String, cpf:String, login:String, senha:String) : Usuario
//+ editarUsuario(usuario:Usuario, nome:String, cpf:String, login:String, senha:String, ativo:boolean) : void
//+ desativarUsuario(usuario:Usuario) : void
//+ autenticar(login:String, senha:String) : boolean
//+ listarUsuarios() : List<Usuario>

public class Usuario {
    // ATRIBUTOS
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    // CONSTRUTOR
    public Usuario(String nome, String cpf, String login, String senha, boolean ativo) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    // METODOS
    public void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    public void desativarUsuario(Usuario usuario){
        // deletar
    }

    public boolean autenticar(String login, String senha){
        if( (this.login.equals(login)) && (this.senha.equals(senha)) ){
            return true;
        } else{
            return false;
        }
    }

//    public List<Usuario> listarUsuarios(){
//        // listar usuarios
//    }

    public void verUsuario() {

    }
}