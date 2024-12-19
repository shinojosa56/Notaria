package com.NotariaSpring.controller;

import java.io.IOException;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;

import com.NotariaSpring.entity.Servicios;
import com.NotariaSpring.entity.Transaccion;
import com.NotariaSpring.entity.Usuarios;
import com.NotariaSpring.repository.HorariosRepository;
import com.NotariaSpring.repository.ServiciosRepository;
import com.NotariaSpring.repository.TransaccionRepository;
import com.NotariaSpring.repository.UsuariosRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	UsuariosRepository usuariosRepository;

	@Autowired
	ServiciosRepository serviciosRepository;

	@Autowired
	TransaccionRepository transaccionRepository;

	@Autowired
	HorariosRepository horariosRepository;

	@GetMapping("/clientePerfil")
	public String adminPerfil(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			Usuarios usuario = optionalUsuario.get();
			model.addAttribute("usuario", usuario);
			return "cliente/Perfil";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@GetMapping("/editarCliente/{id}")
	public String editarAdmin(@PathVariable("id") int id, Model model) {
		Optional<Usuarios> usuarios = usuariosRepository.findById(id);
		if (usuarios.isPresent()) {
			model.addAttribute("usuarios", usuarios.get());
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
		return "cliente/editarCliente";
	}

	@PostMapping("/actualizarCliente")
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
		return "redirect:/cliente/clientePerfil?usuarioId=" + usuarios.getId();
	}

	@GetMapping("/volver")
	public String volver(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		model.addAttribute("usuarioId", usuarioId);
		return "/cliente";
	}

	@GetMapping("/clienteServiciosDisponible")
	public String mostrarServiciosDisponibles(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		List<Servicios> listaServicios = serviciosRepository.findAll();
		model.addAttribute("listaServicios", listaServicios);
		model.addAttribute("usuarioId", usuarioId);
		return "cliente/serviciosDisponible";
	}

	@GetMapping("/serviciosxAbogado")
	public String mostrarServiciosxAbogado(@RequestParam("usuarioId") Integer usuarioId,
			@RequestParam("servicioId") Integer servicioId, @RequestParam("horario") String horario, Model model) {
		Optional<Servicios> optionalServicio = serviciosRepository.findById(servicioId);
		if (optionalServicio.isPresent()) {
			Servicios servicio = optionalServicio.get();
			String especialidadServicio = servicio.getEspecialidad();
			List<Usuarios> listaUsuarios = usuariosRepository.findByRolAndEspecialidad("abogado", especialidadServicio);
			model.addAttribute("listaUsuarios", listaUsuarios);
			model.addAttribute("usuarioId", usuarioId);
			model.addAttribute("servicioId", servicioId);
			model.addAttribute("horario", horario);
			// Añadimos el horario al modelo
			return "cliente/serviciosxAbogado";
		} else {
			model.addAttribute("mensaje", "Servicio no encontrado");
			return "error";
		}
	}

	@GetMapping("/buscarServicios")
	public String buscarServicios(@RequestParam("nombre") String nombre, @RequestParam("usuarioId") Integer usuarioId,
			Model model) {
		List<Servicios> listaServicios = serviciosRepository.findByNombreContainingAndEstado(nombre, "Activo");
		if (listaServicios.isEmpty()) {
			model.addAttribute("mensaje", "No se encontraron servicios activos con ese nombre.");
		}
		model.addAttribute("listaServicios", listaServicios);
		model.addAttribute("usuarioId", usuarioId);
		return "cliente/serviciosDisponible";
	}

	@GetMapping("/comprarServicio/{servicioId}/{usuarioIdSeleccionado}/{usuarioLogeadoId}/{horario}")
	public String comprarServicio(@PathVariable("servicioId") int servicioId,
			@PathVariable("usuarioIdSeleccionado") int usuarioIdSeleccionado,
			@PathVariable("usuarioLogeadoId") int usuarioLogeadoId, @PathVariable("horario") String horario,
			Model model) {
		Optional<Servicios> optionalServicio = serviciosRepository.findById(servicioId);
		if (optionalServicio.isPresent()) {
			Servicios servicio = optionalServicio.get();
			model.addAttribute("servicio", servicio);
			model.addAttribute("usuarioIdSeleccionado", usuarioIdSeleccionado);
			model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
			model.addAttribute("horario", horario);
			// Añadimos el horario al modelo
			return "cliente/pasarelaPago";
		} else {
			model.addAttribute("mensaje", "Servicio no encontrado.");
			return "error";
		}
	}

	@PostMapping("/realizarPago")
	public String realizarPago(@RequestParam("cardNumber") String cardNumber,
			@RequestParam("expiryDate") String expiryDate, @RequestParam("cvv") String cvv,
			@RequestParam("servicioId") int servicioId,
			@RequestParam("usuarioIdSeleccionado") int usuarioIdSeleccionado,
			@RequestParam("usuarioLogeadoId") int usuarioLogeadoId, @RequestParam("horario") String horario,
			Model model) {
		model.addAttribute("mensaje", "Pago realizado con éxito.");
		Optional<Usuarios> usuarioLogeadoOpt = usuariosRepository.findById(usuarioLogeadoId);
		Optional<Servicios> servicioOpt = serviciosRepository.findById(servicioId);
		Optional<Usuarios> usuarioSeleccionadoOpt = usuariosRepository.findById(usuarioIdSeleccionado);
		if (usuarioLogeadoOpt.isPresent() && servicioOpt.isPresent() && usuarioSeleccionadoOpt.isPresent()) {
			Usuarios usuarioLogeado = usuarioLogeadoOpt.get();
			Servicios servicio = servicioOpt.get();
			Usuarios usuarioSeleccionado = usuarioSeleccionadoOpt.get();
			Transaccion nuevaTransaccion = new Transaccion();
			nuevaTransaccion.setUsuarioLogeado(usuarioLogeado);
			nuevaTransaccion.setServicio(servicio);
			nuevaTransaccion.setUsuarioSeleccionado(usuarioSeleccionado);
			nuevaTransaccion.setFecha(LocalDateTime.now());
			nuevaTransaccion.setDetallesServicio("Detalles del servicio");
			nuevaTransaccion.setHorario(horario);
			nuevaTransaccion.setEstado("Activo");
			// Establecer el estado a "Activo"
			transaccionRepository.save(nuevaTransaccion);
		} else {
			model.addAttribute("mensaje", "Error al procesar la transacción.");
			return "error";
		}
		model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
		return "cliente/resultadoPago";
	}

	@GetMapping("/transaccionesUsuario")
	public String obtenerTransaccionesUsuario(@RequestParam("usuarioId") Integer usuarioLogeadoId, Model model) {
		List<Transaccion> transacciones = transaccionRepository.findByUsuarioLogeadoId(usuarioLogeadoId);
		model.addAttribute("transacciones", transacciones);
		model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
		if (model.containsAttribute("mensaje")) {
			model.addAttribute("mensaje", model.getAttribute("mensaje"));
		}
		return "cliente/clienteServicios";
	}

	@PostMapping("/subirDocumento")
	public String subirDocumento(@RequestParam("transaccionId") Long transaccionId,
			@RequestParam("documento") MultipartFile documento,
			@RequestParam("usuarioLogeadoId") Integer usuarioLogeadoId, Model model) {
		Optional<Transaccion> optionalTransaccion = transaccionRepository.findById(transaccionId);
		if (optionalTransaccion.isPresent()) {
			Transaccion transaccion = optionalTransaccion.get();
			if (transaccion.getHorario() == null) {
				model.addAttribute("mensaje", "Error: El horario no puede ser nulo.");
				model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
				List<Transaccion> transacciones = transaccionRepository.findByUsuarioLogeadoId(usuarioLogeadoId);
				model.addAttribute("transacciones", transacciones);
				return "cliente/clienteServicios";
			}
			try {
				byte[] documentoBytes = documento.getBytes();
				transaccion.setDocumento(documentoBytes);
				// Obtener solo el nombre del archivo sin la ruta
				String nombreArchivo = Paths.get(documento.getOriginalFilename()).getFileName().toString();
				transaccion.setNombreDocumento(nombreArchivo);
				transaccionRepository.save(transaccion);
				model.addAttribute("mensaje", "Archivo subido con éxito.");
			} catch (IOException e) {
				model.addAttribute("mensaje", "Error al cargar el documento.");
			}
		} else {
			model.addAttribute("mensaje", "Transacción no encontrada.");
		}
		model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
		List<Transaccion> transacciones = transaccionRepository.findByUsuarioLogeadoId(usuarioLogeadoId);
		model.addAttribute("transacciones", transacciones);
		return "cliente/clienteServicios";
	}

	@GetMapping("/descargarDocumento/{transaccionId}")
	public void descargarDocumento(@PathVariable("transaccionId") Long transaccionId, HttpServletResponse response) {
		Optional<Transaccion> optionalTransaccion = transaccionRepository.findById(transaccionId);
		if (optionalTransaccion.isPresent()) {
			Transaccion transaccion = optionalTransaccion.get();
			byte[] documento = transaccion.getDocumento();
			if (documento != null) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachment; filename=documento.pdf");
				try {
					response.getOutputStream().write(documento);
					response.getOutputStream().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
