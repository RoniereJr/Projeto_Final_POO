package dao;

import java.sql.*;

public class BibliotecaFactory {

    private static final String DATABASE_URL = "jdbc:sqlite:database/biblioteca.db";
    private static BibliotecaFactory instance;
    private static Connection connection;

    private BibliotecaFactory() {
        criarBancoDeDados();
    }

    public static synchronized BibliotecaFactory getInstance() {
        if (instance == null) {
            instance = new BibliotecaFactory();
        }

        return instance;
    }

    private void criarBancoDeDados() {
        String tableUsuario = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "cpf TEXT PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "login TEXT UNIQUE NOT NULL," +
                "senha TEXT NOT NULL," +
                "ativo INTEGER DEFAULT 1" +
                ")";

        String tableBibliotecario = "CREATE TABLE IF NOT EXISTS bibliotecarios (" +
                "cpf TEXT PRIMARY KEY," +
                "cargo TEXT NOT NULL CHECK(cargo IN ('ESTAGIARIO', 'ATENDENTE', 'SUPERVISOR'))," +
                "FOREIGN KEY (cpf) REFERENCES usuarios(cpf) ON DELETE CASCADE" +
                ")";

        String tableMembro = "CREATE TABLE IF NOT EXISTS membros (" +
                "cpf TEXT PRIMARY KEY," +
                "endereco TEXT NOT NULL," +
                "telefone TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "FOREIGN KEY (cpf) REFERENCES usuarios(cpf) ON DELETE CASCADE" +
                ")";

        String tableLivro = "CREATE TABLE IF NOT EXISTS livros (" +
                "isbn TEXT PRIMARY KEY," +
                "titulo TEXT NOT NULL," +
                "autor TEXT NOT NULL," +
                "anoPublicacao INTEGER NOT NULL," +
                "numeroCopias INTEGER NOT NULL CHECK(numeroCopias >= 0)," +
                "disponiveis INTEGER NOT NULL CHECK(disponiveis >= 0)" +
                ")";

        String tableEmprestimo = "CREATE TABLE IF NOT EXISTS emprestimos (" +
                "livro_isbn TEXT NOT NULL," +
                "membro_cpf TEXT NOT NULL," +
                "dataEmprestimo TEXT NOT NULL," +
                "dataDevolucaoPrevista TEXT NOT NULL," +
                "dataDevolucaoReal TEXT," +
                "devolvido INTEGER DEFAULT 0," +
                "PRIMARY KEY (livro_isbn, membro_cpf, dataEmprestimo)," +
                "FOREIGN KEY (livro_isbn) REFERENCES livros(isbn)," +
                "FOREIGN KEY (membro_cpf) REFERENCES membros(cpf)" +
                ")";

        try (Connection con = DriverManager.getConnection(DATABASE_URL); Statement stm = con.createStatement()) {
            stm.executeUpdate(tableUsuario);
            stm.executeUpdate(tableBibliotecario);
            stm.executeUpdate(tableMembro);
            stm.executeUpdate(tableLivro);
            stm.executeUpdate(tableEmprestimo);
            System.out.println("Banco de dados verificado");
        } catch (SQLException e) {
            System.err.println("Erro ao criar banco de dados: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DATABASE_URL);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver SQLite não encontrado!", e);
            }
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = BibliotecaFactory.getConnection();
            System.out.println("Conexão DB estabelecida");
            con.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
