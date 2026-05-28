package ProjetoDePoo;

import java.util.Date;
import java.util.List;

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
        this.devolvido = devolvido;
    }

    // METODOS
//    public editarEmprestimo(Emprestimo emprestimo, Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido) {
//        this.dataEmprestimo = dataEmprestimo;
//        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
//        this.dataDevolucaoReal = dataDevolucaoReal;
//        this.devolvido = devolvido;
//    }

    public void excluirEmprestimo(Emprestimo emprestimo){
        // remover
    }

    public void registrarDevolucao(){
        this.devolvido = true;
    }

//    public float calcularMulta(){
//        return (dataDevolucaoReal - dataDevolucaoPrevista);
//    }

//    public List<Emprestimo> listarEmprestimos(){
//
//    }

//    public List<Emprestimo> listarEmprestimosAtrasados(){
//
//    }

//    public List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro){
//
//    }

//    public List<Emprestimo> listarEmprestimosAtivos(){
//
//    }

//    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro){
//
//    }
}
