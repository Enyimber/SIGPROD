package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.HistoriaUsuario;
import com.techsoft.solutions.service.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historias")
public class HistoriaRestController {

    @Autowired private HistoriaService historiaService;

    @GetMapping
    public ResponseEntity<List<HistoriaUsuario>> listar(
            @RequestParam(required = false) String proyectoId,
            @RequestParam(required = false) String sprintId) {
        if (sprintId != null) return ResponseEntity.ok(historiaService.listarPorSprint(sprintId));
        if (proyectoId != null) return ResponseEntity.ok(historiaService.listarPorProyecto(proyectoId));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaUsuario> obtener(@PathVariable String id) {
        return historiaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/backlog/{proyectoId}")
    public ResponseEntity<List<HistoriaUsuario>> backlog(@PathVariable String proyectoId) {
        return ResponseEntity.ok(historiaService.listarBacklog(proyectoId));
    }

    @PostMapping
    public ResponseEntity<HistoriaUsuario> crear(@RequestBody HistoriaUsuario historia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historiaService.crear(historia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaUsuario> actualizar(@PathVariable String id,
                                                       @RequestBody HistoriaUsuario datos) {
        try {
            return ResponseEntity.ok(historiaService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<HistoriaUsuario> cambiarEstado(@PathVariable String id,
                                                          @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(historiaService.cambiarEstado(id,
                    HistoriaUsuario.EstadoHistoria.valueOf(body.get("estado"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/sprint")
    public ResponseEntity<HistoriaUsuario> asignarSprint(@PathVariable String id,
                                                          @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(historiaService.asignarASprint(id, body.get("sprintId")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        historiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
