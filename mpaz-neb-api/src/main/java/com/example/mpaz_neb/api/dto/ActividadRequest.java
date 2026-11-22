package com.example.mpaz_neb.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ActividadRequest {

    @Schema(description = "Código: LEC-### (Lectura) o MAT-### (Matemática)", example = "LEC-000")
    @NotBlank
    @Length(min = 7, max = 8)
    private String codigo;

    @Schema(description = "Nombre de la actividad")
    @Length(max = 150)
    private String nombre;

    @Schema(description = "Lectura o Matemática")
    @NotBlank
    @Length(min = 3, max = 20)
    private String modulo;

    @Schema(description = "Curso de 1.º a 4.º básico", example = "2.º básico")
    @Length(max = 30)
    private String curso;

    @Schema(description = "Descripción de contenido")
    @Length(max = 100)
    private String contenido;

    @Schema(description = "Objetivo pedagógico")
    @Length(max = 500)
    private String objetivo;

    @Schema(description = "Fácil, Medio o Difícil")
    @Length(max = 20)
    private String dificultad;

    @Schema(description = "Instrucciones para el estudiante")
    @Length(max = 1000)
    private String instrucciones;

    @Schema(description = "Cantidad de preguntas")
    private Integer cantidadPreguntas;

    @Schema(description = "Intentos permitidos")
    private Integer intentosPermitidos;

    @Schema(description = "borrador, disponible o desactivada")
    @NotBlank
    @Length(min = 3, max = 20)
    private String estado;

    @Schema(description = "Título del texto (sólo Lectura)")
    @Length(max = 150)
    private String tituloTexto;

    @Schema(description = "Tipo de texto (sólo Lectura)")
    @Length(max = 30)
    private String tipoTexto;

    @Schema(description = "Texto que lee el estudiante (sólo Lectura)")
    @Length(max = 2000)
    private String contenidoTexto;

    @Schema(description = "Nivel de vocabulario (sólo Lectura)")
    @Length(max = 30)
    private String nivelVocabulario;

    @Schema(description = "Usuario del docente que la editó por última vez")
    @NotBlank
    @Length(min = 3, max = 50)
    private String docente;
}