package com.NotariaSpring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NotariaSpring.entity.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
	List<Transaccion> findByUsuarioLogeadoId(Integer usuarioLogeadoId);
	List<Transaccion> findByUsuarioSeleccionadoId(Integer usuarioSeleccionadoId);
	List<Transaccion> findByUsuarioSeleccionadoIdAndEstado(Integer usuarioSeleccionadoId, String estado);
	List<Transaccion> findByEstado(String estado);
}
