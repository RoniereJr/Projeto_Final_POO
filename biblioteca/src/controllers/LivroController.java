package controllers;


import models.Usuario;
import models.Bibliotecario;
import models.Livro;
import dao.LivroDAO;
import java.sql.SQLException;
import java.util.List;


public class LivroController {

    private LivroDAO dao = new LivroDAO();

    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo,
                                  String autor, int anoPublicacao, int numeroCopias) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar livros.");
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty()
                || numeroCopias <= 0) {
            System.out.println("Erro: Dados inválidos.");
            return false;
        }

        Livro livro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        if (livro != null) {
            System.out.println(">> Livro cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (ISBN duplicado?).");
        return false;
    }

    public boolean editarLivro(Usuario usuarioLogado, String isbn, String titulo,
                               String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar livros.");
            return false;
        }
        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Erro: Dados inválidos.");
            return false;
        }
        
        Livro.editarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
        System.out.println(">> Livro atualizado.");
        return true;
    }

    public boolean removerLivro(Usuario usuarioLogado, Livro livro) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem remover livros.");
            return false;
        }
        if (livro == null) {
            System.out.println("Erro: Livro inválido.");
            return false;
        }
        Livro.excluirLivro(livro);
        System.out.println(">> Livro removido.");
        return true;
    }

    public boolean adicionarCopias(Usuario usuarioLogado, Livro livro, int quantidade) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem adicionar cópias.");
            return false;
        }
        if (livro == null || quantidade <= 0) {
            System.out.println("Erro: Parâmetros inválidos.");
            return false;
        }
        livro.adicionarCopias(quantidade); // método de instância
        System.out.println(">> Cópias adicionadas.");
        return true;
    }

    public boolean removerCopias(Usuario usuarioLogado, Livro livro, int quantidade) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem remover cópias.");
            return false;
        }
        if (livro == null || quantidade <= 0 || quantidade > livro.getNumeroCopias()) {
            System.out.println("Erro: Quantidade inválida.");
            return false;
        }
        livro.removerCopias(quantidade); // método de instância
        System.out.println(">> Cópias removidas.");
        return true;
    }

    public List<Livro> listarLivros() {
        return Livro.listarLivros();
    }

    public Livro buscarPorIsbn(String isbn) {
        try {
            return dao.buscarPorIsbn(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}