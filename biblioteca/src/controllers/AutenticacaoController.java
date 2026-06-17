package controllers;

import models.Usuario;

public class AutenticacaoController {

    private boolean logado = false;

    public boolean login(String login, String senha) {
        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Erro: Login e senha não podem estar vazios.");
            return false;
        }

        
        boolean sucesso = Usuario.autenticar(login, senha);

        if (!sucesso) {
            System.out.println("Erro: Login ou senha incorretos.");
            this.logado = false;
            return false;
        }

        this.logado = true;
        System.out.println(">> Login efetuado com sucesso via Model!");
        return true;
    }

    public void logout() {
        this.logado = false;
        System.out.println(">> Sessão encerrada.");
    }

    public boolean isLogado() {
        return logado;
    }
}