package exception;

import java.time.DateTimeException;

public class PrazoEstornoExpiradoException extends DateTimeException {
    public PrazoEstornoExpiradoException(String message) {
        super(message);
    }
}
