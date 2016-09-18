package com.github.allin.advices;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 */
@Log4j
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String missingParam(
            Exception e
    ) {
        log.error("missing param:", e);
        return "errors/MissingRequestParam";
    }
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String wrongRequest(
            Exception e
    ) {
        log.error("wrong request: ", e);
        return "errors/WrongRequest";
    }
}
