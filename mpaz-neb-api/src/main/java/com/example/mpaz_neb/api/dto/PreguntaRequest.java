package com.example.mpaz_neb.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PreguntaRequest {

    @Schema(description = "Código de la actividad a la que pertenece", example = "LEC-000")
    @NotBlank
    @Length(min = 7, max = 8)
    private String actividadCodigo;

    @Schema(description = "Tipo de pregunta")
    @Length(max = 30)
    private String tipo;

    @Schema(description = "Enunciado")
    @NotBlank
    @Length(max = 500)
    private String enunciado;

    @Schema(description = "Alternativa A")
    @NotBlank
    @Length(max = 200)
    private String alternativaA;

    @Schema(description = "Alternativa B")
    @NotBlank
    @Length(max = 200)
    private String alternativaB;

    @Schema(description = "Alternativa C")
    @NotBlank
    @Length(max = 200)
    private String alternativaC;

    @Schema(description = "Alternativa correcta: 0 = A, 1 = B, 2 = C")
    @NotNull
    @Min(0)
    @Max(2)
    private Integer correcta;

    @Schema(description = "Datos numéricos (sólo Matemática)")
    @Length(max = 100)
    private String datosNumericos;

    @Schema(description = "Operación (sólo Matemática)")
    @Length(max = 50)
    private String operacion;

    @Schema(description = "Procedimiento (sólo Matemática)")
    @Length(max = 500)
    private String procedimiento;

    @Schema(description = "Retroalimentación que ve el estudiante")
    @Length(max = 500)
    private String retroalimentacion;
}