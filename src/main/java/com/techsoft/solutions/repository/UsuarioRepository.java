package com.techsoft.solutions.repository;

import com.techsoft.solutions.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    List<Usuario> findByActivoTrue();
    List<Usuario> findByRolSistema(Usuario.RolSistema rol);
}
