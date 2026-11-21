package domain.usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class User {
    private String idConta;
    private String senha;
    private LocalDate dataDeNascimento;
    private double saldo;

    public User(String idConta, String senha, LocalDate dataDeNascimento) {
        this.idConta = idConta;
        this.senha = senha;
        this.dataDeNascimento = dataDeNascimento;
        this.saldo = 0.0;
    }

    public String getIdConta() {
        return idConta;
    }

    public String getSenha() {
        return senha;
    }

    public String getDataDeNascimento() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataDeNascimento.format(formatter);
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}