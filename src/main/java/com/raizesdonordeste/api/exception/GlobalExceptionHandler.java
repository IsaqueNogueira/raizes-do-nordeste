package com.raizesdonordeste.api.exception;

import com.raizesdonordeste.application.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.raizesdonordeste.api.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErroResponse.DetalheErro> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new ErroResponse.DetalheErro(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());

        ErroResponse erro = new ErroResponse(
                "VALIDACAO_INVALIDA",
                "Erro de validação nos campos enviados",
                detalhes,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleMessageNotReadable(
        org.springframework.http.converter.HttpMessageNotReadableException ex,
        HttpServletRequest request) {

        ErroResponse erro = new ErroResponse(
            "CAMPO_INVALIDO",
            "Valor inválido para um ou mais campos. Verifique os valores aceitos.",
            List.of(),
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
   }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request) {

        ErroResponse erro = new ErroResponse(
                "ERRO_NEGOCIO",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ErroResponse erro = new ErroResponse(
                "ERRO_INTERNO",
                "Erro interno do servidor",
                List.of(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}