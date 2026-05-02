package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.Tarea;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TareaRepository extends MongoRepository<Tarea, String>{
	
	List<Tarea> findByHistoriaId(String historiaId);
    List<Tarea> findByAsignadoId(String usuarioId);
    List<Tarea> findByAsignadoIdAndEstado(String usuarioId, Tarea.EstadoTarea estado);
    long countByAsignadoIdAndEstado(String usuarioId, Tarea.EstadoTarea estado);
}
