package com.example.mpaz_neb.api.controller;

import com.example.mpaz_neb.api.dto.ActividadResponse;
import com.example.mpaz_neb.api.dto.ApiErrorResponse;
import com.example.mpaz_neb.api.service.ActividadService;
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
@RequestMapping("/api/actividad")
@RequiredArgsConstructor
@Tag(name = "Actividades", description = "Operaciones con las actividades de Lectura y Matemática")
public class ActividadController {
    private final ActividadService actividadService;

    @Operation(summary = "Listar actividades")
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
    public ResponseEntity<List<ActividadResponse>> obtenerActividades(
            @RequestParam(required = false) String search) {
        List<ActividadResponse> actividades = actividadService.obtenerActividades(search);
        return ResponseEntity.ok(actividades);
    }

    @Operation(summary = "Buscar una actividad por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividad encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
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
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> obtenerActividad(@PathVariable Integer id) {
        ActividadResponse actividad = actividadService.obtenerActividad(id);
        return ResponseEntity.ok(actividad);
    }
}