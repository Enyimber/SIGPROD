package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Riesgo;
import com.techsoft.solutions.service.ProyectoService;
import com.techsoft.solutions.service.RiesgoService;
import com.techsoft.solutions.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador WEB para Riesgos — rutas /riesgos/**
 * Separado de RiesgoRestController (/api/riesgos/**) para evitar conflictos.
 */
@Controller
@RequestMapping("/riesgos")
public class RiesgoController {

    @Autowired private RiesgoService   riesgoService;
    @Autowired private ProyectoService proyectoService;
    @Autowired private UsuarioService  usuarioService;

    // ── Listado por proyecto ───────────────────────────────
    @GetMapping("/proyecto/{proyectoId}")
    public String listar(@PathVariable String proyectoId, Model model) {
        model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));
        model.addAttribute("riesgos",  riesgoService.listarPorProyecto(proyectoId));
        return "riesgos/lista";
    }

    // ── Formulario nuevo riesgo ────────────────────────────
    @GetMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','ANALISTA')")
    public String nuevoForm(@PathVariable String proyectoId, Model model) {
        Riesgo r = new Riesgo();
        r.setProyectoId(proyectoId);
        model.addAttribute("riesgo",   r);
        model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId).orElseThrow());
        model.addAttribute("estados",  Riesgo.EstadoRiesgo.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());
        return "riesgos/formulario";
    }

    @PostMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','ANALISTA')")
    public String guardar(@PathVariable String proyectoId,
                          @Valid @ModelAttribute("riesgo") Riesgo riesgo,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId).orElseThrow());
            model.addAttribute("estados",  Riesgo.EstadoRiesgo.values());
            model.addAttribute("usuarios", usuarioService.listarActivos());
            return "riesgos/formulario";
        }
        riesgo.setProyectoId(proyectoId);
        riesgoService.crear(riesgo);
        flash.addFlashAttribute("successMsg", "Riesgo registrado.");
        return "redirect:/riesgos/proyecto/" + proyectoId;
    }

    // ── Detalle / editar riesgo ────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Riesgo r = riesgoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Riesgo no encontrado"));
        model.addAttribute("riesgo",   r);
        model.addAttribute("proyecto", proyectoService.buscarPorId(r.getProyectoId()).orElseThrow());
        model.addAttribute("estados",  Riesgo.EstadoRiesgo.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());
        return "riesgos/detalle";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','ANALISTA')")
    public String editar(@PathVariable String id,
                         @ModelAttribute Riesgo datos,
                         RedirectAttributes flash) {
        riesgoService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Riesgo actualizado.");
        return "redirect:/riesgos/" + id;
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        Riesgo r = riesgoService.buscarPorId(id).orElseThrow();
        String proyectoId = r.getProyectoId();
        riesgoService.eliminar(id);
        flash.addFlashAttribute("successMsg", "Riesgo eliminado.");
        return "redirect:/riesgos/proyecto/" + proyectoId;
    }
    
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','ANALISTA')")
    public String editarForm(@PathVariable String id, Model model) {

        Riesgo r = riesgoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Riesgo no encontrado"));

        model.addAttribute("riesgo", r);

        model.addAttribute("proyecto",
            proyectoService.buscarPorId(r.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));

        model.addAttribute("estados", Riesgo.EstadoRiesgo.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());

        return "riesgos/formulario";
    }
}
