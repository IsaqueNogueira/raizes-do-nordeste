package com.raizesdonordeste.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroResponse {

    private String error;
    private String message;
    private List<DetalheErro> details;
    private LocalDateTime timestamp;
    private String path;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetalheErro {
        private String field;
        private String issue;
    }
}