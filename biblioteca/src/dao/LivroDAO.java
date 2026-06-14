package dao;

import models.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private final BibliotecaFactory factory = BibliotecaFactory.getInstance();

    private Livro mapear(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("anoPublicacao"),
                rs.getInt("numeroCopias"),
                rs.getInt("disponiveis"));
    }

    public Livro criarLivro(String isbn, String titulo, String autor,
            int anoPublicacao, int numeroCopias, int disponiveis)
            throws SQLException {
        String sql = "INSERT INTO livros (isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setString(2, titulo);
            ps.setString(3, autor);
            ps.setInt(4, anoPublicacao);
            ps.setInt(5, numeroCopias);
            ps.setInt(6, disponiveis);
            ps.executeUpdate();
        }
        return new Livro(isbn, titulo, autor, anoPublicacao, numeroCopias, disponiveis);
    }

    public Livro buscarPorIsbn(String isbn) throws SQLException {
        String sql = "SELECT * FROM livros WHERE isbn = ?";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapear(rs);
            }
        }
        return null;
    }

    public List<Livro> listarLivros() throws SQLException {
        String sql = "SELECT * FROM livros ORDER BY titulo";
        List<Livro> lista = new ArrayList<>();
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapear(rs));
        }
        return lista;
    }

    public void editarLivro(String isbn, String titulo, String autor,
            int anoPublicacao, int numeroCopias, int disponiveis) throws SQLException {
        String sql = "UPDATE livros SET titulo=?, autor=?, anoPublicacao=?, numeroCopias=?, disponiveis=? " +
                "WHERE isbn=?";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, autor);
            ps.setInt(3, anoPublicacao);
            ps.setInt(4, numeroCopias);
            ps.setInt(5, disponiveis);
            ps.setString(6, isbn);
            ps.executeUpdate();
        }
    }

    public void excluirLivro(Livro livro) throws SQLException {
        String sql = "DELETE FROM livros WHERE isbn = ?";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, livro.getIsbn());
            ps.executeUpdate();
        }
    }

    public void adicionarCopias(Livro livro, int quantidade) throws SQLException {
        String sql = "UPDATE livros SET numeroCopias = numeroCopias + ?, " +
                "disponiveis = disponiveis + ? WHERE isbn = ?";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantidade);
            ps.setInt(2, quantidade);
            ps.setString(3, livro.getIsbn());
            ps.executeUpdate();
        }
    }

    public void removerCopias(Livro livro, int quantidade) throws SQLException {
        String sql = "UPDATE livros SET numeroCopias = numeroCopias - ?, " +
                "disponiveis = disponiveis - ? WHERE isbn = ?";
        try (Connection con = factory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantidade);
            ps.setInt(2, quantidade);
            ps.setString(3, livro.getIsbn());
            ps.executeUpdate();
        }
    }
}
