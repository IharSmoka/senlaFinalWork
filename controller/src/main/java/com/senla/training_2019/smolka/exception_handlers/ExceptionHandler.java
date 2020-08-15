package com.senla.training_2019.smolka.exception_handlers;

import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.model.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.apache.log4j.Logger;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    static final Logger logger = Logger.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({InternalServiceException.class, IOException.class})
    public ResponseEntity<ExceptionDto> handleException(Exception exc) {
        logger.error(exc.getMessage());
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalArgumentException exc) {
        logger.error(exc.getMessage());
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(EntityNotFoundException exc) {
        logger.error(exc.getMessage());
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.NOT_FOUND);
    }
}
