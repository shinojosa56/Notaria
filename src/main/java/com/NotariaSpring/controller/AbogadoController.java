package com.NotariaSpring.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NotariaSpring.entity.Servicios;
import com.NotariaSpring.entity.Transaccion;
import com.NotariaSpring.entity.Usuarios;
import com.NotariaSpring.repository.ServiciosRepository;
import com.NotariaSpring.repository.TransaccionRepository;
import com.NotariaSpring.repository.UsuariosRepository;

@Controller
@RequestMapping("/abogado")
public class AbogadoController {

	@Autowired
	UsuariosRepository usuariosRepository;

	@Autowired
	TransaccionRepository transaccionRepository;

	@Autowired
	ServiciosRepository serviciosRepository;

	@GetMapping("/abogadoPerfil")
	public String adminPerfil(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			Usuarios usuario = optionalUsuario.get();
			model.addAttribute("usuario", usuario);
			return "abogado/Perfil";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@GetMapping("/editarAbogado/{id}")
	public String editarAdmin(@PathVariable("id") int id, Model model) {
		Optional<Usuarios> usuarios = usuariosRepository.findById(id);
		if (usuarios.isPresent()) {
			model.addAttribute("usuarios", usuarios.get());
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
		return "abogado/editarAbogado";
	}

	@PostMapping("/actualizarAbogado")
	public String actualizarAdmin(@ModelAttribute("usuarios") Usuarios usuarios, Model model) {
		Usuarios usuarioExistente = usuariosRepository.findById(usuarios.getId()).orElse(null);
		if (usuarioExistente != null) {
			usuarioExistente.setTelefono(usuarios.getTelefono());
			usuarioExistente.setDireccion(usuarios.getDireccion());
			usuarioExistente.setContraseña(usuarios.getContraseña());
			usuariosRepository.save(usuarioExistente);
			model.addAttribute("mensaje", "Información actualizada con éxito.");
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado.");
		}
		return "redirect:/abogado/abogadoPerfil?usuarioId=" + usuarios.getId();
	}

	@GetMapping("/volver")
	public String volver(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		model.addAttribute("usuarioId", usuarioId);
		return "/abogado";
	}

	@GetMapping("/abogadoCitasPendientes")
	public String citasPendientes(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			List<Transaccion> transacciones = transaccionRepository.findByUsuarioSeleccionadoIdAndEstado(usuarioId,
					"Activo");
			model.addAttribute("transacciones", transacciones);
			model.addAttribute("usuarioId", usuarioId);
			return "abogado/abogadoCitasPendientes";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@GetMapping("/abogadoProgramarCita")
	public String mostrarFormularioCita(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> usuarioLogeado = usuariosRepository.findById(usuarioId);
		if (usuarioLogeado.isPresent()) {
			List<Servicios> servicios = serviciosRepository.findByEspecialidad(usuarioLogeado.get().getEspecialidad());
			model.addAttribute("servicios", servicios);
			model.addAttribute("usuarioId", usuarioId);
			return "abogado/abogadoProgramarCita";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@PostMapping("/crearCita")
	public String crearCita(@RequestParam("dniCliente") String dniCliente,
			@RequestParam("servicioId") Integer servicioId, @RequestParam("horario") String horario,
			@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Usuarios usuarioSeleccionado = usuariosRepository.findByDni(dniCliente);
		Optional<Usuarios> usuarioLogeado = usuariosRepository.findById(usuarioId);
		Optional<Servicios> servicio = serviciosRepository.findById(servicioId);
		if (usuarioSeleccionado != null && usuarioLogeado.isPresent() && servicio.isPresent()) {
			Transaccion transaccion = new Transaccion();
			transaccion.setUsuarioSeleccionado(usuarioLogeado.get());
			transaccion.setUsuarioLogeado(usuarioSeleccionado);
			transaccion.setServicio(servicio.get());
			transaccion.setHorario(horario);
			transaccion.setFecha(LocalDateTime.now());
			transaccion.setEstado("Activo");
			// Establecer el estado a "Activo"
			transaccionRepository.save(transaccion);
			model.addAttribute("mensaje", "Cita creada con éxito.");
		} else if (usuarioSeleccionado == null) {
			model.addAttribute("mensaje", "Usuario no encontrado.");
		}
		return "redirect:/abogado/abogadoProgramarCita?usuarioId=" + usuarioId;
	}

	@PostMapping("/completarTransaccion")
	public String completarTransaccion(@RequestParam("transaccionId") Long transaccionId, Model model) {
		Optional<Transaccion> optionalTransaccion = transaccionRepository.findById(transaccionId);
		if (optionalTransaccion.isPresent()) {
			Transaccion transaccion = optionalTransaccion.get();
			transaccion.setEstado("Inactiva");
			transaccionRepository.save(transaccion);
			model.addAttribute("mensaje", "Transacción completada con éxito.");
			return "redirect:/abogado/abogadoCitasPendientes?usuarioId=" + transaccion.getUsuarioSeleccionado().getId();
		} else {
			model.addAttribute("mensaje", "Transacción no encontrada.");
			return "redirect:/abogado/abogadoCitasPendientes";
			// Ajuste aquí para manejar el caso cuando la transacción no se encuentra
		}
	}

	@GetMapping("/abogadoCitasHistorial")
	public String citasHistorial(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			List<Transaccion> transacciones = transaccionRepository.findByUsuarioSeleccionadoIdAndEstado(usuarioId,
					"Inactiva");
			model.addAttribute("transacciones", transacciones);
			model.addAttribute("usuarioId", usuarioId);
			return "abogado/abogadoHistorialCitas";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}
}
