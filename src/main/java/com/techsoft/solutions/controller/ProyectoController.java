package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Proyecto;
import com.techsoft.solutions.model.Riesgo;
import com.techsoft.solutions.model.Sprint;
import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/proyectos")
public class ProyectoController {
	@Autowired private ProyectoService proyectoService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private SprintService sprintService;
    @Autowired private RiesgoService riesgoService;
    @Autowired private HistoriaService historiaService;
 
    // ── Listado ────────────────────────────────────────────
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proyectos", proyectoService.listarTodos());
        return "proyectos/lista";
    }
 
    // ── Detalle ────────────────────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
 
        model.addAttribute("proyecto",   proyecto);
        model.addAttribute("sprints",    sprintService.listarPorProyecto(id));
        model.addAttribute("riesgos",    riesgoService.listarPorProyecto(id));
        model.addAttribute("historias",  historiaService.listarPorProyecto(id));
        model.addAttribute("usuarios",   usuarioService.listarActivos());
        return "proyectos/detalle";
    }
 
    // ── Formulario nuevo ───────────────────────────────────
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String nuevoForm(Model model) {
        model.addAttribute("proyecto",     new Proyecto());
        model.addAttribute("metodologias", Proyecto.Metodologia.values());
        model.addAttribute("usuarios",     usuarioService.listarActivos());
        return "proyectos/formulario";
    }
 
    // ── Guardar nuevo ──────────────────────────────────────
    @PostMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String guardar(@Valid @ModelAttribute("proyecto") Proyecto proyecto,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("metodologias", Proyecto.Metodologia.values());
            model.addAttribute("usuarios",     usuarioService.listarActivos());
            return "proyectos/formulario";
        }
        Proyecto guardado = proyectoService.crear(proyecto);
        flash.addFlashAttribute("successMsg", "Proyecto creado exitosamente.");
        return "redirect:/proyectos/" + guardado.getId();
    }
 
    // ── Formulario editar ──────────────────────────────────
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String editarForm(@PathVariable String id, Model model) {
        model.addAttribute("proyecto",     proyectoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));
        model.addAttribute("metodologias", Proyecto.Metodologia.values());
        model.addAttribute("estados",      Proyecto.EstadoProyecto.values());
        model.addAttribute("usuarios",     usuarioService.listarActivos());
        return "proyectos/formulario";
    }
 
    // ── Guardar edición ────────────────────────────────────
    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String guardarEdicion(@PathVariable String id,
                                 @Valid @ModelAttribute("proyecto") Proyecto proyecto,
                                 BindingResult result,
                                 RedirectAttributes flash,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("metodologias", Proyecto.Metodologia.values());
            model.addAttribute("estados",      Proyecto.EstadoProyecto.values());
            model.addAttribute("usuarios",     usuarioService.listarActivos());
            return "proyectos/formulario";
        }
        proyectoService.actualizar(id, proyecto);
        flash.addFlashAttribute("successMsg", "Proyecto actualizado.");
        return "redirect:/proyectos/" + id;
    }
 
    // ── Eliminar ───────────────────────────────────────────
    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        proyectoService.eliminar(id);
        flash.addFlashAttribute("successMsg", "Proyecto eliminado.");
        return "redirect:/proyectos";
    }
 
    // ── Agregar miembro ────────────────────────────────────
    @PostMapping("/{id}/miembros")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public String agregarMiembro(@PathVariable String id,
                                 @ModelAttribute Proyecto.MiembroProyecto miembro,
                                 RedirectAttributes flash) {
        proyectoService.agregarMiembro(id, miembro);
        flash.addFlashAttribute("successMsg", "Miembro agregado al proyecto.");
        return "redirect:/proyectos/" + id;
    }
}
