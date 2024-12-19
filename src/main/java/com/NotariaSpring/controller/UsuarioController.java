package com.NotariaSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.NotariaSpring.entity.Usuarios;
import com.NotariaSpring.repository.UsuariosRepository;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	UsuariosRepository usuarioRepository;

	@PostMapping("/validarAcceso")
	public String validarAcceso(@RequestParam("usuario") String usuario, @RequestParam("contraseña") String contraseña,
			Model model) {
		Usuarios usuarios = usuarioRepository.findByUsuarioAndContraseña(usuario, contraseña);
		if (usuarios != null) {
			model.addAttribute("usuarioId", usuarios.getId());
			switch (usuarios.getRol()) {
			case "admin":
				return "admin";
			case "recepcionista":
				return "recepcionista";
			case "abogado":
				return "abogado";
			default:
				return "cliente";
			}
		} else {
			model.addAttribute("error", "Usuario o contraseña incorrectos.");
			return "index";
		}
	}

	@PostMapping("/crear")
	public String crearUsuario(@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("dni") String dni, @RequestParam("direccion") String direccion,
			@RequestParam("telefono") String telefono, @RequestParam("usuario") String usuario,
			@RequestParam("contraseña") String contraseña, Model model) {
		if (usuarioRepository.findByDni(dni) != null) { 
			model.addAttribute("error", "El DNI ya está registrado."); 
			return "crearUsuario"; } 
		if (usuarioRepository.findByUsuario(usuario) != null) { 
			model.addAttribute("error", "El correo ya está registrado."); 
			return "crearUsuario"; 
			}
		Usuarios nuevoUsuario = new Usuarios();
		nuevoUsuario.setNombre(nombre);
		nuevoUsuario.setApellido(apellido);
		nuevoUsuario.setDni(dni);
		nuevoUsuario.setDireccion(direccion);
		nuevoUsuario.setTelefono(telefono);
		nuevoUsuario.setUsuario(usuario);
		nuevoUsuario.setContraseña(contraseña);
		nuevoUsuario.setRol("Cliente");
		// Valor por defecto
		nuevoUsuario.setEstado("Activo"); // Valor por defecto
		usuarioRepository.save(nuevoUsuario);
		return "redirect:/";
	}

}
