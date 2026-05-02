package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class UsuarioService {
	@Autowired
    private UsuarioRepository usuarioRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
 
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByActivoTrue();
    }
 
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }
 
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
 
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        }
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        return usuarioRepository.save(usuario);
    }
 
    public Usuario actualizar(String id, Usuario datos) {
        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        existente.setNombres(datos.getNombres());
        existente.setApellidos(datos.getApellidos());
        existente.setTelefono(datos.getTelefono());
        existente.setRolSistema(datos.getRolSistema());
        existente.setActivo(datos.isActivo());
        return usuarioRepository.save(existente);
    }
 
    public void cambiarPassword(String id, String nuevaPassword) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(u);
    }
 
    public void desactivar(String id) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setActivo(false);
        usuarioRepository.save(u);
    }
 
    public List<Usuario> buscarPorRol(Usuario.RolSistema rol) {
        return usuarioRepository.findByRolSistema(rol);
    }
}
