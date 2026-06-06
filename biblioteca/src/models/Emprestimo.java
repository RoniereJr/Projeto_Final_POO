package models;

import java.util.Date;

public class Emprestimo {
    // ATRIBUTOS
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private boolean devolvido;

    // CONSTRUTOR
    public Emprestimo(Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido) {
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.devolvido = false;
    }

    // METODOS
    public static Emprestimo criaEmprestimo(Livro livro, Membro membro){
        return criaEmprestimo(livro, membro);
    }

   public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

   public static void editarEmprestimo(Emprestimo emprestimo, Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido) {
       emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
       emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
       emprestimo.setDataEmprestimo(dataEmprestimo);
       emprestimo.setDevolvido(devolvido);
   }

    public void excluirEmprestimo(Emprestimo emprestimo){
        //banco de dados
    }

    public void registrarDevolucao(){
        this.devolvido = true;
    }

    public int calcularMulta(){
        return (int) (dataDevolucaoReal.getTime() - dataDevolucaoPrevista.getTime());
    }
}
