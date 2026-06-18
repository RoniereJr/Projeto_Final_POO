package models;

import dao.LivroDAO;
import java.sql.SQLException;
import java.util.List;

public class Livro {

    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int numeroCopias;
    private int disponiveis;

    public Livro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroCopias = numeroCopias;
        this.disponiveis = disponiveis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public int getDisponiveis() {
        return disponiveis;
    }

    public void setDisponiveis(int disponiveis) {
        this.disponiveis = disponiveis;
    }

    public void verLivro() {
        System.out.println("ISBN: " + isbn + " | Titulo: " + titulo + " | Autor: " + autor +
                " | Ano: " + anoPublicacao + " | Copias: " + numeroCopias +
                " | Disponiveis: " + disponiveis);
    }

    public void adicionarCopias(int quantidade) {
        this.numeroCopias += quantidade;
        this.disponiveis += quantidade;
        try {
            new LivroDAO().adicionarCopias(this, quantidade);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerCopias(int quantidade) {
        if (quantidade > 0 && quantidade <= this.numeroCopias) {
            this.numeroCopias -= quantidade;
            if (this.disponiveis > quantidade)
                this.disponiveis -= quantidade;
            else
                this.disponiveis = 0;
            try {
                new LivroDAO().removerCopias(this, quantidade);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Livro criarLivro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        try {
            return new LivroDAO().criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, numeroCopias);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void editarLivro(String isbn, String titulo, String autor,
            int anoPublicacao, int numeroCopias, int disponiveis) {
        try {
            new LivroDAO().editarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirLivro(Livro livro) {
        try {
            new LivroDAO().excluirLivro(livro);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Livro> listarLivros() {
        try {
            return new LivroDAO().listarLivros();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
