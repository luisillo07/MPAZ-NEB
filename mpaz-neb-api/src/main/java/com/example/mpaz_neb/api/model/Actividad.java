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
@Table(name = "actividades")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 8)
    private String codigo;

    @Column(length = 150)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String modulo;

    @Column(length = 30)
    private String curso;

    @Column(length = 100)
    private String contenido;

    @Column(length = 500)
    private String objetivo;

    @Column(length = 20)
    private String dificultad;

    @Column(length = 1000)
    private String instrucciones;

    @Column(name = "cantidad_preguntas")
    private Integer cantidadPreguntas;

    @Column(name = "intentos_permitidos")
    private Integer intentosPermitidos;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "titulo_texto", length = 150)
    private String tituloTexto;

    @Column(name = "tipo_texto", length = 30)
    private String tipoTexto;

    @Column(name = "contenido_texto", length = 2000)
    private String contenidoTexto;

    @Column(name = "nivel_vocabulario", length = 30)
    private String nivelVocabulario;

    @Column(nullable = false, length = 50)
    private String docente;
}