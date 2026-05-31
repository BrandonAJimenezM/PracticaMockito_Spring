package com.api.rest.pruebas_unitarias_spring_boot.repository;

import com.api.rest.pruebas_unitarias_spring_boot.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByEmail(String email);
}
