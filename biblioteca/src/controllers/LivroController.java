package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Livro;
import dao.LivroDAO; 
import java.sql.SQLException;
import java.util.List;

public class LivroController {

    private final LivroDAO livroDAO = new LivroDAO(); //instancia o DAO

    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar livros no acervo.");
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty() || numeroCopias <= 0) {
            System.out.println("Erro: Dados de livro inválidos ou incompletos.");
            return false;
        }

        try {
            // envia diretamente para a tabela 'livros'
            livroDAO.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, numeroCopias);
            System.out.println(">> Livro '" + titulo + "' salvo com sucesso no Banco de Dados!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro de banco ao cadastrar livro: " + e.getMessage());
            return false;
        }
    }

    public boolean alterarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada. Apenas bibliotecários podem editar livros.");
            return false;
        }

        try {
            // atualiza os dados no SQLite 
            livroDAO.editarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
            System.out.println(">> Dados do livro atualizados com sucesso no Banco de Dados.");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro de banco ao alterar livro: " + e.getMessage());
            return false;
        }
    }

    public void visualizarAcervo() {
        try {
            // puxa a lista atualizada direto do banco de dados
            List<Livro> todosOsLivros = livroDAO.listarLivros();
            if (todosOsLivros.isEmpty()) {
                System.out.println("O acervo está vazio no momento.");
                return;
            }
            System.out.println("\n--- ACERVO DA BIBLIOTECA ---");
            for (Livro livro : todosOsLivros) {
                System.out.println("Título: " + livro.getTitulo() + 
                                   " | Autor: " + livro.getAutor() + 
                                   " | ISBN: " + livro.getIsbn() + 
                                   " | Disponíveis: " + livro.getDisponiveis() + "/" + livro.getNumeroCopias());
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar acervo do banco: " + e.getMessage());
        }
    }
}