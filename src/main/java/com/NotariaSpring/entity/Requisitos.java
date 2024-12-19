package com.NotariaSpring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Requisitos {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id; 
	private String requisito; 
	@ManyToOne 
	@JoinColumn(name = "servicio_id") 
	private Servicios servicio;
	
	public Requisitos(String requisito, Servicios servicio) { 
		this.requisito = requisito; 
		this.servicio = servicio; }
	
	public Requisitos() { }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRequisito() {
		return requisito;
	}
	public void setRequisito(String requisito) {
		this.requisito = requisito;
	}
	public Servicios getServicio() {
		return servicio;
	}
	public void setServicio(Servicios servicio) {
		this.servicio = servicio;
	}
	
}
