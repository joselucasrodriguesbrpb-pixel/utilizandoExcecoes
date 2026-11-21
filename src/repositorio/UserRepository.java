package repositorio;

import banco_de_dados_temporario.ArmazenamentoDeUsuario;
import domain.usuario.SaldoUsuario;
import exception.SaldoInsuficienteException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository implements ArmazenamentoDeUsuario {

    private List<SaldoUsuario> extrato = new ArrayList<>();
    private List<SaldoUsuario> saldoUsuario = new ArrayList<>();
    private String contaAtiva;

    public UserRepository() {}

    public UserRepository(List<SaldoUsuario> extrato, List<SaldoUsuario> saldoUsuario) {
        this.extrato = extrato;
        this.saldoUsuario = saldoUsuario;
    }

    public void setContaAtiva(String idConta) {
        this.contaAtiva = idConta;
    }

    @Override
    public double visualizarSaldo(String idConta) {
        double saldo = 0;
        for (SaldoUsuario item : saldoUsuario) {
            if (item.getIdConta().equals(idConta)) {
                saldo += item.getValorSaldo();
            }
        }
        return saldo;
    }

    @Override
    public double sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de levantamento deve ser superior a zero.");
        }
        double saldoAtual = visualizarSaldo(contaAtiva);
        if (saldoAtual < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a operação.");
        }

        String idTx = UUID.randomUUID().toString().substring(0, 8);
        SaldoUsuario movimentacao = new SaldoUsuario(idTx, contaAtiva, "SAQUE", -valor);
        saldoUsuario.add(movimentacao);
        extrato.add(movimentacao);

        return visualizarSaldo(contaAtiva);
    }

    @Override
    public double depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de depósito deve ser superior a zero.");
        }

        String idTx = UUID.randomUUID().toString().substring(0, 8);
        SaldoUsuario movimentacao = new SaldoUsuario(idTx, contaAtiva, "DEPOSITO", valor);
        saldoUsuario.add(movimentacao);
        extrato.add(movimentacao);

        return visualizarSaldo(contaAtiva);
    }

    public List<SaldoUsuario> getExtrato() {
        return extrato;
    }

    public List<SaldoUsuario> getSaldoUsuario() {
        return saldoUsuario;
    }
}