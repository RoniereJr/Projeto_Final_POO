package models;

import java.util.List;

public class Livro {
    // ATRIBUTOS
    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int numeroCopias;
    private int disponiveis;

    // CONSTRUTOR
    public Livro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroCopias = numeroCopias;
        this.disponiveis = disponiveis;
    }

    // METODOS
    public void editarLivro(Livro livro, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroCopias = numeroCopias;
        this.disponiveis = disponiveis;
    }

    public void excluirLivro(Livro livro){

    }

    public void adiconarCopias(){

    }

    public void removerCopias(){

    }

//    public List<Livro> listarLivro(){
//
//    }
}
