package com.NotariaSpring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horarios {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String valor;

	@ManyToOne
	@JoinColumn(name = "servicio_id", nullable = false)
	private Servicios servicio;

	// Constructor por defecto
	public Horarios() {
	}

	// Constructor con parámetros
	public Horarios(String valor, Servicios servicio) {
		this.valor = valor;
		this.servicio = servicio;
	} // Getters y setters

	public Horarios(String valor) {
		this.valor = valor;
	}

	public Servicios getServicio() {
		return servicio;
	}

	public void setServicio(Servicios servicio) {
		this.servicio = servicio;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
