package exception;

public class ContaNaoEncontradaException extends ClassNotFoundException {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }
}
