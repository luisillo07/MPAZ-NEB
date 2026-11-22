package com.example.mpaz_neb.api.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.mpaz_neb.api.model.Actividad;
import com.example.mpaz_neb.api.model.Estudiante;
import com.example.mpaz_neb.api.repository.ActividadRepository;
import com.example.mpaz_neb.api.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EstudianteRepository estudianteRepository;
    private final ActividadRepository actividadRepository;

    @Override
    public void run(String... args) throws Exception {
        if (estudianteRepository.count() == 0) {
            estudianteRepository.saveAll(estudiantesIniciales());
        }
        Actividad actividad1 = Actividad.builder()
                .codigo("LEC-001")
                .nombre("El perro Tito")
                .modulo("Lectura")
                .curso("2.º básico")
                .contenido("Comprensión lectora")
                .objetivo("Extraer información explícita e implícita de un texto narrativo breve")
                .dificultad("Fácil")
                .instrucciones("Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.")
                .cantidadPreguntas(4)
                .intentosPermitidos(2)
                .estado("disponible")
                .tituloTexto("El perro Tito")
                .tipoTexto("Narrativo")
                .contenidoTexto("Tito es un perro café. Vive con Ana en una casa con un patio grande. Todas las tardes Tito juega con una pelota roja.")
                .nivelVocabulario("Básico")
                .docente("docente")
                .build();
        actividadRepository.save(actividad1);

        Actividad actividad2 = Actividad.builder()
                .codigo("LEC-002")
                .nombre("La casa de Ana")
                .modulo("Lectura")
                .curso("2.º básico")
                .contenido("Comprensión lectora")
                .objetivo("Identificar personajes, lugares y acciones en un texto descriptivo breve")
                .dificultad("Medio")
                .instrucciones("Lee el texto con calma y responde las preguntas. Puedes volver a leer el texto cuando quieras.")
                .cantidadPreguntas(4)
                .intentosPermitidos(2)
                .estado("disponible")
                .tituloTexto("La casa de Ana")
                .tipoTexto("Descriptivo")
                .contenidoTexto("Ana vive con su abuela en una casa amarilla. En la cocina hay una mesa de madera. Los domingos, Ana y su abuela preparan pan para el desayuno.")
                .nivelVocabulario("Básico")
                .docente("docente")
                .build();
        actividadRepository.save(actividad2);

        Actividad actividad3 = Actividad.builder()
                .codigo("MAT-001")
                .nombre("Sumas y restas en la feria")
                .modulo("Matemática")
                .curso("2.º básico")
                .contenido("Adición y sustracción hasta 100")
                .objetivo("Resolver problemas simples de adición y sustracción con números hasta 100")
                .dificultad("Fácil")
                .instrucciones("Lee cada ejercicio con calma, haz el cálculo y marca la respuesta correcta. Puedes usar tu cuaderno para calcular.")
                .cantidadPreguntas(4)
                .intentosPermitidos(2)
                .estado("disponible")
                .tituloTexto("")
                .tipoTexto("")
                .contenidoTexto("")
                .nivelVocabulario("")
                .docente("docente")
                .build();
        actividadRepository.save(actividad3);

        Actividad actividad4 = Actividad.builder()
                .codigo("LEC-003")
                .nombre("Actividad 4")
                .modulo("Lectura")
                .curso("")
                .contenido("")
                .objetivo("")
                .dificultad("")
                .instrucciones("")
                .estado("borrador")
                .tituloTexto("")
                .tipoTexto("")
                .contenidoTexto("")
                .nivelVocabulario("")
                .docente("docente")
                .build();
        actividadRepository.save(actividad4);
    }

    private List<Estudiante> estudiantesIniciales() {
        return List.of(
                Estudiante.builder().codigo("EST-001").nombre("Estudiante ficticio 1").usuario("estudiante1").build(),
                Estudiante.builder().codigo("EST-002").nombre("Estudiante ficticio 2").usuario("estudiante2").build(),
                Estudiante.builder().codigo("EST-003").nombre("Estudiante ficticio 3").usuario("estudiante3").build(),
                Estudiante.builder().codigo("EST-004").nombre("Estudiante ficticio 4").usuario("estudiante4").build(),
                Estudiante.builder().codigo("EST-005").nombre("Estudiante ficticio 5").usuario("estudiante5").build(),
                Estudiante.builder().codigo("EST-006").nombre("Estudiante ficticio 6").usuario("estudiante6").build()
        );
    }
}