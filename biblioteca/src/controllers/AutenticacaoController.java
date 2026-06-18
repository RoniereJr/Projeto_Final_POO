package controllers;

import models.Usuario;
import java.util.List;

public class AutenticacaoController {

    private Usuario usuarioLogado;

    public Usuario login(String login, String senha) {
        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Erro: Login e senha não podem estar vazios.");
            return null;
        }

        if (!Usuario.autenticar(login, senha)) {
            System.out.println("Erro: Login/senha incorretos ou conta desativada.");
            return null;
        }

        List<Usuario> usuarios = Usuario.listarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.isAtivo()) {
                this.usuarioLogado = u;
                System.out.println(">> Login efetuado. Bem-vindo, " + u.getNome() + "!");
                return u;
            }
        }
        System.out.println("Erro: Usuário não encontrado.");
        return null;
    }

    public void logout() {
        if (usuarioLogado != null) {
            System.out.println(">> Sessão encerrada para: " + usuarioLogado.getLogin());
            usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}