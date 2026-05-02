package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.Defecto;
import com.techsoft.solutions.service.DefectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/defectos")
public class DefectoRestController {

    @Autowired private DefectoService defectoService;

    @GetMapping
    public ResponseEntity<List<Defecto>> listar(
            @RequestParam(required = false) String proyectoId,
            @RequestParam(required = false) String usuarioId) {
        if (proyectoId != null) return ResponseEntity.ok(defectoService.listarPorProyecto(proyectoId));
        if (usuarioId != null) return ResponseEntity.ok(defectoService.listarAsignadosA(usuarioId));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Defecto> obtener(@PathVariable String id) {
        return defectoService.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Defecto> reportar(@RequestBody Defecto defecto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(defectoService.reportar(defecto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Defecto> actualizar(@PathVariable String id, @RequestBody Defecto datos) {
        try {
            return ResponseEntity.ok(defectoService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Defecto> cambiarEstado(@PathVariable String id,
                                                  @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(defectoService.cambiarEstado(id,
                    Defecto.EstadoDefecto.valueOf(body.get("estado"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
