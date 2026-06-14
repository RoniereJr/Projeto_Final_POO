package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Livro;
import java.util.List;

public class LivroController {

    /**
     Cadastra um novo livro no sistema;
     regra de negocio: apenas bibliotecários podem gerenciar o cadastro de livros.
     */
    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias) {
        // Validação de segurança baseada no Requisito do Sistema
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar livros no acervo.");
            return false;
        }

        // validações de consistência de dados da View
        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Erro: O ISBN do livro é obrigatório.");
            return false;
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Erro: O título do livro é obrigatório.");
            return false;
        }
        if (numeroCopias <= 0) {
            System.out.println("Erro: O número de cópias deve ser maior que zero.");
            return false;
        }

        
        Livro novoLivro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, numeroCopias);

        System.out.println("Livro '" + novoLivro.getTitulo() + "' instanciado com sucesso!");
        return true;
    }

    /**
     * edita os dados de um livro já existente;
     * Regra de negócio: Apenas bibliotecários podem alterar dados do acervo.
     */
    public boolean alterarLivro(Usuario usuarioLogado, Livro livroExistente, String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Permissão negada. Apenas bibliotecários podem editar livros.");
            return false;
        }

        if (livroExistente == null) {
            System.out.println("Erro: Livro inválido ou inexistente.");
            return false;
        }

        
        Livro.editarLivro(livroExistente, isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
        System.out.println("Dados do livro '" + livroExistente.getTitulo() + "' atualizados com sucesso.");
        return true;
    }

    /**
       exibe o acervo mapeado;
       regra de negócio: Cada membro pode visualizar o acervo da biblioteca.
     */
    public void visualizarAcervo(List<Livro> todosOsLivros) {
        if (todosOsLivros == null || todosOsLivros.isEmpty()) {
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
    }
}