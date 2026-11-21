package com.example.mpaz_neb.api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mpaz_neb.api.dto.EstudianteResponse;
import com.example.mpaz_neb.api.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Estudiantes ficticios del curso")
public class EstudianteController {
    private final EstudianteService estudianteService;

    @Operation(summary = "Listar estudiantes")
    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> obtenerEstudiantes() {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantes());
    }
}