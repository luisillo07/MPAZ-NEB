package com.example.mpaz_neb.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreguntaResponse {

    @Schema(description = "ID de la base de datos")
    private Integer id;

    @Schema(description = "Código de la actividad a la que pertenece", example = "LEC-000")
    private String actividadCodigo;

    @Schema(description = "Tipo de pregunta")
    private String tipo;

    @Schema(description = "Enunciado")
    private String enunciado;

    @Schema(description = "Alternativa A")
    private String alternativaA;

    @Schema(description = "Alternativa B")
    private String alternativaB;

    @Schema(description = "Alternativa C")
    private String alternativaC;

    @Schema(description = "Alternativa correcta: 0 = A, 1 = B, 2 = C")
    private Integer correcta;

    @Schema(description = "Datos numéricos (sólo Matemática)")
    private String datosNumericos;

    @Schema(description = "Operación (sólo Matemática)")
    private String operacion;

    @Schema(description = "Procedimiento (sólo Matemática)")
    private String procedimiento;

    @Schema(description = "Retroalimentación que ve el estudiante")
    private String retroalimentacion;
}