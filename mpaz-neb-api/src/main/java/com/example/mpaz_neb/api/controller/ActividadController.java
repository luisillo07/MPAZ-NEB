package com.example.mpaz_neb.api.controller;

import com.example.mpaz_neb.api.dto.ActividadRequest;
import com.example.mpaz_neb.api.dto.ActividadResponse;
import com.example.mpaz_neb.api.dto.ApiErrorResponse;
import com.example.mpaz_neb.api.service.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Crear una actividad")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Actividad creada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El código ya existe",
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
    @PostMapping
    public ResponseEntity<ActividadResponse> crearActividad(@Valid @RequestBody ActividadRequest actividadRequest) {
        ActividadResponse actividad = actividadService.crearActividad(actividadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(actividad);
    }

    @Operation(summary = "Editar una actividad por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividad editada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
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
                    responseCode = "409",
                    description = "El código ya existe",
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
    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponse> actualizarActividad(@PathVariable Integer id,
                                                                 @Valid @RequestBody ActividadRequest actividadRequest) {
        ActividadResponse actividad = actividadService.actualizarActividad(id, actividadRequest);
        return ResponseEntity.ok(actividad);
    }

    @Operation(summary = "Borrar una actividad")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Actividad borrada",
                    content = @Content(mediaType = "application/json")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Integer id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }
}