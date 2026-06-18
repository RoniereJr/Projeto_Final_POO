package dao;

import models.Bibliotecario;
import models.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecarioDAO {

    private Bibliotecario mapear(ResultSet rs) throws SQLException {
        return new Bibliotecario(
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("login"),
                rs.getString("senha"),
                rs.getInt("ativo") == 1,
                Cargo.valueOf(rs.getString("cargo")));
    }

    public Bibliotecario criarBibliotecario(String nome, String cpf, String login, String senha, Cargo cargo)
            throws SQLException {
        String sqlU = "INSERT INTO usuarios (cpf, nome, login, senha, ativo) VALUES (?, ?, ?, ?, 1)";
        String sqlB = "INSERT INTO bibliotecarios (cpf, cargo) VALUES (?, ?)";

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psU = con.prepareStatement(sqlU);
                PreparedStatement psB = con.prepareStatement(sqlB)) {

            psU.setString(1, cpf);
            psU.setString(2, nome);
            psU.setString(3, login);
            psU.setString(4, senha);
            psU.executeUpdate();

            psB.setString(1, cpf);
            psB.setString(2, cargo.name());
            psB.executeUpdate();

            con.commit();
            return new Bibliotecario(nome, cpf, login, senha, true, cargo);
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public Bibliotecario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT u.*, b.cargo FROM usuarios u " +
                "JOIN bibliotecarios b ON u.cpf = b.cpf WHERE u.cpf = ?";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapear(rs);
            }
        }
        return null;
    }

    public List<Bibliotecario> listarBibliotecarios() throws SQLException {
        String sql = "SELECT u.*, b.cargo FROM usuarios u " +
                "JOIN bibliotecarios b ON u.cpf = b.cpf ORDER BY u.nome";
        List<Bibliotecario> lista = new ArrayList<>();
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapear(rs));
        }
        return lista;
    }

    public void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf,
            String login, String senha, boolean ativo, Cargo cargo)
            throws SQLException {
        String sqlU = "UPDATE usuarios SET nome=?, login=?, senha=?, ativo=? WHERE cpf=?";
        String sqlB = "UPDATE bibliotecarios SET cargo=? WHERE cpf=?";

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psU = con.prepareStatement(sqlU);
                PreparedStatement psB = con.prepareStatement(sqlB)) {

            psU.setString(1, nome);
            psU.setString(2, login);
            psU.setString(3, senha);
            psU.setInt(4, ativo ? 1 : 0);
            psU.setString(5, bibliotecario.getCpf());
            psU.executeUpdate();

            psB.setString(1, cargo.name());
            psB.setString(2, bibliotecario.getCpf());
            psB.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
