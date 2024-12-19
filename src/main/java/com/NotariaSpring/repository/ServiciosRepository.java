package com.NotariaSpring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NotariaSpring.entity.Servicios;

@Repository
public interface ServiciosRepository extends JpaRepository<Servicios, Integer>{
	List<Servicios> findAll();
	List<Servicios> findByNombreContaining(String nombre);
	Servicios findByNombre(String nombre);
	List<Servicios> findByNombreContainingAndEstado(String nombre, String estado); 
	List<Servicios> findByEspecialidad(String especialidad);
}
