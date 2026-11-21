package domain.banco;

import domain.usuario.User;
import exception.*;

import java.time.LocalDate;
import java.util.Date;

public interface ServicoFinanceiro {

    User cadastrar(String idConta, String senha, LocalDate dataDeNascimento) throws DataInvalidaException;

    void login(String idConta, String senha) throws SenhaOuIdErradoException;

    void realizarTransferencia(User remetente, User destinatario, double valor) throws ContaNaoEncontradaException, SaldoInsuficienteException;

    String consultarExtrato(String idConta) throws ContaNaoEncontradaException;

    void estornarTransacao(String idTransacao) throws PrazoEstornoExpiradoException;
}
