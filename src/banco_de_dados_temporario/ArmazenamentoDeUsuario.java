package banco_de_dados_temporario;

public interface ArmazenamentoDeUsuario {
    double visualizarSaldo(String idConta);

    double sacar(double valor);

    double depositar(double valor);
}
