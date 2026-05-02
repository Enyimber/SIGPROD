package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.Riesgo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RiesgoRepository extends MongoRepository<Riesgo, String>{
	List<Riesgo> findByProyectoId(String proyectoId);
    List<Riesgo> findByProyectoIdAndEstado(String proyectoId, Riesgo.EstadoRiesgo estado);
}
