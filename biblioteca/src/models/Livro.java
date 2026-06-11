package models;

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

    public static Livro criarLivro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis){
        return new Livro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
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

    public static void editarLivro(Livro livro, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        livro.setIsbn(isbn);
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setNumeroCopias(numeroCopias);
        livro.setDisponiveis(disponiveis);   
    }

    public void excluirLivro(Livro livro){
    }

    public void adiconarCopias(int numeroCopias){
        this.numeroCopias += numeroCopias;
    }

    public void removerCopias(int numeroCopias){
        this.numeroCopias -= numeroCopias;
    }
}
