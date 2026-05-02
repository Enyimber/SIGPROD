package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Defecto;
import com.techsoft.solutions.service.DefectoService;
import com.techsoft.solutions.service.ProyectoService;
import com.techsoft.solutions.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador WEB para Defectos — rutas /defectos/**
 * Separado de DefectoRestController (/api/defectos/**) para evitar conflictos.
 */
@Controller
@RequestMapping("/defectos")
public class DefectoController {

    @Autowired private DefectoService  defectoService;
    @Autowired private ProyectoService proyectoService;
    @Autowired private UsuarioService  usuarioService;

    // ── Listado por proyecto ───────────────────────────────
    @GetMapping("/proyecto/{proyectoId}")
    public String listar(@PathVariable String proyectoId, Model model) {
        model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));
        model.addAttribute("defectos", defectoService.listarPorProyecto(proyectoId));
        model.addAttribute("severidades", Defecto.Severidad.values());
        model.addAttribute("estados",     Defecto.EstadoDefecto.values());
        return "defectos/lista";
    }

    // ── Formulario reportar defecto ────────────────────────
    @GetMapping("/proyecto/{proyectoId}/nuevo")
    public String nuevoForm(@PathVariable String proyectoId, Model model, Authentication auth) {
        Defecto d = new Defecto();
        d.setProyectoId(proyectoId);
        usuarioService.buscarPorCorreo(auth.getName()).ifPresent(u -> d.setReportadoPorId(u.getId()));
        model.addAttribute("defecto",    d);
        model.addAttribute("proyecto",   proyectoService.buscarPorId(proyectoId).orElseThrow());
        model.addAttribute("severidades", Defecto.Severidad.values());
        model.addAttribute("prioridades", Defecto.Prioridad.values());
        model.addAttribute("usuarios",    usuarioService.listarActivos());
        return "defectos/formulario";
    }

    @PostMapping("/proyecto/{proyectoId}/nuevo")
    public String reportar(@PathVariable String proyectoId,
                           @Valid @ModelAttribute("defecto") Defecto defecto,
                           BindingResult result,
                           RedirectAttributes flash,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proyecto",    proyectoService.buscarPorId(proyectoId).orElseThrow());
            model.addAttribute("severidades", Defecto.Severidad.values());
            model.addAttribute("prioridades", Defecto.Prioridad.values());
            model.addAttribute("usuarios",    usuarioService.listarActivos());
            return "defectos/formulario";
        }
        defecto.setProyectoId(proyectoId);
        defectoService.reportar(defecto);
        flash.addFlashAttribute("successMsg", "Defecto reportado exitosamente.");
        return "redirect:/defectos/proyecto/" + proyectoId;
    }

    // ── Detalle del defecto ────────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Defecto d = defectoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Defecto no encontrado"));
        model.addAttribute("defecto",     d);
        model.addAttribute("proyecto",    proyectoService.buscarPorId(d.getProyectoId()).orElseThrow());
        model.addAttribute("severidades", Defecto.Severidad.values());
        model.addAttribute("prioridades", Defecto.Prioridad.values());
        model.addAttribute("estados",     Defecto.EstadoDefecto.values());
        model.addAttribute("usuarios",    usuarioService.listarActivos());
        return "defectos/detalle";
    }

    // ── Editar defecto ─────────────────────────────────────
    @PostMapping("/{id}/editar")
    public String editar(@PathVariable String id,
                         @ModelAttribute Defecto datos,
                         RedirectAttributes flash) {
        defectoService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Defecto actualizado.");
        return "redirect:/defectos/" + id;
    }

    // ── Cambiar estado ─────────────────────────────────────
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam Defecto.EstadoDefecto estado,
                                RedirectAttributes flash) {
        Defecto d = defectoService.cambiarEstado(id, estado);
        flash.addFlashAttribute("successMsg", "Estado actualizado.");
        return "redirect:/defectos/" + id;
    }
}
