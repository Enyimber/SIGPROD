package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.Defecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DefectoRepository extends MongoRepository<Defecto, String> {
    List<Defecto> findByProyectoId(String proyectoId);
    List<Defecto> findByProyectoIdAndEstado(String proyectoId, Defecto.EstadoDefecto estado);
    List<Defecto> findByAsignadoAId(String usuarioId);
    long countByProyectoIdAndEstado(String proyectoId, Defecto.EstadoDefecto estado);
    long countByProyectoIdAndSeveridad(String proyectoId, Defecto.Severidad severidad);
}
