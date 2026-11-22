package com.example.mpaz_neb.api.controller;

import com.example.mpaz_neb.api.dto.ApiErrorResponse;
import com.example.mpaz_neb.api.dto.EstudianteResponse;
import com.example.mpaz_neb.api.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiante")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Estudiantes ficticios del curso")
public class EstudianteController {
    private final EstudianteService estudianteService;
    @Operation(summary = "Listar estudiantes")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista entregada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> obtenerEstudiantes() {
        List<EstudianteResponse> estudiantes = estudianteService.obtenerEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
}