package models;

import dao.MembroDAO;
import java.sql.SQLException;
import java.util.List;

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
    
    public static Membro criaMembro(String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email){
        return new Membro(nome, cpf, login, senha, ativo, endereco, telefone, email);
    }
    
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void verMembro() {
        verUsuario();
        System.out.println("Endereco: " + endereco + " | Telefone: " + telefone + " | Email: " + email);
    }

    public static Membro criarMembro(String nome, String cpf, String login, String senha,
            String endereco, String telefone, String email) {
        try {
            return new MembroDAO().criarMembro(nome, cpf, login, senha, endereco, telefone, email);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void editarMembro(Membro membro, String nome, String cpf, String login,
            String senha, boolean ativo, String endereco, String telefone, String email) {
        try {
            new MembroDAO().editarMembro(membro, nome, cpf, login, senha, ativo, endereco, telefone, email);
            membro.setNome(nome);
            membro.setCpf(cpf);
            membro.setLogin(login);
            membro.setSenha(senha);
            membro.setAtivo(ativo);
            membro.setEndereco(endereco);
            membro.setTelefone(telefone);
            membro.setEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Membro> listarMembros() {
        try {
            return new MembroDAO().listarMembros();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Membro> listarMembrosSuspensos() {
        try {
            return new MembroDAO().listarMembrosSuspensos();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
