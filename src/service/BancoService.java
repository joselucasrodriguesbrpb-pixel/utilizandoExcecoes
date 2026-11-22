package service;

import domain.banco.ServicoFinanceiro;
import domain.usuario.User;
import domain.usuario.UserSaldo;
import exception.*;
import repositorio.UserRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class BancoService implements ServicoFinanceiro {

    private Map<String, User> contas = new HashMap<>();
    private UserRepository repository;

    public BancoService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User cadastrar(String idConta, String senha, LocalDate dataDeNascimento) throws DataInvalidaException {
        if (dataDeNascimento == null || dataDeNascimento.isAfter(LocalDate.now())) {
            throw new DataInvalidaException("Data de nascimento inválida.");
        }
        if (contas.containsKey(idConta)) {
            throw new IllegalArgumentException("Já existe uma conta com este ID.");
        }
        User novoUsuario = new User(idConta, senha, dataDeNascimento);
        contas.put(idConta, novoUsuario);
        return novoUsuario;
    }

    @Override
    public void login(String idConta, String senha) throws SenhaOuIdErradoException {
        User user = contas.get(idConta);
        if (user == null || !user.getSenha().equals(senha)) {
            throw new SenhaOuIdErradoException("ID da conta ou palavra-passe incorretos.");
        }
        repository.setContaAtiva(idConta);
    }

    @Override
    public void realizarTransferencia(User remetente, User destinatario, double valor)
            throws ContaNaoEncontradaException, SaldoInsuficienteException {

        if (remetente == null || !contas.containsKey(remetente.getIdConta())) {
            throw new ContaNaoEncontradaException("Conta de origem não encontrada.");
        }
        if (destinatario == null || !contas.containsKey(destinatario.getIdConta())) {
            throw new ContaNaoEncontradaException("Conta de destino não encontrada.");
        }

        double saldoRemetente = repository.visualizarSaldo(remetente.getIdConta());
        if (saldoRemetente < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferência.");
        }

        String idTx = UUID.randomUUID().toString().substring(0, 8);
        UserSaldo debito = new UserSaldo(idTx, remetente.getIdConta(), "TRANSFERENCIA_ENVIADA", -valor);
        UserSaldo credito = new UserSaldo(idTx, destinatario.getIdConta(), "TRANSFERENCIA_RECEBIDA", valor);

        repository.getUserSaldo().add(debito);
        repository.getExtrato().add(debito);

        repository.getUserSaldo().add(credito);
        repository.getExtrato().add(credito);
    }

    @Override
    public String consultarExtrato(String idConta) throws ContaNaoEncontradaException {
        if (!contas.containsKey(idConta)) {
            throw new ContaNaoEncontradaException("Conta não encontrada.");
        }

        StringBuilder sb = new StringBuilder("=== Extrato da Conta: " + idConta + " ===\n");
        for (UserSaldo mov : repository.getExtrato()) {
            if (mov.getIdConta().equals(idConta)) {
                sb.append(mov.toString()).append("\n");
            }
        }
        sb.append(String.format("Saldo Atual: R$ %.2f\n", repository.visualizarSaldo(idConta)));
        return sb.toString();
    }

    @Override
    public void estornarTransacao(String idTransacao) throws PrazoEstornoExpiradoException {
        List<UserSaldo> movimentacoesAlvo = new ArrayList<>();

        for (UserSaldo mov : repository.getExtrato()) {
            if (mov.getIdTransacao().equals(idTransacao)) {
                long horasPassadas = Duration.between(mov.getDataHora(), LocalDateTime.now()).toHours();
                if (horasPassadas > 24) {
                    throw new PrazoEstornoExpiradoException("O prazo de 24h para estorno já expirou.");
                }
                movimentacoesAlvo.add(mov);
            }
        }

        for (UserSaldo mov : movimentacoesAlvo) {
            UserSaldo estorno = new UserSaldo(
                    UUID.randomUUID().toString().substring(0, 8),
                    mov.getIdConta(),
                    "ESTORNO_" + mov.getTipo(),
                    -mov.getValorSaldo()
            );
            repository.getUserSaldo().add(estorno);
            repository.getExtrato().add(estorno);
        }
    }
}