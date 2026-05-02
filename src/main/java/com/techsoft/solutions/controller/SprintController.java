package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Sprint;
import com.techsoft.solutions.service.HistoriaService;
import com.techsoft.solutions.service.ProyectoService;
import com.techsoft.solutions.service.SprintService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador WEB para Sprints
 * Funciona con o sin server.servlet.context-path
 *
 * Si NO usas context-path:
 * http://localhost:8201/sprints/...
 *
 * Si usas /sigprod:
 * http://localhost:8201/sigprod/sprints/...
 */
@Controller
@RequestMapping("/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private HistoriaService historiaService;

    // =====================================================
    // LISTAR SPRINTS DE UN PROYECTO
    // =====================================================
    @GetMapping("/proyecto/{proyectoId}")
    public String listar(@PathVariable String proyectoId, Model model) {

        model.addAttribute("proyecto",
                proyectoService.buscarPorId(proyectoId)
                        .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));

        model.addAttribute("sprints",
                sprintService.listarPorProyecto(proyectoId));

        return "sprints/lista";
    }

    // =====================================================
    // FORMULARIO NUEVO SPRINT
    // =====================================================
    @GetMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String nuevoForm(@PathVariable String proyectoId, Model model) {

        Sprint sprint = new Sprint();
        sprint.setProyectoId(proyectoId);

        model.addAttribute("sprint", sprint);
        model.addAttribute("proyecto",
                proyectoService.buscarPorId(proyectoId)
                        .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));

        model.addAttribute("estados", Sprint.EstadoSprint.values());

        return "sprints/formulario";
    }

    // =====================================================
    // GUARDAR NUEVO SPRINT
    // =====================================================
    @PostMapping("/proyecto/{proyectoId}/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String guardar(
            @PathVariable String proyectoId,
            @Valid @ModelAttribute("sprint") Sprint sprint,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("proyecto",
                    proyectoService.buscarPorId(proyectoId).orElseThrow());
            model.addAttribute("estados", Sprint.EstadoSprint.values());

            return "sprints/formulario";
        }

        sprint.setProyectoId(proyectoId);
        sprintService.crear(sprint);

        flash.addFlashAttribute("successMsg",
                "Sprint creado exitosamente");

        return "redirect:/sprints/proyecto/" + proyectoId;
    }

    // =====================================================
    // DETALLE
    // =====================================================
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {

        Sprint sprint = sprintService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));

        model.addAttribute("sprint", sprint);

        model.addAttribute("proyecto",
                proyectoService.buscarPorId(sprint.getProyectoId())
                        .orElseThrow());

        model.addAttribute("historias",
                historiaService.listarPorSprint(id));

        return "sprints/detalle";
    }

    // =====================================================
    // FORMULARIO EDITAR
    // =====================================================
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String editarForm(@PathVariable String id, Model model) {

        Sprint sprint = sprintService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));

        model.addAttribute("sprint", sprint);

        model.addAttribute("proyecto",
                proyectoService.buscarPorId(sprint.getProyectoId())
                        .orElseThrow());

        model.addAttribute("estados",
                Sprint.EstadoSprint.values());

        return "sprints/formulario";
    }

    // =====================================================
    // GUARDAR EDICIÓN
    // =====================================================
    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String editar(
            @PathVariable String id,
            @ModelAttribute Sprint datos,
            RedirectAttributes flash) {

        sprintService.actualizar(id, datos);

        flash.addFlashAttribute("successMsg",
                "Sprint actualizado correctamente");

        return "redirect:/sprints/" + id;
    }

    // =====================================================
    // INICIAR
    // =====================================================
    @PostMapping("/{id}/iniciar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String iniciar(
            @PathVariable String id,
            RedirectAttributes flash) {

        Sprint sprint = sprintService.iniciar(id);

        flash.addFlashAttribute("successMsg",
                "Sprint iniciado correctamente");

        return "redirect:/sprints/proyecto/" + sprint.getProyectoId();
    }

    // =====================================================
    // COMPLETAR
    // =====================================================
    @PostMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String completar(
            @PathVariable String id,
            RedirectAttributes flash) {

        Sprint sprint = sprintService.completar(id);

        flash.addFlashAttribute("successMsg",
                "Sprint completado correctamente");

        return "redirect:/sprints/proyecto/" + sprint.getProyectoId();
    }
}