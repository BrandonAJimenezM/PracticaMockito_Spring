package com.api.rest.pruebas_unitarias_spring_boot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.api.rest.pruebas_unitarias_spring_boot.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
public class EmpleadoRepositoryTests {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = Empleado.builder()
                .nombre("Juan")
                .apellido("Juan")
                .email("c1@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado() {
        //Concepto BDD
        //dado o give o condicion previa o configuracion
        Empleado empleado1 = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@gmail.com")
                .build();
        //when o accion o el comportamiento que vamos a probar
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);
        //then verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);

    }

    @DisplayName("Test para listar a los empleados")
    @Test
    void testListarEmpleados() {
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Julen")
                .apellido("Oliver")
                .email("j2@gmail.com")
                .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);
        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then

        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    void testObtenerEmpleadoPorId() {
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        //then
        assertThat(empleadoBD).isNotNull();

    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado() {
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setNombre("Christian Raul");
        empleadoGuardado.setApellido("Ramirez Cucitini");
        empleadoGuardado.setEmail("c232@gmail.com");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("c232@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Christian Raul");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado() {
        empleadoRepository.save(empleado);
        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());
        //then
        assertThat(empleadoOptional).isEmpty();

    }


}
