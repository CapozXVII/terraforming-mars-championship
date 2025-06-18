package it.capozxvii.terraformingmars.util.exception;

import it.capozxvii.terraformingmars.util.Message;

public class TerraformingMarsException extends RuntimeException {
    public TerraformingMarsException(final String message, final Object... arguments) {
        super(Message.formatMessage(message, arguments));
    }
}
