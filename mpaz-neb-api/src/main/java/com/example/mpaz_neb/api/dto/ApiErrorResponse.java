package com.example.mpaz_neb.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    @Schema(description = "Fecha y hora del error", example = "2026-11-21T20:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP (404, 409, 400 o 500)")
    private Integer status;

    @Schema(description = "Nombre del estado")
    private String error;

    @Schema(description = "Mensaje del error")
    private String message;

    @Schema(description = "Ruta pedida")
    private String path;

    @Schema(description = "Campos con errores de validación")
    private List<String> errors;
}