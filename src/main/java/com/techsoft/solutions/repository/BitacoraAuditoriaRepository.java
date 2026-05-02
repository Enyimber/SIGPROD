package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.BitacoraAuditoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BitacoraAuditoriaRepository extends MongoRepository<BitacoraAuditoria, String> {
    List<BitacoraAuditoria> findByUsuarioIdOrderByFechaDesc(String usuarioId);
    List<BitacoraAuditoria> findByEntidadAndEntidadIdOrderByFechaDesc(String entidad, String entidadId);
    List<BitacoraAuditoria> findTop50ByOrderByFechaDesc();
}
