package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.HistoriaUsuario;
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
 
@Controller
@RequestMapping("/backlog")
public class BacklogController {
	@Autowired private HistoriaService historiaService;
    @Autowired private ProyectoService proyectoService;
    @Autowired private SprintService   sprintService;
 
    // ── Backlog del proyecto ───────────────────────────────
    @GetMapping("/{proyectoId}")
    public String backlog(@PathVariable String proyectoId, Model model) {
        model.addAttribute("proyecto",  proyectoService.buscarPorId(proyectoId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado")));
        model.addAttribute("historias", historiaService.listarPorProyecto(proyectoId));
        model.addAttribute("sprints",   sprintService.listarPorProyecto(proyectoId));
        return "backlog/lista";
    }
 
    // ── Nueva historia ─────────────────────────────────────
    @GetMapping("/{proyectoId}/nueva")
    public String nuevaForm(@PathVariable String proyectoId, Model model) {
        HistoriaUsuario h = new HistoriaUsuario();
        h.setProyectoId(proyectoId);
        model.addAttribute("historia",   h);
        model.addAttribute("prioridades", HistoriaUsuario.PrioridadMoSCoW.values());
        model.addAttribute("proyecto",   proyectoService.buscarPorId(proyectoId).orElseThrow());
        return "backlog/formulario";
    }
 
    @PostMapping("/{proyectoId}/nueva")
    public String guardar(@PathVariable String proyectoId,
                          @Valid @ModelAttribute("historia") HistoriaUsuario historia,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("prioridades", HistoriaUsuario.PrioridadMoSCoW.values());
            return "backlog/formulario";
        }
        historia.setProyectoId(proyectoId);
        historiaService.crear(historia);
        flash.addFlashAttribute("successMsg", "Historia de usuario creada.");
        return "redirect:/backlog/" + proyectoId;
    }
 
    // ── Detalle / editar historia ──────────────────────────
    @GetMapping("/historia/{id}")
    public String detalle(@PathVariable String id, Model model) {
        HistoriaUsuario h = historiaService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        model.addAttribute("historia",   h);
        model.addAttribute("prioridades", HistoriaUsuario.PrioridadMoSCoW.values());
        model.addAttribute("estados",    HistoriaUsuario.EstadoHistoria.values());
        model.addAttribute("proyecto",   proyectoService.buscarPorId(h.getProyectoId()).orElseThrow());
        return "backlog/detalle";
    }
 
    @PostMapping("/historia/{id}/editar")
    public String editar(@PathVariable String id,
                         @ModelAttribute HistoriaUsuario datos,
                         RedirectAttributes flash) {
        HistoriaUsuario actualizada = historiaService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Historia actualizada.");
        return "redirect:/backlog/historia/" + id;
    }
 
    // ── Asignar a sprint ───────────────────────────────────
    @PostMapping("/historia/{id}/sprint")
    public String asignarSprint(@PathVariable String id,
                                @RequestParam String sprintId,
                                RedirectAttributes flash) {
        HistoriaUsuario h = historiaService.asignarASprint(id, sprintId);
        flash.addFlashAttribute("successMsg", "Historia asignada al sprint.");
        return "redirect:/backlog/" + h.getProyectoId();
    }
 
    // ── Aceptar / rechazar (Product Owner) ────────────────
    @PostMapping("/historia/{id}/aceptar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String aceptar(@PathVariable String id, RedirectAttributes flash) {
        HistoriaUsuario h = historiaService.cambiarEstado(id, HistoriaUsuario.EstadoHistoria.ACEPTADA);
        flash.addFlashAttribute("successMsg", "Historia aceptada.");
        return "redirect:/backlog/" + h.getProyectoId() + "#aceptacion";
    }
 
    @PostMapping("/historia/{id}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    public String rechazar(@PathVariable String id, RedirectAttributes flash) {
        HistoriaUsuario h = historiaService.cambiarEstado(id, HistoriaUsuario.EstadoHistoria.RECHAZADA);
        flash.addFlashAttribute("warningMsg", "Historia rechazada.");
        return "redirect:/backlog/" + h.getProyectoId() + "#aceptacion";
    }
 
    // ── Eliminar ───────────────────────────────────────────
    @PostMapping("/historia/{id}/eliminar")
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','PROJECT_MANAGER')")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        HistoriaUsuario h = historiaService.buscarPorId(id).orElseThrow();
        String proyectoId = h.getProyectoId();
        historiaService.eliminar(id);
        flash.addFlashAttribute("successMsg", "Historia eliminada.");
        return "redirect:/backlog/" + proyectoId;
    }
}
