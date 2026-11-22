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
public class EstudianteResponse {

    @Schema(description = "ID de la base de datos")
    private Integer id;

    @Schema(description = "Código del estudiante", example = "EST-001")
    private String codigo;

    @Schema(description = "Nombre (ficticio)", example = "Estudiante ficticio 1")
    private String nombre;

    @Schema(description = "Cuenta con la que entra a la app", example = "estudiante1")
    private String usuario;
}