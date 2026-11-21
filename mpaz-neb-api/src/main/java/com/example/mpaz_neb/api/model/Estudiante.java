package com.example.mpaz_neb.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Estudiante {
    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String usuario;
}