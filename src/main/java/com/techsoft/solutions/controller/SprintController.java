package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Sprint;
import com.techsoft.solutions.service.ProyectoService;
import com.techsoft.solutions.service.SprintService;
import com.techsoft.solutions.service.HistoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador WEB para Sprints — rutas /sprints/**
 * Separado de SprintRestController (/api/sprints/**) para evitar conflictos.
 */
@Controller
@RequestMapping("/sprints")
public class SprintController {

    @Autowired private SprintService sprintService;
    @Autowired private ProyectoService proyectoService;
    @Autowired private HistoriaService historiaService;

    // ── Listado de sprints de un proyecto ─────────────────
    @GetMapping("/proyecto/{proyectoId}")
    public String listar(@PathVariable String proyectoId, Model model) {
        model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));
        model.addAttribute("sprints", sprintService.listarPorProyecto(proyectoId));
        return "sprints/lista";
    }

    // ── Formulario nuevo sprint ────────────────────────────
    @GetMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String nuevoForm(@PathVariable String proyectoId, Model model) {
        Sprint s = new Sprint();
        s.setProyectoId(proyectoId);
        model.addAttribute("sprint",   s);
        model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId).orElseThrow());
        model.addAttribute("estados",  Sprint.EstadoSprint.values());
        return "sprints/formulario";
    }

    // ── Guardar nuevo sprint ───────────────────────────────
    @PostMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String guardar(@PathVariable String proyectoId,
                          @Valid @ModelAttribute("sprint") Sprint sprint,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proyecto", proyectoService.buscarPorId(proyectoId).orElseThrow());
            model.addAttribute("estados",  Sprint.EstadoSprint.values());
            return "sprints/formulario";
        }
        sprint.setProyectoId(proyectoId);
        sprintService.crear(sprint);
        flash.addFlashAttribute("successMsg", "Sprint creado exitosamente.");
        return "redirect:/sprints/proyecto/" + proyectoId;
    }

    // ── Detalle del sprint ─────────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Sprint sprint = sprintService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));
        model.addAttribute("sprint",   sprint);
        model.addAttribute("proyecto", proyectoService.buscarPorId(sprint.getProyectoId()).orElseThrow());
        model.addAttribute("historias", historiaService.listarPorSprint(id));
        return "sprints/detalle";
    }

    // ── Editar sprint ──────────────────────────────────────
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String editarForm(@PathVariable String id, Model model) {
        Sprint sprint = sprintService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));
        model.addAttribute("sprint",   sprint);
        model.addAttribute("proyecto", proyectoService.buscarPorId(sprint.getProyectoId()).orElseThrow());
        model.addAttribute("estados",  Sprint.EstadoSprint.values());
        return "sprints/formulario";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String editar(@PathVariable String id,
                         @ModelAttribute Sprint datos,
                         RedirectAttributes flash) {
        sprintService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Sprint actualizado.");
        return "redirect:/sprints/" + id;
    }

    // ── Iniciar sprint ─────────────────────────────────────
    @PostMapping("/{id}/iniciar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String iniciar(@PathVariable String id, RedirectAttributes flash) {
        Sprint s = sprintService.iniciar(id);
        flash.addFlashAttribute("successMsg", "Sprint iniciado.");
        return "redirect:/sprints/proyecto/" + s.getProyectoId();
    }

    // ── Completar sprint ───────────────────────────────────
    @PostMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String completar(@PathVariable String id, RedirectAttributes flash) {
        Sprint s = sprintService.completar(id);
        flash.addFlashAttribute("successMsg", "Sprint completado.");
        return "redirect:/sprints/proyecto/" + s.getProyectoId();
    }
}
