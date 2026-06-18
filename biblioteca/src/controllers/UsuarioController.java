package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Cargo;
import java.util.List;

/**
 * Controller responsável por gerenciar operações cadastrais generalistas na entidade base de Usuários.
 */
public class UsuarioController {

    /**
     * Valida as regras hierárquicas de permissão e atualiza os dados gerais de uma conta via Model.
     * * @param usuarioLogado Usuário operando o sistema (deve ser Bibliotecário).
     * @param usuarioAlvo Instância do usuário que sofrerá as alterações.
     * @param nome Novo nome.
     * @param cpf Novo CPF.
     * @param login Novo login.
     * @param senha Nova senha.
     * @param ativo Novo status de ativação da conta.
     * @return true se a edição foi efetuada com sucesso, false em caso de impedimento por privilégios.
     */
    public boolean editarUsuario(Usuario usuarioLogado, Usuario usuarioAlvo,
            String nome, String cpf, String login,
            String senha, boolean ativo) {
        // Restrição de acesso global: apenas bibliotecários ativos podem editar contas no sistema
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar usuários.");
            return false;
        }
        // Restrição de hierarquia: se o alvo for um bibliotecário, quem está editando precisa obrigatoriamente ser um SUPERVISOR
        if (usuarioAlvo instanceof Bibliotecario &&
                !(usuarioLogado instanceof Bibliotecario &&
                        ((Bibliotecario) usuarioLogado).getCargo() == Cargo.SUPERVISOR)) {
            System.out.println("Erro: Apenas SUPERVISORES podem editar bibliotecários.");
            return false;
        }
        
        // Invoca o método estático da Model mãe (Usuario) para executar a sincronização dos dados e persistência
        Usuario.editarUsuario(usuarioAlvo, nome, cpf, login, senha, ativo);
        System.out.println(">> Usuário editado.");
        return true;
    }

    /**
     * Valida as regras hierárquicas de permissão e altera o status de atividade de um usuário para falso.
     */
    public boolean desativarUsuario(Usuario usuarioLogado, Usuario usuarioAlvo) {
        // Restrição de acesso global: restrito a funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem desativar contas.");
            return false;
        }
        // Restrição de hierarquia: um funcionário comum não pode desativar a conta de outro funcionário, apenas um SUPERVISOR pode
        if (usuarioAlvo instanceof Bibliotecario &&
                !(usuarioLogado instanceof Bibliotecario &&
                        ((Bibliotecario) usuarioLogado).getCargo() == Cargo.SUPERVISOR)) {
            System.out.println("Erro: Apenas SUPERVISORES podem desativar bibliotecários.");
            return false;
        }
        
        // Delegação do comando de desativação lógica para o método estático contido na Model mãe
        Usuario.desativarUsuario(usuarioAlvo);
        System.out.println(">> Conta desativada.");
        return true;
    }

    /**
     * Retorna a listagem agregada de todos os usuários cadastrados no sistema (Membros e Bibliotecários).
     */
    public List<Usuario> listarUsuarios() {
        return Usuario.listarUsuarios();
    }
}