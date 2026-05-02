package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Tarea;
import com.techsoft.solutions.service.HistoriaService;
import com.techsoft.solutions.service.TareaService;
import com.techsoft.solutions.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/tareas")
public class TareaController {
	@Autowired private TareaService    tareaService;
    @Autowired private HistoriaService historiaService;
    @Autowired private UsuarioService  usuarioService;
 
    // ── Mis tareas (Kanban personal) ───────────────────────
    @GetMapping("/mis-tareas")
    public String misTareas(Authentication auth, Model model) {
        String correo = auth.getName();
        String usuarioId = usuarioService.buscarPorCorreo(correo)
            .orElseThrow().getId();
 
        model.addAttribute("todo",       tareaService.listarMisTareasPorEstado(usuarioId, Tarea.EstadoTarea.TODO));
        model.addAttribute("enProgreso", tareaService.listarMisTareasPorEstado(usuarioId, Tarea.EstadoTarea.IN_PROGRESS));
        model.addAttribute("revision",   tareaService.listarMisTareasPorEstado(usuarioId, Tarea.EstadoTarea.CODE_REVIEW));
        model.addAttribute("testing",    tareaService.listarMisTareasPorEstado(usuarioId, Tarea.EstadoTarea.TESTING));
        model.addAttribute("done",       tareaService.listarMisTareasPorEstado(usuarioId, Tarea.EstadoTarea.DONE));
        return "tareas/kanban";
    }
 
    // ── Tareas de una historia ─────────────────────────────
    @GetMapping("/historia/{historiaId}")
    public String tareasPorHistoria(@PathVariable String historiaId, Model model) {
        model.addAttribute("tareas",   tareaService.listarPorHistoria(historiaId));
        model.addAttribute("historia", historiaService.buscarPorId(historiaId).orElseThrow());
        model.addAttribute("tipos",    Tarea.TipoTarea.values());
        model.addAttribute("estados",  Tarea.EstadoTarea.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());
        return "tareas/lista";
    }
 
    // ── Nueva tarea ────────────────────────────────────────
    @GetMapping("/historia/{historiaId}/nueva")
    public String nuevaForm(@PathVariable String historiaId, Model model) {
        Tarea t = new Tarea();
        t.setHistoriaId(historiaId);
        model.addAttribute("tarea",    t);
        model.addAttribute("tipos",    Tarea.TipoTarea.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());
        model.addAttribute("historia", historiaService.buscarPorId(historiaId).orElseThrow());
        return "tareas/formulario";
    }
 
    @PostMapping("/historia/{historiaId}/nueva")
    public String guardar(@PathVariable String historiaId,
                          @Valid @ModelAttribute("tarea") Tarea tarea,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tipos",    Tarea.TipoTarea.values());
            model.addAttribute("usuarios", usuarioService.listarActivos());
            return "tareas/formulario";
        }
        tarea.setHistoriaId(historiaId);
        tareaService.crear(tarea);
        flash.addFlashAttribute("successMsg", "Tarea creada.");
        return "redirect:/tareas/historia/" + historiaId;
    }
 
    // ── Cambiar estado (AJAX-friendly redirect) ────────────
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam Tarea.EstadoTarea estado,
                                @RequestParam(required = false) String origen,
                                RedirectAttributes flash) {
        tareaService.cambiarEstado(id, estado);
        flash.addFlashAttribute("successMsg", "Estado actualizado.");
        return "redirect:" + (origen != null ? origen : "/tareas/mis-tareas");
    }
 
    // ── Detalle ────────────────────────────────────────────
    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Tarea t = tareaService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        model.addAttribute("tarea",    t);
        model.addAttribute("tipos",    Tarea.TipoTarea.values());
        model.addAttribute("estados",  Tarea.EstadoTarea.values());
        model.addAttribute("usuarios", usuarioService.listarActivos());
        model.addAttribute("historia", historiaService.buscarPorId(t.getHistoriaId()).orElseThrow());
        return "tareas/detalle";
    }
 
    // ── Editar ─────────────────────────────────────────────
    @PostMapping("/{id}/editar")
    public String editar(@PathVariable String id,
                         @ModelAttribute Tarea datos,
                         RedirectAttributes flash) {
        tareaService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Tarea actualizada.");
        return "redirect:/tareas/" + id;
    }
 
    // ── Eliminar ───────────────────────────────────────────
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        Tarea t = tareaService.buscarPorId(id).orElseThrow();
        String historiaId = t.getHistoriaId();
        tareaService.eliminar(id);
        flash.addFlashAttribute("successMsg", "Tarea eliminada.");
        return "redirect:/tareas/historia/" + historiaId;
    }
}
