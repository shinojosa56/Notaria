package com.NotariaSpring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NotariaSpring.entity.Transaccion;
import com.NotariaSpring.entity.Usuarios;
import com.NotariaSpring.repository.TransaccionRepository;
import com.NotariaSpring.repository.UsuariosRepository;

@Controller
@RequestMapping("/recepcionista")
public class RecepcionistaController {

	@Autowired
	UsuariosRepository usuariosRepository;

	@Autowired
	TransaccionRepository transaccionRepository;

	@GetMapping("/Perfil")
	public String adminPerfil(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			Usuarios usuario = optionalUsuario.get();
			model.addAttribute("usuario", usuario);
			return "recepcionista/Perfil";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@GetMapping("/todasTransaccionesActivas")
	public String todasTransaccionesActivas(Model model) {
		List<Transaccion> transacciones = transaccionRepository.findByEstado("Activo");
		model.addAttribute("transacciones", transacciones);
		return "recepcionista/transaccionesActivas";
	}

	@GetMapping("/recepcionistaVerAbogados")
	public String verAbogadosEnTurno(Model model) {
		List<Usuarios> abogados = usuariosRepository.findByRolAndEstado("Abogado", "Activo");
		model.addAttribute("abogados", abogados);
		return "recepcionista/VerAbogados";
	}
}
