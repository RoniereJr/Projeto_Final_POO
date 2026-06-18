package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Cargo;
import java.util.List;

public class UsuarioController {

    public boolean editarUsuario(Usuario usuarioLogado, Usuario usuarioAlvo,
            String nome, String cpf, String login,
            String senha, boolean ativo) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar usuários.");
            return false;
        }
        if (usuarioAlvo instanceof Bibliotecario &&
                !(usuarioLogado instanceof Bibliotecario &&
                        ((Bibliotecario) usuarioLogado).getCargo() == Cargo.SUPERVISOR)) {
            System.out.println("Erro: Apenas SUPERVISORES podem editar bibliotecários.");
            return false;
        }
        Usuario.editarUsuario(usuarioAlvo, nome, cpf, login, senha, ativo);
        System.out.println(">> Usuário editado.");
        return true;
    }

    public boolean desativarUsuario(Usuario usuarioLogado, Usuario usuarioAlvo) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem desativar contas.");
            return false;
        }
        if (usuarioAlvo instanceof Bibliotecario &&
                !(usuarioLogado instanceof Bibliotecario &&
                        ((Bibliotecario) usuarioLogado).getCargo() == Cargo.SUPERVISOR)) {
            System.out.println("Erro: Apenas SUPERVISORES podem desativar bibliotecários.");
            return false;
        }
        Usuario.desativarUsuario(usuarioAlvo);
        System.out.println(">> Conta desativada.");
        return true;
    }

    public List<Usuario> listarUsuarios() {
        return Usuario.listarUsuarios();
    }
}