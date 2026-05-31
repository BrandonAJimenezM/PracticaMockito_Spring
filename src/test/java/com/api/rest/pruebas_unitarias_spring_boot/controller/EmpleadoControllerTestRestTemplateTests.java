package com.api.rest.pruebas_unitarias_spring_boot.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.api.rest.pruebas_unitarias_spring_boot.model.Empleado;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    @Order(1)
    void testGuardarEmpleado() {
        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        ResponseEntity<Empleado> response = testRestTemplate.postForEntity("/api/empleados", empleado, Empleado.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());


        Empleado empleadoCreado = response.getBody();
        assertNotNull(empleadoCreado);

        assertNotNull(empleadoCreado.getId());
        assertEquals("Christian", empleadoCreado.getNombre());
        assertEquals("Ramirez", empleadoCreado.getApellido());
        assertEquals("c1@gmail.com", empleadoCreado.getEmail());
    }

    @Test
    @Order(2)
    void testListarEmpleados() {

        ResponseEntity<Empleado[]> response =
                testRestTemplate.getForEntity(
                        "/api/empleados",
                        Empleado[].class);

        List<Empleado> empleados = Arrays.asList(response.getBody());


        empleados.forEach(System.out::println);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
