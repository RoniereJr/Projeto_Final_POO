package models;

import dao.EmprestimoDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Emprestimo {

    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

    public Emprestimo(Livro livro, Membro membro,
            LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
            LocalDate dataDevolucaoReal, boolean devolvido) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.devolvido = devolvido;
    }

    public Emprestimo(Livro livro, Membro membro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this(livro, membro, dataEmprestimo, dataDevolucaoPrevista, null, false);
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public void verEmprestimo() {
        System.out.println("Emprestimo | Livro: " + livro.getTitulo() +
                " | Membro: " + membro.getNome() +
                " | Data: " + dataEmprestimo +
                " | Prevista: " + dataDevolucaoPrevista +
                " | Real: " + dataDevolucaoReal +
                " | Devolvido: " + devolvido);
    }

    public void registrarDevolucao() {
        if (!devolvido) {
            this.dataDevolucaoReal = LocalDate.now();
            this.devolvido = true;
        }
    }

    public int calcularMulta() {
        if (!devolvido) {
            long dias = LocalDate.now().toEpochDay() - dataDevolucaoPrevista.toEpochDay();
            return dias > 0 ? (int) dias : 0;
        } else if (dataDevolucaoReal != null) {
            long dias = dataDevolucaoReal.toEpochDay() - dataDevolucaoPrevista.toEpochDay();
            return dias > 0 ? (int) dias : 0;
        }
        return 0;
    }

    public static Emprestimo criarEmprestimo(Livro livro, Membro membro) {
        try {
            return new EmprestimoDAO().criarEmprestimo(livro, membro);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void editarEmprestimo(Emprestimo emprestimo, LocalDate dataEmprestimo,
            LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal,
            boolean devolvido) {
        try {
            new EmprestimoDAO().editarEmprestimo(
                    emprestimo.getLivro().getIsbn(),
                    emprestimo.getMembro().getCpf(),
                    emprestimo.getDataEmprestimo(),
                    dataEmprestimo, dataDevolucaoPrevista, dataDevolucaoReal, devolvido);
            emprestimo.setDataEmprestimo(dataEmprestimo);
            emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
            emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
            emprestimo.setDevolvido(devolvido);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirEmprestimo(Emprestimo emprestimo) {
        try {
            new EmprestimoDAO().excluirEmprestimo(
                    emprestimo.getLivro().getIsbn(),
                    emprestimo.getMembro().getCpf(),
                    emprestimo.getDataEmprestimo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Emprestimo> listarEmprestimos() {
        try {
            return new EmprestimoDAO().listarEmprestimos();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Emprestimo> listarEmprestimosAtrasados() {
        try {
            return new EmprestimoDAO().listarEmprestimosAtrasados();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        try {
            return new EmprestimoDAO().listarEmprestimosAtrasadosPorMembro(membro);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Emprestimo> listarEmprestimosAtivos() {
        try {
            return new EmprestimoDAO().listarEmprestimosAtivos();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        try {
            return new EmprestimoDAO().listarEmprestimosAtivosPorMembro(membro);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
