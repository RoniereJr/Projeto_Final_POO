package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Livro;
import java.util.List;

public class LivroController {

    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem gerenciar o catálogo de livros.");
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty() || numeroCopias <= 0) {
            System.out.println("Erro: Parâmetros do livro inválidos ou incompletos.");
            return false;
        }

        Livro novoLivro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        if (novoLivro != null) {
            System.out.println(">> Livro '" + titulo + "' inserido no catálogo com sucesso!");
            return true;
        }
        return false;
    }

    public boolean alterarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada.");
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Erro: ISBN identificador obrigatório.");
            return false;
        }

        
        Livro.editarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
        System.out.println(">> Dados do livro atualizados via Model.");
        return true;
    }

    public boolean removerLivro(Usuario usuarioLogado, Livro livro) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada.");
            return false;
        }

        if (livro == null) {
            System.out.println("Erro: Livro inválido.");
            return false;
        }

        Livro.excluirLivro(livro); 
        System.out.println(">> Livro removido do catálogo via Model.");
        return true;
    }

    public List<Livro> obterAcervo() {
        return Livro.listarLivros();
    }
}