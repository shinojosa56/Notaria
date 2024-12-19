package com.NotariaSpring.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Servicios {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "Nombre")
	private String nombre;
	@Column(name = "Descripcion")
	private String descripcion;
	@Column(name = "Requisitos")
	// @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	private String requisitos;
	@Column(name = "Estado")
	private String estado;
	@Column(name = "Precio")
	private String precio;
	@Column(name = "Descuento")
	private String descuento;
	private String especialidad;

	@OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Horarios> horarios;

	public Servicios() {
	}

	// Constructor con parámetros (opcional)
	public Servicios(String nombre, String descripcion, String requisitos, String precio, String descuento,
			String estado, String especialidad) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.requisitos = requisitos;
		this.precio = precio;
		this.descuento = descuento;
		this.estado = estado;
		this.especialidad = especialidad;
	}

	public List<Horarios> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horarios> horarios) {
		this.horarios = horarios;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	// public List<Requisitos> getRequisitos() {
	// return requisitos;
	// }
	// public void setRequisitos(List<Requisitos> requisitos) {
	// this.requisitos = requisitos;
	// }
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPrecio() {
		return precio;
	}

	public String getRequisitos() {
		return requisitos;
	}

	public void setRequisitos(String requisitos) {
		this.requisitos = requisitos;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

}
