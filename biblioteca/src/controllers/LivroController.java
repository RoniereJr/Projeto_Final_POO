package controllers;

import models.Usuario;
import models.Bibliotecario;
import models.Livro;
import dao.LivroDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller responsável pelas operações de catálogo, estoque e gerenciamento de Livros.
 */
public class LivroController {

    // Instância direta do DAO para consultas específicas de persistência no SQLite
    private LivroDAO dao = new LivroDAO();

    /**
     * Valida o perfil do usuário e solicita a criação de um novo registro de livro na Model.
     * * @param usuarioLogado Usuário que opera a ação (deve ser um Bibliotecário).
     * @param isbn Código identificador internacional do livro.
     * @param titulo Título da obra.
     * @param autor Autor do livro.
     * @param anoPublicacao Ano em que a obra foi publicada.
     * @param numeroCopias Quantidade inicial de cópias totais.
     * @return true se o cadastro foi concluído com sucesso, false em caso de erro ou dados inválidos.
     */
    public boolean cadastrarLivro(Usuario usuarioLogado, String isbn, String titulo,
                                  String autor, int anoPublicacao, int numeroCopias) {
        // Restrição de acesso: operação permitida exclusivamente para funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem cadastrar livros.");
            return false;
        }

        // Validação preventiva para garantir consistência mínima dos dados antes de invocar a persistência
        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty()
                || numeroCopias <= 0) {
            System.out.println("Erro: Dados inválidos.");
            return false;
        }

        // Invoca o método estático da Model Livro para inserção no banco de dados
        Livro livro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias);
        if (livro != null) {
            System.out.println(">> Livro cadastrado.");
            return true;
        }
        System.out.println("Erro: Falha ao cadastrar (ISBN duplicado?).");
        return false;
    }

    /**
     * Valida as permissões e repassa os dados atualizados para alteração do registro através da Model.
     */
    public boolean editarLivro(Usuario usuarioLogado, String isbn, String titulo,
                               String autor, int anoPublicacao, int numeroCopias, int disponiveis) {
        // Restrição de acesso: restrito a funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem editar livros.");
            return false;
        }
        // Validação de chaves obrigatórias de identificação e integridade do registro
        if (isbn == null || isbn.trim().isEmpty() || titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Erro: Dados inválidos.");
            return false;
        }
        
        // Sincroniza e atualiza os dados no banco invocando o método estático presente na classe de Model
        Livro.editarLivro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
        System.out.println(">> Livro atualizado.");
        return true;
    }

    /**
     * Remove fisicamente um livro do catálogo e do banco de dados chamando a Model.
     */
    public boolean removerLivro(Usuario usuarioLogado, Livro livro) {
        // Restrição de acesso: restrito a funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem remover livros.");
            return false;
        }
        // Validação de segurança da instância recebida
        if (livro == null) {
            System.out.println("Erro: Livro inválido.");
            return false;
        }
        
        // Executa a exclusão repassando a entidade para o método estático da Model
        Livro.excluirLivro(livro);
        System.out.println(">> Livro removido.");
        return true;
    }

    /**
     * Incrementa o estoque físico global e de cópias disponíveis de um livro específico.
     */
    public boolean adicionarCopias(Usuario usuarioLogado, Livro livro, int quantidade) {
        // Restrição de acesso: restrito a funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem adicionar cópias.");
            return false;
        }
        // Garante que a quantidade inserida seja positiva e a instância seja válida
        if (livro == null || quantidade <= 0) {
            System.out.println("Erro: Parâmetros inválidos.");
            return false;
        }
        
        // Executa a alteração incremental chamando o método de instância diretamente no objeto
        livro.adicionarCopias(quantidade); 
        System.out.println(">> Cópias adicionadas.");
        return true;
    }

    /**
     * Decrementa o estoque físico global e de cópias disponíveis de um livro específico.
     */
    public boolean removerCopias(Usuario usuarioLogado, Livro livro, int quantidade) {
        // Restrição de acesso: restrito a funcionários (Bibliotecários)
        if (!(usuarioLogado instanceof Bibliotecario)) {
            System.out.println("Erro: Apenas bibliotecários podem remover cópias.");
            return false;
        }
        // Valida se a quantidade a subtrair faz sentido lógico perante o estoque total existente
        if (livro == null || quantidade <= 0 || quantidade > livro.getNumeroCopias()) {
            System.out.println("Erro: Quantidade inválida.");
            return false;
        }
        
        // Executa a remoção incremental chamando o método de instância diretamente no objeto
        livro.removerCopias(quantidade); 
        System.out.println(">> Cópias removidas.");
        return true;
    }

    /**
     * Retorna a listagem completa de todos os livros catalogados no acervo.
     */
    public List<Livro> listarLivros() {
        return Livro.listarLivros();
    }

    /**
     * Efetua a busca por um livro específico consultando diretamente o banco de dados via DAO pelo ISBN.
     * * @param isbn Código único identificador do livro.
     * @return O objeto Livro correspondente se localizado, ou null em caso de erro/inexistência.
     */
    public Livro buscarPorIsbn(String isbn) {
        try {
            // Executa a consulta de rastreamento mapeada através da instância local de LivroDAO
            return dao.buscarPorIsbn(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}