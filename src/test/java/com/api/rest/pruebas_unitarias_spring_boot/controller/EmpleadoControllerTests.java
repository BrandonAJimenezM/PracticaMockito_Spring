package com.api.rest.pruebas_unitarias_spring_boot.controller;

import com.api.rest.pruebas_unitarias_spring_boot.model.Empleado;
import com.api.rest.pruebas_unitarias_spring_boot.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(empleadoController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGuardarEmpleado() throws Exception {

        // GIVEN
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willReturn(empleado);

        // WHEN
        ResultActions response = mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado)));

        // THEN
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception {
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());

        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);
        //when

        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));


    }

    @Test
    void testObtenerEmpleadoId() throws Exception {
        //given
        Long id = 1L;
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.of(empleado));
        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", id));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));



    }
    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception {
        //given
        Long id = 1L;
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.empty());
        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", id));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());


    }

    @Test
    void testActualizarEmpleado() throws Exception {
        //given
        Long id = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Christian")
                .apellido("Lopez")
                .email("c1@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .id(1L)
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willReturn(empleadoActualizado);

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));



    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {
        //given
        Long id = 1L;


        Empleado empleadoActualizado = Empleado.builder()
                .id(1L)
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.empty());


        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());



    }

    @Test
    void testEliminarEmpleado() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(id);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}", id));

        //then
        response.andExpect(status().isOk())
                .andDo(print());

    }
}