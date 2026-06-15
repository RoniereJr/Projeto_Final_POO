package models;

public class Membro extends Usuario {
    private String endereco;
    private String telefone;
    private String email;

    public Membro(String nome, String cpf, String login, String senha, boolean ativo,
                  String endereco, String telefone, String email) {
        super(nome, cpf, login, senha, ativo);
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail()    { return email;    }

    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email)       { this.email = email;       }

    public static boolean criarMembro(String nome, String cpf, String login, String senha,
                                      String endereco, String telefone, String email) {
        try {
            new dao.MembroDAO().criarMembro(nome, cpf, login, senha, endereco, telefone, email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}