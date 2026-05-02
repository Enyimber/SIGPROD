package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProyectoRepository extends MongoRepository<Proyecto, String>{
	List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado);
    List<Proyecto> findByProductOwnerIdOrProjectManagerId(String poId, String pmId);
    List<Proyecto> findByMiembros_UsuarioId(String usuarioId);
    long countByEstado(Proyecto.EstadoProyecto estado);
}
