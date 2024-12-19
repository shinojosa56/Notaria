package com.NotariaSpring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NotariaSpring.entity.Usuarios;


@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Integer>{
	List<Usuarios> findAll();
	List<Usuarios> findByNombreContaining(String nombre);
	Usuarios findByUsuarioAndContraseña(String usuario, String contraseña);
	Usuarios findByNombre(String nombre);
	Usuarios findByDni(String dni); 
	Usuarios findByUsuario(String usuario);
	List<Usuarios> findByEspecialidad(String especialidad);
	List<Usuarios> findByRolAndEspecialidad(String rol, String especialidad);
	List<Usuarios> findByRolAndEstado(String rol, String estado);
	
}
