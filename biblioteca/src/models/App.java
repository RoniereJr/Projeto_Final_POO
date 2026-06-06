package models;

import java.util.Scanner;

public class App {
    static final Scanner entrada = new Scanner(System.in);
    static char opcao = ' ';
    
    static final String sucesso = ">> Operacao realizada com sucesso.\n";
    static final String falha = ">> Algo deu errado. \nErro: ";
    static final String invalida = ">> Opcao invalida. Tente novamente";

    public static void main(String[] args) {
        try {
            // conexao com banco de dados
            while(opcao != '5'){
                System.out.println("\n[1] Cadastrar");
                System.out.println("[2] Editar");
                System.out.println("[3] Excluir");
                System.out.println("[4] Exibir");
                System.out.println("[5] Sair");
                System.out.print(">> Informe a opcao: ");
                opcao = entrada.next().charAt(0);

                switch (opcao) {
                    case '1':
                        cadastrar();
                        // banco de dados
                        break;

                    case '2':
                        // banco de dados
                        System.out.println(sucesso);
                        break;

                    case '3':
                        // banco de dados
                        System.out.println(sucesso);
                        break;

                    case '4':
                        // banco de dados
                        System.out.println(sucesso);
            
                    case '5':
                        System.out.println(sucesso);
                        break;
                    default:
                        System.out.println(invalida);
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println(falha + e);
        } finally{
            System.out.println(sucesso);
            entrada.close();
        }
    }

    public static void cadastrar(){
        System.out.println("\n[1] Bibliotecario");
        System.out.println("[2] Membro");
        System.out.print(">> Quem voce deseja cadastrar? ");
        opcao = entrada.next().charAt(0);

        if(opcao == '1' || opcao == '2'){
            entrada.nextLine();
            System.out.println("Informe o nome: ");
            String nome = entrada.nextLine();
            System.out.println("Informe o cpf: ");
            String cpf = entrada.nextLine();
            System.out.println("Informe o login: ");
            String login = entrada.nextLine();
            System.out.println("Informe a senha: ");
            String senha = entrada.nextLine();

            if(opcao == '1'){
                for (Cargo c : Cargo.values()) {
                    System.out.println("-> " + c);
                }
                System.out.println("Selecione o cargo: ");
                Cargo cargo = Cargo.valueOf(entrada.nextLine().toUpperCase());
                Bibliotecario bibliotecario = new Bibliotecario(nome, cpf, login, senha, false, cargo);
            } else{
                System.out.println("Informe o endereco: ");
                String endereco = entrada.nextLine();
                System.out.println("Informe o telefone: ");
                String telefone = entrada.nextLine();
                System.out.println("Informe o email: ");
                String email = entrada.nextLine();

                Membro membro = new Membro(nome, cpf, login, senha, false, endereco, telefone, email);
            }

            System.out.println(sucesso);
        } else{
            System.out.println(invalida);
        }
    }
}
