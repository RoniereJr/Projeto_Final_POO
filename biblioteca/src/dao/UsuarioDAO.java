package dao;

import models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void editarUsuario(Usuario usuario, String nome, String cpf,
            String login, String senha, boolean ativo) throws SQLException {
        String sql = "UPDATE usuarios SET nome=?, login=?, senha=?, ativo=? WHERE cpf=?";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, login);
            ps.setString(3, senha);
            ps.setInt(4, ativo ? 1 : 0);
            ps.setString(5, usuario.getCpf());
            ps.executeUpdate();
        }
    }

    public void desativarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = 0 WHERE cpf = ?";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getCpf());
            ps.executeUpdate();
        }
    }

    public boolean autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE login = ? AND senha = ? AND ativo = 1";
        try (Connection con = BibliotecaFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        lista.addAll(new BibliotecarioDAO().listarBibliotecarios());
        lista.addAll(new MembroDAO().listarMembros());
        return lista;
    }
}
