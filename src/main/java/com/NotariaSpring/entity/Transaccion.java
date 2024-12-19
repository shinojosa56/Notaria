package com.NotariaSpring.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transacciones")
public class Transaccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "usuario_logeado_id", nullable = false)
	private Usuarios usuarioLogeado;
	@ManyToOne
	@JoinColumn(name = "servicio_id", nullable = false)
	private Servicios servicio;
	@ManyToOne
	@JoinColumn(name = "usuario_seleccionado_id", nullable = false)
	private Usuarios usuarioSeleccionado;
	private LocalDateTime fecha;
	@Column(name = "detalles_servicio")
	private String detallesServicio;

	@Column(name = "horario", nullable = false)
	private String horario; // Campo de horario como String

	@Lob
	@Column(name = "documento")
	private byte[] documento;

	@Column(name = "nombre_documento")
	private String nombreDocumento;

	@Column(name = "estado", length = 50)
	private String estado;

	public Transaccion() {
	}

	public Transaccion(Usuarios usuarioLogeado, Servicios servicio, Usuarios usuarioSeleccionado, LocalDateTime fecha,
			String detallesServicio, String horario) {
		this.usuarioLogeado = usuarioLogeado;
		this.servicio = servicio;
		this.usuarioSeleccionado = usuarioSeleccionado;
		this.fecha = fecha;
		this.detallesServicio = detallesServicio;
		this.horario = horario;
	}

	public Transaccion(Usuarios usuarioLogeado, Servicios servicio, Usuarios usuarioSeleccionado, LocalDateTime fecha,
			String detallesServicio, String horario, byte[] documento) {
		this.usuarioLogeado = usuarioLogeado;
		this.servicio = servicio;
		this.usuarioSeleccionado = usuarioSeleccionado;
		this.fecha = fecha;
		this.detallesServicio = detallesServicio;
		this.horario = horario;
		this.documento = documento;
	}

	public Transaccion(Usuarios usuarioLogeado, Servicios servicio, Usuarios usuarioSeleccionado, LocalDateTime fecha,
			String detallesServicio, String horario, byte[] documento, String nombreDocumento) {
		this.usuarioLogeado = usuarioLogeado;
		this.servicio = servicio;
		this.usuarioSeleccionado = usuarioSeleccionado;
		this.fecha = fecha;
		this.detallesServicio = detallesServicio;
		this.horario = horario;
		this.documento = documento;
		this.nombreDocumento = nombreDocumento;
	}

	public Transaccion(Usuarios usuarioLogeado, Servicios servicio, Usuarios usuarioSeleccionado, LocalDateTime fecha,
			String detallesServicio, String horario, byte[] documento, String nombreDocumento, String estado) {
		this.usuarioLogeado = usuarioLogeado;
		this.servicio = servicio;
		this.usuarioSeleccionado = usuarioSeleccionado;
		this.fecha = fecha;
		this.detallesServicio = detallesServicio;
		this.horario = horario;
		this.documento = documento;
		this.nombreDocumento = nombreDocumento;
		this.estado = estado;
	}

	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}

	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}

	public byte[] getDocumento() {
		return documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuarios getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(Usuarios usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	public Servicios getServicio() {
		return servicio;
	}

	public void setServicio(Servicios servicio) {
		this.servicio = servicio;
	}

	public Usuarios getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getDetallesServicio() {
		return detallesServicio;
	}

	public void setDetallesServicio(String detallesServicio) {
		this.detallesServicio = detallesServicio;
	}

}
