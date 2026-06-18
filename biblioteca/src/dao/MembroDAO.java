package dao;

import models.Membro;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MembroDAO {

    private Membro mapear(ResultSet rs) throws SQLException {
        return new Membro(
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("login"),
                rs.getString("senha"),
                rs.getInt("ativo") == 1,
                rs.getString("endereco"),
                rs.getString("telefone"),
                rs.getString("email"));
    }

    public Membro criarMembro(String nome, String cpf, String login, String senha,
            String endereco, String telefone, String email) throws SQLException {
        String sqlU = "INSERT INTO usuarios (cpf, nome, login, senha, ativo) VALUES (?, ?, ?, ?, 1)";
        String sqlM = "INSERT INTO membros (cpf, endereco, telefone, email) VALUES (?, ?, ?, ?)";

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psU = con.prepareStatement(sqlU);
                PreparedStatement psM = con.prepareStatement(sqlM)) {

            psU.setString(1, cpf);
            psU.setString(2, nome);
            psU.setString(3, login);
            psU.setString(4, senha);
            psU.executeUpdate();

            psM.setString(1, cpf);
            psM.setString(2, endereco);
            psM.setString(3, telefone);
            psM.setString(4, email);
            psM.executeUpdate();

            con.commit();
            return new Membro(nome, cpf, login, senha, true, endereco, telefone, email);
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public Membro buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT u.*, m.endereco, m.telefone, m.email " +
                "FROM usuarios u JOIN membros m ON u.cpf = m.cpf WHERE u.cpf = ?";
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

    public List<Membro> listarMembros() throws SQLException {
        String sql = "SELECT u.*, m.endereco, m.telefone, m.email " +
                "FROM usuarios u JOIN membros m ON u.cpf = m.cpf ORDER BY u.nome";
        List<Membro> lista = new ArrayList<>();
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(mapear(rs));
        }
        return lista;
    }

    public void editarMembro(Membro membro, String nome, String cpf, String login,
            String senha, boolean ativo,
            String endereco, String telefone, String email) throws SQLException {
        String sqlU = "UPDATE usuarios SET nome=?, login=?, senha=?, ativo=? WHERE cpf=?";
        String sqlM = "UPDATE membros SET endereco=?, telefone=?, email=? WHERE cpf=?";

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psU = con.prepareStatement(sqlU);
                PreparedStatement psM = con.prepareStatement(sqlM)) {

            psU.setString(1, nome);
            psU.setString(2, login);
            psU.setString(3, senha);
            psU.setInt(4, ativo ? 1 : 0);
            psU.setString(5, membro.getCpf());
            psU.executeUpdate();

            psM.setString(1, endereco);
            psM.setString(2, telefone);
            psM.setString(3, email);
            psM.setString(4, membro.getCpf());
            psM.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public List<Membro> listarMembrosSuspensos() throws SQLException {
        List<Membro> todos = listarMembros();
        List<Membro> suspensos = new ArrayList<>();
        for (Membro m : todos) {
            if (membroEstaSuspenso(m.getCpf())) {
                suspensos.add(m);
            }
        }
        return suspensos;
    }

    private boolean membroEstaSuspenso(String cpf) throws SQLException {
        String sql = "SELECT dataDevolucaoPrevista, dataDevolucaoReal " +
                "FROM emprestimos WHERE membro_cpf = ? AND devolvido = 1 " +
                "AND dataDevolucaoReal > dataDevolucaoPrevista";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate prev = LocalDate.parse(rs.getString("dataDevolucaoPrevista"));
                    LocalDate real = LocalDate.parse(rs.getString("dataDevolucaoReal"));
                    int diasAtraso = (int) ChronoUnit.DAYS.between(prev, real);
                    LocalDate fimSuspensao = real.plusDays(diasAtraso);
                    if (!LocalDate.now().isAfter(fimSuspensao))
                        return true;
                }
            }
        }
        return false;
    }
}
