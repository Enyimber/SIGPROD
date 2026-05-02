package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.Riesgo;
import com.techsoft.solutions.service.RiesgoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/riesgos")
public class RiesgoRestController {

    @Autowired private RiesgoService riesgoService;

    @GetMapping
    public ResponseEntity<List<Riesgo>> listar(@RequestParam(required = false) String proyectoId) {
        if (proyectoId != null) return ResponseEntity.ok(riesgoService.listarPorProyecto(proyectoId));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Riesgo> obtener(@PathVariable String id) {
        return riesgoService.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Riesgo> crear(@RequestBody Riesgo riesgo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(riesgoService.crear(riesgo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Riesgo> actualizar(@PathVariable String id, @RequestBody Riesgo datos) {
        try {
            return ResponseEntity.ok(riesgoService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        riesgoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
