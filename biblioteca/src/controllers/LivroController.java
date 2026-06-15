package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Livro;

public class LivroController {

    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar livros no acervo.");
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty() || numeroCopias <= 0) {
            System.out.println("Erro: Dados de livro inválidos ou incompletos.");
            return false;
        }

        // chama o método estatico da Model Livro
//        Livro novoLivro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
//        
//        if (novoLivro != null) {
//            System.out.println(">> Livro '" + titulo + "' cadastrado com sucesso via Model!");
//            return true;
//        }
        return false;
    }

    public boolean removerLivro(Usuario usuarioLogado, Livro livro) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada. Apenas bibliotecários podem excluir livros.");
            return false;
        }

        if (livro == null) {
            System.out.println("Erro: Livro inválido.");
            return false;
        }

        // chamada ESTÁTICA passando o objeto livro;;;;
//            Livro.excluirLivro(livro); ;;
//            System.out.println(">> Livro removido do acervo via Model.");
//            return true;
        return false;
 

        }
    }