package com.example.mpaz_neb.api.controller;

import com.example.mpaz_neb.api.dto.ApiErrorResponse;
import com.example.mpaz_neb.api.dto.PreguntaRequest;
import com.example.mpaz_neb.api.dto.PreguntaResponse;
import com.example.mpaz_neb.api.service.PreguntaService;
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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/pregunta")
@RequiredArgsConstructor
@Tag(name = "Preguntas", description = "Operaciones con las preguntas de cada actividad")
public class PreguntaController {
    private final PreguntaService preguntaService;

    @Operation(summary = "Listar preguntas")
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
    public ResponseEntity<List<PreguntaResponse>> obtenerPreguntas(
            @RequestParam(required = false) String actividad) {
        List<PreguntaResponse> preguntas = preguntaService.obtenerPreguntas(actividad);
        return ResponseEntity.ok(preguntas);
    }

    @Operation(summary = "Buscar una pregunta por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pregunta encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreguntaResponse.class)
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
    public ResponseEntity<PreguntaResponse> obtenerPregunta(@PathVariable Integer id) {
        PreguntaResponse pregunta = preguntaService.obtenerPregunta(id);
        return ResponseEntity.ok(pregunta);
    }

    @Operation(summary = "Crear una pregunta")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pregunta creada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreguntaResponse.class)
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
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<PreguntaResponse> crearPregunta(@Valid @RequestBody PreguntaRequest preguntaRequest) {
        PreguntaResponse pregunta = preguntaService.crearPregunta(preguntaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pregunta);
    }

    @Operation(summary = "Editar una pregunta por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pregunta editada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreguntaResponse.class)
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
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PreguntaResponse> actualizarPregunta(@PathVariable Integer id,
                                                               @Valid @RequestBody PreguntaRequest preguntaRequest) {
        PreguntaResponse pregunta = preguntaService.actualizarPregunta(id, preguntaRequest);
        return ResponseEntity.ok(pregunta);
    }

    @Operation(summary = "Borrar una pregunta")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pregunta borrada",
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
    public ResponseEntity<Void> eliminarPregunta(@PathVariable Integer id) {
        preguntaService.eliminarPregunta(id);
        return ResponseEntity.noContent().build();
    }
}