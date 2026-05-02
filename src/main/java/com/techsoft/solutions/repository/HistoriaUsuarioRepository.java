package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.HistoriaUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface HistoriaUsuarioRepository extends MongoRepository<HistoriaUsuario, String>{
	List<HistoriaUsuario> findByProyectoId(String proyectoId);
    List<HistoriaUsuario> findBySprintId(String sprintId);
    List<HistoriaUsuario> findByProyectoIdAndEstado(String proyectoId, HistoriaUsuario.EstadoHistoria estado);
    List<HistoriaUsuario> findByEpicaId(String epicaId);
    long countByProyectoIdAndEstado(String proyectoId, HistoriaUsuario.EstadoHistoria estado);
}
