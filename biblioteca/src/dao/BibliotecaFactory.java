package dao;

import java.sql.*;

public class BibliotecaFactory {

    static{
        try{
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private static final String DATABASE_URL = "jdbc:sqlite:database/biblioteca.db";
    private static BibliotecaFactory instance;
    private Connection connection;

    private BibliotecaFactory(){
        criarBancoDeDados();
    }
    
    public static synchronized BibliotecaFactory getInstance(){
        if(instance == null){
            instance = new BibliotecaFactory();
        }

        return instance;
    }

    private void criarBancoDeDados(){
        String tableUsuario = "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "cpf TEXT UNIQUE NOT NULL," +
            "login TEXT UNIQUE NOT NULL," +
            "senha TEXT NOT NULL," +
            "ativo INTEGER DEFAULT 1" +
            ")";

        String tableBibliotecario = "CREATE TABLE IF NOT EXISTS bibliotecarios (" +
            "id INTEGER PRIMARY KEY," +
            "cargo TEXT NOT NULL CHECK(cargo IN ('ESTAGIARIO', 'ATENDENTE', 'SUPERVISOR'))," +
            "FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE" +
            ")";

        String tableMembro = "CREATE TABLE IF NOT EXISTS membros (" +
            "id INTEGER PRIMARY KEY," +
            "endereco TEXT NOT NULL," +
            "telefone TEXT NOT NULL," +
            "email TEXT NOT NULL," +
            "FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE" +
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
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "livro_isbn TEXT NOT NULL," +
            "membro_id INTEGER NOT NULL," +
            "dataEmprestimo DATE NOT NULL," +
            "dataDevolucaoPrevista DATE NOT NULL," +
            "dataDevolucaoReal DATE," +
            "devolvido INTEGER DEFAULT 0," +
            "FOREIGN KEY (livro_isbn) REFERENCES livros(isbn)," +
            "FOREIGN KEY (membro_id) REFERENCES membros(id)" +
            ")";

        try(Connection con = DriverManager.getConnection(DATABASE_URL); Statement stm = con.createStatement()){
            stm.executeUpdate(tableUsuario);
            stm.executeUpdate(tableBibliotecario);
            stm.executeUpdate(tableMembro);
            stm.executeUpdate(tableLivro);
            stm.executeUpdate(tableEmprestimo);
            System.out.println("Banco de dados verificado");
        } catch(SQLException e){
            System.err.println("Erro ao criar banco de dados: " + e.getMessage());
        }
    }
    
    public Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(DATABASE_URL);
        }

        return connection;
    }

    public void closeConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    public static void main(String[] args) {
        BibliotecaFactory factory = BibliotecaFactory.getInstance();
        try{
            Connection con = factory.getConnection();
            System.out.println("Conexão DB estabelecida");
            con.close();
        } catch(SQLException e){
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
