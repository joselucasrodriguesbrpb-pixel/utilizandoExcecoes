import domain.banco.BancoService;
import domain.usuario.User;
import repositorio.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TesteDoBanco {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        UserRepository repository = new UserRepository();
        BancoService banco = new BancoService(repository);
        Scanner scanner = new Scanner(System.in);

        try {
            // Contas pré-cadastradas para testes e transferências
            User u1 = banco.cadastrar("1001", "1234", LocalDate.parse("15/05/2000", FORMATTER));
            User u2 = banco.cadastrar("1002", "4321", LocalDate.parse("20/08/2001", FORMATTER));

            System.out.println("=== Bem-vindo ao Sistema Bancário ===");
            System.out.println("1 - Fazer Login");
            System.out.println("2 - Fazer Cadastro");
            System.out.print("Escolha uma opção: ");
            int opcaoInicial = Integer.parseInt(scanner.nextLine());

            User usuarioLogado;
            if (opcaoInicial == 2) {
                usuarioLogado = realizarCadastro(scanner, banco);
            } else if (opcaoInicial == 1) {
                usuarioLogado = realizarLogin(scanner, banco, u1, u2);
            } else {
                System.out.println("Opção inválida. Encerrando...");
                return;
            }

            String id = usuarioLogado.getIdConta();
            int opcao;
            do {
                System.out.println("\n=== Menu Principal (Conta: " + id + ") ===");
                System.out.println("1 - Visualizar Saldo");
                System.out.println("2 - Depositar");
                System.out.println("3 - Sacar");
                System.out.println("4 - Transferir para conta 1002");
                System.out.println("5 - Consultar Extrato");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.printf("Saldo atual: R$ %.2f%n", repository.visualizarSaldo(id));
                        break;
                    case 2:
                        System.out.print("Valor do depósito: ");
                        double dep = Double.parseDouble(scanner.nextLine());
                        repository.depositar(dep);
                        System.out.println("Depósito realizado!");
                        break;
                    case 3:
                        System.out.print("Valor do saque: ");
                        double saq = Double.parseDouble(scanner.nextLine());
                        repository.sacar(saq);
                        System.out.println("Saque realizado!");
                        break;
                    case 4:
                        System.out.print("Valor da transferência: ");
                        double transf = Double.parseDouble(scanner.nextLine());
                        banco.realizarTransferencia(usuarioLogado, u2, transf);
                        System.out.println("Transferência concluída!");
                        break;
                    case 5:
                        System.out.println(banco.consultarExtrato(id));
                        break;
                    case 0:
                        System.out.println("A encerrar...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } while (opcao != 0);

        }  catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static User realizarCadastro(Scanner scanner, BancoService banco) throws Exception {
        System.out.println("\n--- Novo Cadastro ---");
        System.out.print("Digite o ID da nova conta: ");
        String novoId = scanner.nextLine();
        System.out.print("Crie uma senha: ");
        String novaSenha = scanner.nextLine();
        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataTexto = scanner.nextLine();

        LocalDate dataNascimento = LocalDate.parse(dataTexto, FORMATTER);
        User novoUsuario = banco.cadastrar(novoId, novaSenha, dataNascimento);
        banco.login(novoId, novaSenha);

        System.out.println("Cadastro realizado com sucesso! Bem-vindo, conta " + novoId + ".");
        return novoUsuario;
    }

    private static User realizarLogin(Scanner scanner, BancoService banco, User u1, User u2) throws Exception {
        System.out.println("\n--- Login ---");
        System.out.print("ID da Conta: ");
        String id = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        banco.login(id, senha);
        System.out.println("Login efetuado com sucesso!");

        if (id.equals(u2.getIdConta())) {
            return u2;
        }
        return u1;
    }
}