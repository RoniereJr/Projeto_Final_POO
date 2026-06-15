package models;

public class Usuario {

    public static Usuario autenticar(String login, String senha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
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

    public String getNome()  { return nome;  }
    public String getCpf()   { return cpf;   }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public boolean isAtivo() { return ativo; }

    public void setNome(String nome)   { this.nome = nome;   }
    public void setCpf(String cpf)     { this.cpf = cpf;     }
    public void setLogin(String login) { this.login = login; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setAtivo(boolean ativo){ this.ativo = ativo; }

    public void verUsuario() {
        System.out.println("Nome: " + nome + " | CPF: " + cpf);
    }
}