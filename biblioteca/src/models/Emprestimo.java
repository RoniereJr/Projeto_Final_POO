package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private boolean devolvido;

    public Emprestimo(Livro livro, Membro membro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal, boolean devolvido) {
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusWeeks(1);
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.devolvido = false;
    }

    public static Emprestimo criaEmprestimo(Livro livro, Membro membro){
        return criaEmprestimo(livro, membro);
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

   public static void editarEmprestimo(Emprestimo emprestimo, Livro livro, Membro membro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal, boolean devolvido) {
       emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
       emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
       emprestimo.setDataEmprestimo(dataEmprestimo);
       emprestimo.setDevolvido(devolvido);
   }

    public void excluirEmprestimo(Emprestimo emprestimo){
    }

    public void registrarDevolucao() {
        this.dataDevolucaoReal = LocalDate.now();
        this.devolvido = true;
    }

    public long calcularMulta() {
    if (dataDevolucaoReal != null && dataDevolucaoReal.isAfter(dataDevolucaoPrevista)) {
        // Retorna a diferença em dias
        return ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataDevolucaoReal);
    }
    return 0;
}
}