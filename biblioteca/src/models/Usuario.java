package models;

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

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo) {
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setAtivo(ativo);
    }

    public void desativarUsuario(Usuario usuario){
    }

    public boolean autenticar(String login, String senha){
        if( (this.login.equals(login)) && (this.senha.equals(senha)) ){
            return true;
        } else{
            return false;
        }
    }

    public void verUsuario() {
    }
}