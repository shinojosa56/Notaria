package com.NotariaSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

	
	@GetMapping("/clienteServiciosDisponible")
	public String clienteServiciosDisponible() {
		return "cliente/serviciosDisponible";
	}
	@GetMapping("/clienteServicios")
	public String clienteServicios() {
		return "cliente/clienteServicios";
	}
	@GetMapping("/ayuda")
	public String ayuda() {
		return "ayuda";
	}
	@GetMapping("/abogadoPerfil")
	public String abogadoPerfil() {
		return "abogadoPerfil";
	}
	@GetMapping("/abogadoCitasPendientes")
	public String abogadoCitasPendientes() {
		return "abogadoCitasPendientes";
	}
	@GetMapping("/abogadoProgramarCita")
	public String abogadoProgramarCita() {
		return "abogadoProgramarCita";
	}
	@GetMapping("/abogadoHistorialCitas")
	public String abogadoHistorialCitas() {
		return "abogadoHistorialCitas";
	}
	@GetMapping("/clientePerfil")
	public String clientePerfil() {
		return "clientePerfil";
	}
	@GetMapping("/recepcionistaPerfil")
	public String recepcionistaPerfil() {
		return "recepcionistaPerfil";
	}
	@GetMapping("/recepcionistaVerAbogados")
	public String recepcionistaVerAbogados() {
		return "recepcionistaVerAbogados";
	}

	@GetMapping("/adminServicios")
	public String adminServicios() {
		return "admin/Servicios";
	}
	
	@GetMapping("/adminListaUsuarios")
	public String adminListaUsuarios() {
		return "admin/ListaUsuarios";
	}
	
	@GetMapping("/crearUsuario")
	public String crearUsuario() {
		return "crearUsuario";
	}
	
	@GetMapping("/index")
	public String index() {
		return "redirect:/";
	}
}
