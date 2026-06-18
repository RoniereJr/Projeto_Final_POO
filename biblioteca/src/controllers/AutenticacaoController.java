package controllers;

import models.Usuario;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento da sessão e autenticação de usuários.
 */
public class AutenticacaoController {

    // Armazena a instância do usuário que está atualmente logado no sistema
    private Usuario usuarioLogado;

    /**
     * Realiza a validação das credenciais e inicia a sessão do usuário.
     * * @param login Nome de usuário fornecido.
     * @param senha Senha fornecida.
     * @return O objeto Usuario correspondente se autenticado com sucesso, ou null em caso de falha.
     */
    public Usuario login(String login, String senha) {
        // Validação preventiva contra entradas nulas ou vazias
        if (login == null || senha == null || login.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Erro: Login e senha não podem estar vazios.");
            return null;
        }

        // Invoca o método estático da Model Usuario para checar as credenciais no banco
        if (!Usuario.autenticar(login, senha)) {
            System.out.println("Erro: Login/senha incorretos ou conta desativada.");
            return null;
        }

        // Se autenticado, percorre a lista de usuários para encontrar o objeto completo e persistir na sessão
        List<Usuario> usuarios = Usuario.listarUsuarios();
        for (Usuario u : usuarios) {
            // Verifica se o login coincide e se a conta do usuário está ativa
            if (u.getLogin().equals(login) && u.isAtivo()) {
                this.usuarioLogado = u;
                System.out.println(">> Login efetuado. Bem-vindo, " + u.getNome() + "!");
                return u;
            }
        }
        System.out.println("Erro: Usuário não encontrado.");
        return null;
    }

    /**
     * Encerra a sessão ativa do usuário atual, limpando a referência.
     */
    public void logout() {
        if (usuarioLogado != null) {
            System.out.println(">> Sessão encerrada para: " + usuarioLogado.getLogin());
            usuarioLogado = null; // Remove o usuário da sessão
        }
    }

    /**
     * Retorna o usuário que está atualmente logado no sistema.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}