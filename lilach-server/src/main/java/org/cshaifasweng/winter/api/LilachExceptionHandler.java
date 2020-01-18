package org.cshaifasweng.winter.api;

import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class LilachExceptionHandler {
    @ExceptionHandler(LogicalException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public static ResponseEntity<ServerException> handleLogicalException(LogicalException exception) {
        return new ResponseEntity<>(
                new ServerException(exception.getLocalizedMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
