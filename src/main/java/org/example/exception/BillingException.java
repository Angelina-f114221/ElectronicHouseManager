package org.example.exception;
// грешки при изчисление на разход
//
public class BillingException extends RuntimeException {
    // показва само съобщение
    public BillingException(String message) {
        super(message);
    }
    // показва съобщение и причина за грешката
    public BillingException(String message, Throwable cause) {
        super(message, cause);
    }
}
