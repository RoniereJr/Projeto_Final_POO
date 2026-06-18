package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import models.Emprestimo;
import models.Livro;
import models.Membro;

public class EmprestimoDAO {

    private Emprestimo mapear(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getString("livro_isbn"),
                rs.getString("livro_titulo"),
                rs.getString("livro_autor"),
                rs.getInt("livro_anoPublicacao"),
                rs.getInt("livro_numeroCopias"),
                rs.getInt("livro_disponiveis"));
        Membro membro = new Membro(
                rs.getString("membro_nome"),
                rs.getString("membro_cpf"),
                rs.getString("membro_login"),
                rs.getString("membro_senha"),
                rs.getInt("membro_ativo") == 1,
                rs.getString("membro_endereco"),
                rs.getString("membro_telefone"),
                rs.getString("membro_email"));
        String realStr = rs.getString("dataDevolucaoReal");
        return new Emprestimo(
                livro,
                membro,
                LocalDate.parse(rs.getString("dataEmprestimo")),
                LocalDate.parse(rs.getString("dataDevolucaoPrevista")),
                (realStr != null) ? LocalDate.parse(realStr) : null,
                rs.getInt("devolvido") == 1);
    }

    public Emprestimo criarEmprestimo(Livro livro, Membro membro) throws SQLException {
        String sqlEmp = "INSERT INTO emprestimos (livro_isbn, membro_cpf, dataEmprestimo, " +
                "dataDevolucaoPrevista, devolvido) VALUES (?, ?, ?, ?, 0)";
        String sqlLivro = "UPDATE livros SET disponiveis = disponiveis - 1 WHERE isbn = ?";

        LocalDate hoje = LocalDate.now();
        LocalDate prevista = hoje.plusDays(14);

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psE = con.prepareStatement(sqlEmp);
                PreparedStatement psL = con.prepareStatement(sqlLivro)) {

            psE.setString(1, livro.getIsbn());
            psE.setString(2, membro.getCpf());
            psE.setString(3, hoje.toString());
            psE.setString(4, prevista.toString());
            psE.executeUpdate();

            psL.setString(1, livro.getIsbn());
            psL.executeUpdate();

            con.commit();
            return new Emprestimo(livro, membro, hoje, prevista, null, false);
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public void editarEmprestimo(String livroIsbn, String membroCpf, LocalDate dataOriginal,
            LocalDate novaDataEmprestimo, LocalDate novaDataPrevista,
            LocalDate novaDataReal, boolean novoDevolvido) throws SQLException {
        String sql = "UPDATE emprestimos SET dataEmprestimo = ?, dataDevolucaoPrevista = ?, " +
                "dataDevolucaoReal = ?, devolvido = ? " +
                "WHERE livro_isbn = ? AND membro_cpf = ? AND dataEmprestimo = ?";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, novaDataEmprestimo.toString());
            ps.setString(2, novaDataPrevista.toString());
            ps.setString(3, novaDataReal != null ? novaDataReal.toString() : null);
            ps.setInt(4, novoDevolvido ? 1 : 0);
            ps.setString(5, livroIsbn);
            ps.setString(6, membroCpf);
            ps.setString(7, dataOriginal.toString());
            ps.executeUpdate();
        }
    }

    public void excluirEmprestimo(String livroIsbn, String membroCpf, LocalDate dataEmprestimo) throws SQLException {
        String sql = "DELETE FROM emprestimos WHERE livro_isbn = ? AND membro_cpf = ? AND dataEmprestimo = ?";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, livroIsbn);
            ps.setString(2, membroCpf);
            ps.setString(3, dataEmprestimo.toString());
            ps.executeUpdate();
        }
    }

    public int registrarDevolucao(String livroIsbn, String membroCpf, LocalDate dataEmprestimo) throws SQLException {
        String sqlSelect = "SELECT dataDevolucaoPrevista FROM emprestimos WHERE livro_isbn = ? AND membro_cpf = ? AND dataEmprestimo = ? AND devolvido = 0";
        LocalDate dataPrevista;
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sqlSelect)) {
            ps.setString(1, livroIsbn);
            ps.setString(2, membroCpf);
            ps.setString(3, dataEmprestimo.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    throw new SQLException("Empréstimo não encontrado ou já devolvido.");
                dataPrevista = LocalDate.parse(rs.getString("dataDevolucaoPrevista"));
            }
        }

        LocalDate hoje = LocalDate.now();
        int diasAtraso = (int) (hoje.toEpochDay() - dataPrevista.toEpochDay());
        if (diasAtraso < 0)
            diasAtraso = 0;

        String sqlUpdate = "UPDATE emprestimos SET devolvido = 1, dataDevolucaoReal = ? " +
                "WHERE livro_isbn = ? AND membro_cpf = ? AND dataEmprestimo = ?";
        String sqlLivro = "UPDATE livros SET disponiveis = disponiveis + 1 WHERE isbn = ?";

        Connection con = BibliotecaFactory.getConnection();
        con.setAutoCommit(false);
        try (PreparedStatement psU = con.prepareStatement(sqlUpdate);
                PreparedStatement psL = con.prepareStatement(sqlLivro)) {

            psU.setString(1, hoje.toString());
            psU.setString(2, livroIsbn);
            psU.setString(3, membroCpf);
            psU.setString(4, dataEmprestimo.toString());
            psU.executeUpdate();

            psL.setString(1, livroIsbn);
            psL.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }

        return diasAtraso;
    }

    private List<Emprestimo> executarListaCompleta(String sql, Object... params) throws SQLException {
        String whereClause = "";
        String orderClause = "";
        int whereIdx = sql.indexOf("WHERE");
        if (whereIdx != -1) {
            whereClause = sql.substring(whereIdx);
            int orderIdx = whereClause.indexOf("ORDER BY");
            if (orderIdx != -1) {
                orderClause = whereClause.substring(orderIdx);
                whereClause = whereClause.substring(0, orderIdx);
            }
        } else {
            int orderIdx = sql.indexOf("ORDER BY");
            if (orderIdx != -1) {
                orderClause = sql.substring(orderIdx);
            }
        }

        String joinSql = "SELECT e.*, " +
                "l.titulo AS livro_titulo, l.autor AS livro_autor, " +
                "l.anoPublicacao AS livro_anoPublicacao, l.numeroCopias AS livro_numeroCopias, " +
                "l.disponiveis AS livro_disponiveis, " +
                "u.nome AS membro_nome, u.cpf AS membro_cpf, u.login AS membro_login, " +
                "u.senha AS membro_senha, u.ativo AS membro_ativo, " +
                "m.endereco AS membro_endereco, m.telefone AS membro_telefone, m.email AS membro_email " +
                "FROM emprestimos e " +
                "JOIN livros l ON e.livro_isbn = l.isbn " +
                "JOIN membros m ON e.membro_cpf = m.cpf " +
                "JOIN usuarios u ON m.cpf = u.cpf";

        if (!whereClause.isEmpty()) {
            joinSql += " " + whereClause;
        }
        if (!orderClause.isEmpty()) {
            joinSql += " " + orderClause;
        }

        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(joinSql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprestimo> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        }
    }

    public List<Emprestimo> listarEmprestimosPorMembro(Membro membro) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE membro_cpf = ? ORDER BY dataEmprestimo DESC";
        return executarListaCompleta(sql, membro.getCpf());
    }

    public List<Emprestimo> listarEmprestimos() throws SQLException {
        String sql = "SELECT * FROM emprestimos ORDER BY dataEmprestimo DESC";
        return executarListaCompleta(sql);
    }

    public List<Emprestimo> listarEmprestimosAtivos() throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE devolvido = 0 ORDER BY dataDevolucaoPrevista";
        return executarListaCompleta(sql);
    }

    public List<Emprestimo> listarEmprestimosAtrasados() throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE devolvido = 0 AND dataDevolucaoPrevista < ? ORDER BY dataDevolucaoPrevista";
        return executarListaCompleta(sql, LocalDate.now().toString());
    }

    public List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE membro_cpf = ? AND devolvido = 0 AND dataDevolucaoPrevista < ? ORDER BY dataDevolucaoPrevista";
        return executarListaCompleta(sql, membro.getCpf(), LocalDate.now().toString());
    }

    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) throws SQLException {
        String sql = "SELECT * FROM emprestimos WHERE membro_cpf = ? AND devolvido = 0 ORDER BY dataEmprestimo DESC";
        return executarListaCompleta(sql, membro.getCpf());
    }
}