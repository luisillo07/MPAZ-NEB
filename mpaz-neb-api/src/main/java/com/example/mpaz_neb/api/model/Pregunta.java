package com.example.mpaz_neb.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "preguntas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "actividad_codigo", nullable = false, length = 8)
    private String actividadCodigo;

    @Column(length = 30)
    private String tipo;

    @Column(nullable = false, length = 500)
    private String enunciado;

    @Column(name = "alternativa_a", nullable = false, length = 200)
    private String alternativaA;

    @Column(name = "alternativa_b", nullable = false, length = 200)
    private String alternativaB;

    @Column(name = "alternativa_c", nullable = false, length = 200)
    private String alternativaC;

    @Column(nullable = false)
    private Integer correcta;

    @Column(name = "datos_numericos", length = 100)
    private String datosNumericos;

    @Column(length = 50)
    private String operacion;

    @Column(length = 500)
    private String procedimiento;

    @Column(length = 500)
    private String retroalimentacion;
}