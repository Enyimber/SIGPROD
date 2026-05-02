package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired private ProyectoService proyectoService;
    @Autowired private UsuarioService  usuarioService;
    @Autowired private TareaService    tareaService;
    @Autowired private DefectoService  defectoService;

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        Usuario usuario = usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + auth.getName()));

        // KPIs globales
        model.addAttribute("totalProyectos",   proyectoService.contarActivos());
        model.addAttribute("totalUsuarios",    usuarioService.listarActivos().size());
        model.addAttribute("misProyectos",     proyectoService.listarPorUsuario(usuario.getId()));
        model.addAttribute("misTareas",        tareaService.listarMisTareas(usuario.getId()));
        model.addAttribute("tareasEnProgreso", tareaService.contarTareasEnProgreso(usuario.getId()));
        model.addAttribute("usuario",          usuario);

        return "dashboard/index";
    }
}
