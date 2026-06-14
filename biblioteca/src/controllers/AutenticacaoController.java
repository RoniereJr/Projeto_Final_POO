package controllers;

import models.Usuario;

public class AutenticacaoController {

    private Usuario usuarioLogado;

    /**
     valida as credenciais chamando o método estático da Model.
     */
    public Usuario login(String login, String senha) {
        // validação de parâmetros
        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Erro: Login e senha não podem estar vazios.");
            return null;
        }

        // chama o método estático da MODEL Usuario
        Usuario usuario = Usuario.autenticar(login, senha);

        if (usuario == null) {
            System.out.println("Erro: Login/senha incorretos ou conta desativada.");
            return null;
        }

        // gerenciamento do estado da sessão local
        this.usuarioLogado = usuario;
        System.out.println(">> Login efetuado com sucesso. Bem-vindo, " + usuario.getNome() + "!");
        return usuario;
    }

    /**
      encerra a sessão do usuário.
     */
    public void logout() {
        if (this.usuarioLogado != null) {
            System.out.println(">> Sessão encerrada para o utilizador: " + this.usuarioLogado.getLogin());
            this.usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}