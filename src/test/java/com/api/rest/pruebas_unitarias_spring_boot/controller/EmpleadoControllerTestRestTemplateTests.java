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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        System.out.println("ID generado: " + empleadoCreado.getId());
    }

    @Test
    @Order(2)
    void testListarEmpleados() {

        ResponseEntity<Empleado[]> response =
                testRestTemplate.getForEntity(
                        "/api/empleados",
                        Empleado[].class);

        List<Empleado> empleados = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());

        empleados.forEach(System.out::println);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(1,empleados.size());
        assertEquals(1L,empleados.getFirst().getId());
        assertEquals("Christian",empleados.getFirst().getNombre());
        assertEquals("Ramirez",empleados.getFirst().getApellido());
        assertEquals("c1@gmail.com",empleados.getFirst().getEmail());
    }

    @Test
    @Order(3)
    void testObtenerEmpleado(){
        ResponseEntity<Empleado> response =
                testRestTemplate.getForEntity(
                        "/api/empleados/1",
                        Empleado.class);
        Empleado empleado = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());

        assertNotNull(empleado);assertEquals(1L, empleado.getId());
        assertEquals("Christian",empleado.getNombre());
        assertEquals("Ramirez",empleado.getApellido());
        assertEquals("c1@gmail.com",empleado.getEmail());
    }

    @Test
    @Order(4)
    void testEliminarEmpleado(){
        ResponseEntity<Empleado[]> respuesta = testRestTemplate.getForEntity("/api/empleados",Empleado[].class);
        List<Empleado> empleados = Arrays.asList(respuesta.getBody());
        assertEquals(1,empleados.size());

        Map<String,Long> pathVariables = new HashMap<>();
        pathVariables.put("id",1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/empleados/{id}", HttpMethod.DELETE,null,Void.class,pathVariables);

        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        respuesta = testRestTemplate.getForEntity("/api/empleados",Empleado[].class);
        empleados = Arrays.asList(respuesta.getBody());
        assertEquals(0,empleados.size());

        ResponseEntity<Empleado> respuestaDetalle = testRestTemplate.getForEntity("/api/empleados/2",Empleado.class);
        assertEquals(HttpStatus.NOT_FOUND,respuestaDetalle.getStatusCode());
        assertFalse(respuestaDetalle.hasBody());
    }
}
