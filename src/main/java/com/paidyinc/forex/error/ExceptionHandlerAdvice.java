package com.paidyinc.forex.error;

import com.paidyinc.forex.CurrencyPairException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(CurrencyPairException.class)
    ResponseEntity<String> handleCurrencyPairException(CurrencyPairException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleCatchAllException(Exception exception) {
        LOG.error(exception.getMessage());
        if (LOG.isTraceEnabled()) {
            exception.printStackTrace();
        }

        return ResponseEntity.internalServerError().body("An unknown error occurred, please contact the support team");
    }
}
