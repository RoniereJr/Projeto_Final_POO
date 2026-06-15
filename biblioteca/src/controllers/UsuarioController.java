package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Membro;
import models.Cargo;

public class UsuarioController {

    public boolean cadastrarBibliotecario(Usuario usuarioLogado, String nome, String cpf, String login, String senha, Cargo cargo) {
        if (!(usuarioLogado instanceof Bibliotecario) || ((Bibliotecario) usuarioLogado).getCargo() != Cargo.SUPERVISOR) {
            System.out.println("Erro: Acesso negado. Apenas SUPERVISORES cadastram bibliotecários.");
            return false;
        }

        if (nome == null || cpf == null || login == null || senha == null || cargo == null) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return false;
        }

        // chama o método estático da Model
        boolean sucesso = Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);
        return sucesso;
    }

    public boolean cadastrarMembro(Usuario usuarioLogado, String nome, String cpf, String login, String senha, String endereco, String telefone, String email) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Acesso negado. Apenas bibliotecários cadastram membros.");
            return false;
        }

        if (nome == null || cpf == null || login == null || senha == null) {
            System.out.println("Erro: Dados obrigatórios ausentes.");
            return false;
        }

        // chama o método estático da Model Membro
        boolean sucesso = Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);
        return sucesso;
    }
}