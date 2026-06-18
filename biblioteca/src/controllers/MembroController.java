package controllers;

import models.Membro;
import models.Usuario;
import dao.MembroDAO;
import java.sql.SQLException;
import java.util.List;

public class MembroController {

    private MembroDAO dao = new MembroDAO();

    public boolean cadastrarMembro(Usuario usuarioLogado, String nome, String cpf, String login,
            String senha, String endereco, String telefone, String email) {
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar membros.");
            return false;
        }
        if (nome == null || cpf == null || login == null || senha == null) {
            System.out.println("Erro: Dados obrigatórios ausentes.");
            return false;
        }
        Membro novo = Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);
        if (novo != null) {
            System.out.println(">> Membro cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (CPF ou login duplicado?).");
        return false;
    }

    public boolean editarMembro(Usuario usuarioLogado, Membro membro, String nome,
            String login, String senha, boolean ativo,
            String endereco, String telefone, String email) {
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar membros.");
            return false;
        }

        Membro.editarMembro(membro, nome, membro.getCpf(), login, senha, ativo,
                endereco, telefone, email);
        System.out.println(">> Membro editado.");
        return true;
    }

    public boolean desativarMembro(Usuario usuarioLogado, Membro membro) {
        if (!(usuarioLogado instanceof models.Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem desativar membros.");
            return false;
        }
        Usuario.desativarUsuario(membro);
        System.out.println(">> Membro desativado.");
        return true;
    }

    public List<Membro> listarMembros() {
        return Membro.listarMembros();
    }

    public List<Membro> listarMembrosSuspensos() {
        return Membro.listarMembrosSuspensos();
    }

    public Membro buscarPorCpf(String cpf) {
        try {
            return dao.buscarPorCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}