package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.Tarea;
import com.techsoft.solutions.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaRestController {

    @Autowired private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> listar(
            @RequestParam(required = false) String historiaId,
            @RequestParam(required = false) String usuarioId) {
        if (historiaId != null) return ResponseEntity.ok(tareaService.listarPorHistoria(historiaId));
        if (usuarioId != null) return ResponseEntity.ok(tareaService.listarMisTareas(usuarioId));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtener(@PathVariable String id) {
        return tareaService.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(tarea));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable String id, @RequestBody Tarea datos) {
        try {
            return ResponseEntity.ok(tareaService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Tarea> cambiarEstado(@PathVariable String id,
                                                @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(tareaService.cambiarEstado(id,
                    Tarea.EstadoTarea.valueOf(body.get("estado"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
