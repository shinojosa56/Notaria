package com.NotariaSpring.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NotariaSpring.entity.Horarios;
import com.NotariaSpring.entity.Servicios;
import com.NotariaSpring.entity.Transaccion;
import com.NotariaSpring.entity.Usuarios;
import com.NotariaSpring.repository.HorariosRepository;
import com.NotariaSpring.repository.ServiciosRepository;
import com.NotariaSpring.repository.TransaccionRepository;
import com.NotariaSpring.repository.UsuariosRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	ServiciosRepository serviciosRepository;

	@Autowired
	UsuariosRepository usuariosRepository;

	@Autowired
	HorariosRepository horariosRepository;

	@Autowired
	TransaccionRepository transaccionRepository;

	// Servicios
	@GetMapping("/buscarServicios")
	public String buscarServicios(@RequestParam("nombre") String nombre, Model model) {
		List<Servicios> listaServicios = serviciosRepository.findByNombreContaining(nombre);
		model.addAttribute("listaServicios", listaServicios);
		if (listaServicios.isEmpty()) {
			model.addAttribute("mensaje", "No se encontraron servicios con ese nombre.");
		}
		return "admin/Servicios";
	}

	@GetMapping("editarServicio/{id}")
	public String editar(@PathVariable("id") int id, Model model) throws Exception {
		Optional<Servicios> optionalServicios = serviciosRepository.findById(id);
		if (optionalServicios.isPresent()) {
			Servicios servicios = optionalServicios.get();
			model.addAttribute("servicios", servicios);
			return "admin/editarServicio";
		} else {
			throw new Exception("Servicio no encontrado con id: " + id);
		}
	}

	@PostMapping("/actualizar")
	public String actualizarServicio(@ModelAttribute("servicio") Servicios servicio, Model model) {
		Optional<Servicios> optionalServicios = serviciosRepository.findById(servicio.getId());
		if (optionalServicios.isPresent()) {
			serviciosRepository.save(servicio);
			model.addAttribute("mensaje", "Servicio actualizado con éxito.");
		} else {
			model.addAttribute("mensaje", "Servicio no encontrado.");
		}
		return "redirect:/admin/buscarServicios?nombre=" + servicio.getNombre();
	}

	@GetMapping("/adminCrearServicio")
	public String adminCrearServicio(Model model) {
		Servicios servicios = new Servicios();
		model.addAttribute("servicios", servicios);
		return "admin/CrearServicio";
	}

	@PostMapping("/registrar")
	public String registrar(@ModelAttribute("servicios") Servicios servicios, Model model) {
		Servicios serviciosBD = serviciosRepository.findByNombre(servicios.getNombre());
		if (serviciosBD != null) {
			model.addAttribute("mensaje", "Ya existe el servicio en el sistema");
			model.addAttribute("servicios", servicios);
			return "admin/CrearServicio";
		} else {
			serviciosRepository.save(servicios);
			model.addAttribute("mensaje", "El servicio se registro con exito");
			return "admin/CrearServicio";
		}
	}

	// Perfil
	@GetMapping("/adminPerfil")
	public String adminPerfil(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Optional<Usuarios> optionalUsuario = usuariosRepository.findById(usuarioId);
		if (optionalUsuario.isPresent()) {
			Usuarios usuario = optionalUsuario.get();
			model.addAttribute("usuario", usuario);
			return "admin/Perfil";
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado");
			return "error";
		}
	}

	@GetMapping("editarAdmin/{id}")
	public String editarAdminr(@PathVariable("id") int id, Model model) {
		Optional<Usuarios> usuarios = usuariosRepository.findById(id);
		model.addAttribute("usuarios", usuarios);
		return "admin/editarAdmin";
	}

	@PostMapping("/actualizarAdmin")
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
		return "redirect:/admin/adminPerfil?usuarioId=" + usuarios.getId(); // Ajusta esto según la vista a la que
																			// quieres redirigir después de la
																			// actualización }
	}

	@GetMapping("/volver")
	public String volver(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		model.addAttribute("usuarioId", usuarioId);
		return "admin";
	}

	@GetMapping("/adminListaUsuarios")
	public String adminListaUsuarios(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		List<Usuarios> listaUsuarios = usuariosRepository.findAll();
		model.addAttribute("listaUsuarios", listaUsuarios);
		model.addAttribute("usuarioId", usuarioId);
		return "admin/ListaUsuarios";
	}

	// Usuarios
	@GetMapping("/buscarUsuarios")
	public String buscarUsuarios(@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "usuarioId", required = false) Integer usuarioId, Model model) {
		List<Usuarios> listaUsuarios;
		if (nombre != null && !nombre.isEmpty()) {
			listaUsuarios = usuariosRepository.findByNombreContaining(nombre);
			if (listaUsuarios.isEmpty()) {
				model.addAttribute("mensaje", "No se encontraron usuarios con ese nombre.");
			}
		} else {
			listaUsuarios = usuariosRepository.findAll();
		}
		model.addAttribute("listaUsuarios", listaUsuarios);
		model.addAttribute("usuarioId", usuarioId);
		return "admin/ListaUsuarios";
	}

	@GetMapping("editarUsuarios/{id}")
	public String editarUsuarios(@PathVariable("id") int id, @RequestParam("usuarioId") Integer usuarioId,
			Model model) {
		System.out.println("ID del usuario logeado: " + usuarioId);
		Optional<Usuarios> usuarios = usuariosRepository.findById(id);
		model.addAttribute("usuarios", usuarios.orElse(new Usuarios()));
		model.addAttribute("usuarioId", usuarioId);
		return "admin/editarUsuarios";
	}

	@PostMapping("/actualizarUsuario")
	public String actualizarUsuario(@ModelAttribute("usuarios") Usuarios usuarios,
			@RequestParam("usuarioId") Integer usuarioId, Model model) {
		Usuarios usuarioExistente = usuariosRepository.findById(usuarios.getId()).orElse(null);
		Usuarios usuarioLogeado = usuariosRepository.findById(usuarioId).orElse(null);
		if (usuarioExistente != null && usuarioLogeado != null) {
			usuarioExistente.setNombre(usuarios.getNombre());
			usuarioExistente.setApellido(usuarios.getApellido());
			usuarioExistente.setDni(usuarios.getDni());
			usuarioExistente.setDireccion(usuarios.getDireccion());
			usuarioExistente.setTelefono(usuarios.getTelefono());
			usuarioExistente.setRol(usuarios.getRol());
			usuarioExistente.setEstado(usuarios.getEstado());
			usuarioExistente.setTipoJornada(usuarios.getTipoJornada());
			usuarioExistente.setFecha(LocalDateTime.now());
			// Establecer la fecha actual
			usuarioExistente.setUsuarioActualizacion(usuarioLogeado.getDni());
			// Establecer el DNI del usuario que actualiza
			usuariosRepository.save(usuarioExistente);
			model.addAttribute("mensaje", "Usuario actualizado con éxito.");
		} else {
			model.addAttribute("mensaje", "Usuario no encontrado.");
		}
		return "redirect:/admin/adminListaUsuarios?usuarioId=" + usuarioId;
	}

	@GetMapping("/agregarHorarios/{servicioId}/{usuarioId}")
	public String agregarHorarios(@PathVariable("servicioId") Integer servicioId,
			@PathVariable("usuarioId") Integer usuarioId, Model model) {
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("servicioId", servicioId);
		return "/admin/agregarHorarios";
	}

	@PostMapping("/guardarHorarios")
	public String guardarHorarios(@RequestParam("servicioId") Integer servicioId,
			@RequestParam("horario") String horario, @RequestParam("usuarioId") Integer usuarioId, Model model) {
		Servicios servicio = serviciosRepository.findById(servicioId).orElse(null);
		if (servicio != null) {
			Horarios nuevoHorario = new Horarios(horario, servicio);
			nuevoHorario.setServicio(servicio);
			// Asegúrate de asignar el servicio
			servicio.getHorarios().add(nuevoHorario);
			horariosRepository.save(nuevoHorario);
			serviciosRepository.save(servicio);
		}
		model.addAttribute("usuarioId", usuarioId);
		return "admin";
	}

	@GetMapping("/adminServicios")
	public String adminServicios(@RequestParam("usuarioId") Integer usuarioId, Model model) {
		List<Servicios> listaServicios = serviciosRepository.findAll();
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("listaServicios", listaServicios);
		return "admin/Servicios";
	}

	@GetMapping("/adminListaTransacciones")
	public String listarTransacciones(Model model, @RequestParam Long usuarioId) {
		List<Transaccion> transacciones = transaccionRepository.findAll();
		model.addAttribute("transacciones", transacciones);
		model.addAttribute("usuarioId", usuarioId);
		return "admin/ListaTransacciones";
	}

	@GetMapping("/downloadTransacciones")
	public void downloadTransacciones(@RequestParam Long usuarioId, HttpServletResponse response) throws IOException {
		List<Transaccion> transacciones = transaccionRepository.findAll();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=transacciones.csv");
		StringBuilder csvContent = new StringBuilder();
		csvContent.append(
				"ID Transacción,Nombre del Servicio,Cliente (Usuario Logeado),Abogado (Usuario Seleccionado),Horario,Estado\n");
		for (Transaccion transaccion : transacciones) {
			csvContent.append(transaccion.getId()).append(",");
			csvContent.append(transaccion.getServicio().getNombre()).append(",");
			csvContent.append(transaccion.getUsuarioLogeado().getNombre()).append(",");
			csvContent.append(transaccion.getUsuarioSeleccionado().getNombre()).append(",");
			csvContent.append(transaccion.getHorario()).append(",");
			csvContent.append(transaccion.getEstado()).append("\n");
		}
		response.getWriter().write(csvContent.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
