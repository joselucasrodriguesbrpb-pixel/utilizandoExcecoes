package domain.usuario;

import java.time.LocalDateTime;

public class UserSaldo {
    private String idTransacao;
    private String idConta;
    private String tipo; // Ex: "DEPOSITO", "SAQUE", "TRANSFERENCIA"
    private double valorSaldo;
    private LocalDateTime dataHora;

    public UserSaldo(String idTransacao, String idConta, String tipo, double valorSaldo) {
        this.idTransacao = idTransacao;
        this.idConta = idConta;
        this.tipo = tipo;
        this.valorSaldo = valorSaldo;
        this.dataHora = LocalDateTime.now();
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public String getIdConta() {
        return idConta;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValorSaldo() {
        return valorSaldo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Conta: %s | Valor: R$ %.2f | Data: %s",
                idTransacao, tipo, idConta, valorSaldo, dataHora);
    }
}