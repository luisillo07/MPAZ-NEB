package com.example.mpaz_neb.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.mpaz_neb.api.model.Actividad;
import com.example.mpaz_neb.api.model.Estudiante;
import com.example.mpaz_neb.api.model.Pregunta;
import com.example.mpaz_neb.api.repository.ActividadRepository;
import com.example.mpaz_neb.api.repository.EstudianteRepository;
import com.example.mpaz_neb.api.repository.PreguntaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EstudianteRepository estudianteRepository;
    private final ActividadRepository actividadRepository;
    private final PreguntaRepository preguntaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (estudianteRepository.count() > 0) {
            return;
        }
        for (int i = 1; i < 7; i++) {
            Estudiante estudiante = Estudiante.builder()
                    .codigo("EST-00" + i)
                    .nombre("Estudiante ficticio " + i)
                    .usuario("estudiante" + i)
                    .build();
            estudianteRepository.save(estudiante);
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

        Pregunta pregunta1 = Pregunta.builder()
                .actividadCodigo("LEC-001")
                .tipo("explícita")
                .enunciado("¿De qué color es Tito?")
                .alternativaA("Negro")
                .alternativaB("Café")
                .alternativaC("Blanco")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("El texto dice: «Tito es un perro café».")
                .build();
        preguntaRepository.save(pregunta1);

        Pregunta pregunta2 = Pregunta.builder()
                .actividadCodigo("LEC-001")
                .tipo("explícita")
                .enunciado("¿Con qué juega Tito?")
                .alternativaA("Con un hueso")
                .alternativaB("Con una pelota roja")
                .alternativaC("Con Ana en la plaza")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("Todas las tardes Tito juega con una pelota roja.")
                .build();
        preguntaRepository.save(pregunta2);

        Pregunta pregunta3 = Pregunta.builder()
                .actividadCodigo("LEC-001")
                .tipo("implícita")
                .enunciado("¿Dónde juega Tito con su pelota?")
                .alternativaA("En la calle")
                .alternativaB("En el patio de la casa")
                .alternativaC("En la escuela")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("El texto no lo dice directo, pero Tito vive en una casa con un patio grande y juega ahí todas las tardes.")
                .build();
        preguntaRepository.save(pregunta3);

        Pregunta pregunta4 = Pregunta.builder()
                .actividadCodigo("LEC-001")
                .tipo("vocabulario")
                .enunciado("¿Qué significa la palabra «patio»?")
                .alternativaA("Un espacio abierto de la casa")
                .alternativaB("Un tipo de perro")
                .alternativaC("Un juguete")
                .correcta(0)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("Un patio es un espacio abierto dentro de una casa.")
                .build();
        preguntaRepository.save(pregunta4);

        Pregunta pregunta5 = Pregunta.builder()
                .actividadCodigo("LEC-002")
                .tipo("explícita")
                .enunciado("¿De qué color es la casa de Ana?")
                .alternativaA("Azul")
                .alternativaB("Amarilla")
                .alternativaC("Verde")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("El texto dice: «Ana vive con su abuela en una casa amarilla».")
                .build();
        preguntaRepository.save(pregunta5);

        Pregunta pregunta6 = Pregunta.builder()
                .actividadCodigo("LEC-002")
                .tipo("explícita")
                .enunciado("¿Con quién vive Ana?")
                .alternativaA("Con su abuela")
                .alternativaB("Con su hermano")
                .alternativaC("Con su perro")
                .correcta(0)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("El texto dice: «Ana vive con su abuela en una casa amarilla».")
                .build();
        preguntaRepository.save(pregunta6);

        Pregunta pregunta7 = Pregunta.builder()
                .actividadCodigo("LEC-002")
                .tipo("implícita")
                .enunciado("¿En qué parte de la casa preparan el pan?")
                .alternativaA("En el patio")
                .alternativaB("En la cocina")
                .alternativaC("En el dormitorio")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("El texto no lo dice directo, pero el pan se prepara en la cocina, y el texto dice que su casa tiene una cocina con una mesa de madera.")
                .build();
        preguntaRepository.save(pregunta7);

        Pregunta pregunta8 = Pregunta.builder()
                .actividadCodigo("LEC-002")
                .tipo("vocabulario")
                .enunciado("¿Qué significa la palabra «madera»?")
                .alternativaA("Un tipo de comida")
                .alternativaB("Material de los árboles")
                .alternativaC("Un color")
                .correcta(1)
                .datosNumericos("")
                .operacion("")
                .procedimiento("")
                .retroalimentacion("La madera es el material que sale del tronco de los árboles.")
                .build();
        preguntaRepository.save(pregunta8);

        Pregunta pregunta9 = Pregunta.builder()
                .actividadCodigo("MAT-001")
                .tipo("Problema")
                .enunciado("Ana tiene 12 manzanas y compra 5 más. ¿Cuántas manzanas tiene ahora?")
                .alternativaA("15")
                .alternativaB("17")
                .alternativaC("7")
                .correcta(1)
                .datosNumericos("12 y 5")
                .operacion("Adición")
                .procedimiento("12 + 5 = 17")
                .retroalimentacion("Si juntas 12 manzanas y 5 manzanas, tienes 17.")
                .build();
        preguntaRepository.save(pregunta9);

        Pregunta pregunta10 = Pregunta.builder()
                .actividadCodigo("MAT-001")
                .tipo("Problema")
                .enunciado("En la feria hay 20 globos y se venden 8. ¿Cuántos globos quedan?")
                .alternativaA("12")
                .alternativaB("28")
                .alternativaC("10")
                .correcta(0)
                .datosNumericos("20 y 8")
                .operacion("Sustracción")
                .procedimiento("20 - 8 = 12")
                .retroalimentacion("Si a 20 globos les quitas los 8 que se vendieron, quedan 12.")
                .build();
        preguntaRepository.save(pregunta10);

        Pregunta pregunta11 = Pregunta.builder()
                .actividadCodigo("MAT-001")
                .tipo("Cálculo")
                .enunciado("¿Cuánto es 25 + 10?")
                .alternativaA("30")
                .alternativaB("15")
                .alternativaC("35")
                .correcta(2)
                .datosNumericos("25 y 10")
                .operacion("Adición")
                .procedimiento("25 + 10 = 35")
                .retroalimentacion("Si a 25 le sumas 10, llegas a 35.")
                .build();
        preguntaRepository.save(pregunta11);

        Pregunta pregunta12 = Pregunta.builder()
                .actividadCodigo("MAT-001")
                .tipo("Cálculo")
                .enunciado("¿Cuánto es 30 - 14?")
                .alternativaA("44")
                .alternativaB("16")
                .alternativaC("14")
                .correcta(1)
                .datosNumericos("30 y 14")
                .operacion("Sustracción")
                .procedimiento("30 - 14 = 16")
                .retroalimentacion("Si a 30 le quitas 14, quedan 16.")
                .build();
        preguntaRepository.save(pregunta12);
    }
}