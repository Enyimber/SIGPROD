package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.Sprint;
import com.techsoft.solutions.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sprints")
public class SprintRestController {

    @Autowired private SprintService sprintService;

    @GetMapping
    public ResponseEntity<List<Sprint>> listar(@RequestParam String proyectoId) {
        return ResponseEntity.ok(sprintService.listarPorProyecto(proyectoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> obtener(@PathVariable String id) {
        return sprintService.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activo/{proyectoId}")
    public ResponseEntity<Sprint> activo(@PathVariable String proyectoId) {
        return sprintService.sprintActivo(proyectoId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sprint> crear(@RequestBody Sprint sprint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.crear(sprint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sprint> actualizar(@PathVariable String id, @RequestBody Sprint datos) {
        try {
            return ResponseEntity.ok(sprintService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Sprint> iniciar(@PathVariable String id) {
        return ResponseEntity.ok(sprintService.iniciar(id));
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Sprint> completar(@PathVariable String id) {
        return ResponseEntity.ok(sprintService.completar(id));
    }
}
