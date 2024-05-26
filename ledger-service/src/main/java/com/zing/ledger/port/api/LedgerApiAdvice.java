package com.zing.ledger.port.api;

import com.zing.ledger.adapter.command.LedgerWriteRepositoryException;
import com.zing.ledger.adapter.command.TransactionAlreadyExistsException;
import com.zing.ledger.adapter.query.LedgerQueryRepositoryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class LedgerApiAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({LedgerWriteRepositoryException.class, LedgerQueryRepositoryException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalError(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(
                ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(TransactionAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(
                ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
