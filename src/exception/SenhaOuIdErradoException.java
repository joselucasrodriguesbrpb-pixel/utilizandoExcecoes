package exception;

public class SenhaOuIdErradoException extends IllegalArgumentException {
    public SenhaOuIdErradoException(String message) {
        super(message);
    }
}
