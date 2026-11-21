package com.example.mpaz_neb.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class EstudianteResponse {

    @Schema(description = "Código del estudiante", example = "EST-001")
    private String codigo;

    @Schema(description = "Nombre (ficticio)", example = "Estudiante ficticio 1")
    private String nombre;

    @Schema(description = "Cuenta con la que entra a la app", example = "estudiante1")
    private String usuario;
}