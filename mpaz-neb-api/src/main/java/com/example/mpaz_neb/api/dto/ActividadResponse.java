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
public class ActividadResponse {

    @Schema(description = "ID de la base de datos")
    private Integer id;

    @Schema(description = "Código: LEC-### (Lectura) o MAT-### (Matemática)", example = "LEC-000")
    private String codigo;

    @Schema(description = "Nombre de la actividad")
    private String nombre;

    @Schema(description = "Lectura o Matemática")
    private String modulo;

    @Schema(description = "Curso de 1.º a 4.º básico", example = "2.º básico")
    private String curso;

    @Schema(description = "Descripción de contenido")
    private String contenido;

    @Schema(description = "Objetivo pedagógico")
    private String objetivo;

    @Schema(description = "Fácil, Medio o Difícil")
    private String dificultad;

    @Schema(description = "Instrucciones para el estudiante")
    private String instrucciones;

    @Schema(description = "Cantidad de preguntas")
    private Integer cantidadPreguntas;

    @Schema(description = "Intentos permitidos")
    private Integer intentosPermitidos;

    @Schema(description = "borrador, disponible o desactivada")
    private String estado;

    @Schema(description = "Título del texto (sólo Lectura)")
    private String tituloTexto;

    @Schema(description = "Tipo de texto (sólo Lectura)")
    private String tipoTexto;

    @Schema(description = "Texto que lee el estudiante (sólo Lectura)")
    private String contenidoTexto;

    @Schema(description = "Nivel de vocabulario (sólo Lectura)")
    private String nivelVocabulario;

    @Schema(description = "Usuario del docente que la editó por última vez")
    private String docente;
}