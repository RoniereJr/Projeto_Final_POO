package controllers;

import models.Bibliotecario;
import models.Cargo;
import models.Usuario;
import dao.BibliotecarioDAO;
import java.sql.SQLException;
import java.util.List;

public class BibliotecarioController {

    private BibliotecarioDAO dao = new BibliotecarioDAO();

    public boolean cadastrarBibliotecario(Usuario usuarioLogado, String nome, String cpf,
            String login, String senha, Cargo cargo) {
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem cadastrar bibliotecários.");
            return false;
        }
        if (nome == null || cpf == null || login == null || senha == null || cargo == null) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return false;
        }
        Bibliotecario novo = Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);
        if (novo != null) {
            System.out.println(">> Bibliotecário cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (CPF ou login duplicado?).");
        return false;
    }

    public boolean editarBibliotecario(Usuario usuarioLogado, Bibliotecario bibliotecario,
            String nome, String login, String senha,
            boolean ativo, Cargo cargo) {
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem editar bibliotecários.");
            return false;
        }

        Bibliotecario.editarBibliotecario(bibliotecario, nome, bibliotecario.getCpf(),
                login, senha, ativo, cargo);
        System.out.println(">> Bibliotecário editado.");
        return true;
    }

    public boolean desativarBibliotecario(Usuario usuarioLogado, Bibliotecario bibliotecario) {
        if (!(usuarioLogado instanceof Bibliotecario) ||
                ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Apenas SUPERVISORES podem desativar bibliotecários.");
            return false;
        }
        Usuario.desativarUsuario(bibliotecario);
        System.out.println(">> Bibliotecário desativado.");
        return true;
    }

    public List<Bibliotecario> listarBibliotecarios() {
        return Bibliotecario.listarBibliotecarios();
    }

    public Bibliotecario buscarPorCpf(String cpf) {
        try {
            return dao.buscarPorCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}