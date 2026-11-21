package com.example.mpaz_neb.api.config;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.mpaz_neb.api.model.Estudiante;
import com.example.mpaz_neb.api.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EstudianteRepository estudianteRepository;

    @Override
    public void run(String... args) throws Exception {
        if (estudianteRepository.count() == 0) {
            estudianteRepository.saveAll(estudiantesIniciales());
        }
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