package com.techsoft.solutions.controller.api;

import com.techsoft.solutions.model.Proyecto;
import com.techsoft.solutions.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoRestController {

    @Autowired private ProyectoService proyectoService;

    @GetMapping
    public ResponseEntity<List<Proyecto>> listar() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proyecto>> activos() {
        return ResponseEntity.ok(proyectoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtener(@PathVariable String id) {
        return proyectoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Proyecto> crear(@RequestBody Proyecto proyecto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crear(proyecto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable String id, @RequestBody Proyecto datos) {
        try {
            return ResponseEntity.ok(proyectoService.actualizar(id, datos));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/miembros")
    public ResponseEntity<Void> agregarMiembro(@PathVariable String id,
                                                @RequestBody Proyecto.MiembroProyecto miembro) {
        proyectoService.agregarMiembro(id, miembro);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
