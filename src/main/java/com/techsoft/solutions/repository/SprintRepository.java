package com.techsoft.solutions.repository;
import com.techsoft.solutions.model.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SprintRepository extends MongoRepository<Sprint, String>{
	List<Sprint> findByProyectoIdOrderByNumeroAsc(String proyectoId);
    Optional<Sprint> findByProyectoIdAndEstado(String proyectoId, Sprint.EstadoSprint estado);
    long countByProyectoId(String proyectoId);
}
