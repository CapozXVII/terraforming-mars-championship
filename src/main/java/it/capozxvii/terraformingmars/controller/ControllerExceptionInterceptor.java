package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.ResponseWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionInterceptor {

    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error, please check logs for more details";

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionInterceptor.class);

    @ExceptionHandler(TerraformingMarsException.class)
    public ResponseEntity<ResponseWrapper> handleTerraformingMarsException(
            final TerraformingMarsException exception) {
        LOG.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(SimpleWrapper.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper> handleException(final Exception exception) {
        LOG.error(UNKNOWN_ERROR_MESSAGE, exception);
        return ResponseEntity.internalServerError()
                .body(SimpleWrapper.builder().message(UNKNOWN_ERROR_MESSAGE).build());
    }
}
