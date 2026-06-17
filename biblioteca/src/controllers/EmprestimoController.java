package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Membro;
import models.Livro;
import models.Emprestimo;
import java.util.List;

public class EmprestimoController {

    public boolean realizarEmprestimo(Usuario usuarioLogado, Livro livro, Membro membro) {
        if (!(usuarioLogado instanceof Bibliotecario) || !usuarioLogado.isAtivo()) {
            System.out.println("Erro: Apenas bibliotecários ativos podem realizar empréstimos.");
            return false;
        }

        if (livro == null || membro == null) {
            System.out.println("Erro: Livro ou Membro inválido.");
            return false;
        }

        if (!membro.isAtivo()) {
            System.out.println("Erro: Não é possível realizar empréstimos para um membro desativado.");
            return false;
        }

        if (livro.getDisponiveis() <= 0) {
            System.out.println("Erro: Não há cópias disponíveis de '" + livro.getTitulo() + "' no acervo.");
            return false;
        }

        Emprestimo emp = Emprestimo.criarEmprestimo(livro, membro);
        if (emp != null) {
            // sincroniza a instância em memória, já que o DAO decrementou o banco
            livro.setDisponiveis(livro.getDisponiveis() - 1);
            System.out.println(">> Empréstimo de '" + livro.getTitulo() + "' registrado para " + membro.getNome());
            return true;
        }
        return false;
    }

    public boolean processarDevolucao(Usuario usuarioLogado, Emprestimo emprestimo) {
        if (!(usuarioLogado instanceof Bibliotecario) || !usuarioLogado.isAtivo()) {
            System.out.println("Erro: Acesso negado.");
            return false;
        }

        if (emprestimo == null || emprestimo.isDevolvido()) {
            System.out.println("Erro: Registro de empréstimo inválido ou já finalizado.");
            return false;
        }

        
        emprestimo.registrarDevolucao();
        
        
        Emprestimo.editarEmprestimo(emprestimo, emprestimo.getDataEmprestimo(), 
                emprestimo.getDataDevolucaoPrevista(), emprestimo.getDataDevolucaoReal(), true);

        
        Livro livro = emprestimo.getLivro();
        livro.setDisponiveis(livro.getDisponiveis() + 1);

        int multa = emprestimo.calcularMulta();
        if (multa > 0) {
            System.out.println(">> Devolução registrada. ATENÇÃO: Aplicar multa de " + multa + " dias de atraso.");
        } else {
            System.out.println(">> Devolução concluída com sucesso dentro do prazo regulamentar.");
        }
        return true;
    }

    public List<Emprestimo> listarTodosEmprestimos(Usuario usuarioLogado) {
        if (!(usuarioLogado instanceof Bibliotecario)) return List.of();
        return Emprestimo.listarEmprestimos();
    }

    public List<Emprestimo> listarAtivos(Usuario usuarioLogado) {
        if (!(usuarioLogado instanceof Bibliotecario)) return List.of();
        return Emprestimo.listarEmprestimosAtivos();
    }

    public List<Emprestimo> listarAtrasados(Usuario usuarioLogado) {
        if (!(usuarioLogado instanceof Bibliotecario)) return List.of();
        return Emprestimo.listarEmprestimosAtrasados();
    }
}