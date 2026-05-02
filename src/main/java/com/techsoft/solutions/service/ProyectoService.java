package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Proyecto;
import com.techsoft.solutions.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class ProyectoService {
	@Autowired
    private ProyectoRepository proyectoRepository;
 
    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }
 
    public List<Proyecto> listarActivos() {
        return proyectoRepository.findByEstado(Proyecto.EstadoProyecto.ACTIVO);
    }
 
    public List<Proyecto> listarPorUsuario(String usuarioId) {
        return proyectoRepository.findByMiembros_UsuarioId(usuarioId);
    }
 
    public Optional<Proyecto> buscarPorId(String id) {
        return proyectoRepository.findById(id);
    }
 
    public Proyecto crear(Proyecto proyecto) {
        proyecto.setEstado(Proyecto.EstadoProyecto.ACTIVO);
        return proyectoRepository.save(proyecto);
    }
 
    public Proyecto actualizar(String id, Proyecto datos) {
        Proyecto existente = proyectoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado: " + id));
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCliente(datos.getCliente());
        existente.setFechaInicio(datos.getFechaInicio());
        existente.setFechaFin(datos.getFechaFin());
        existente.setPresupuesto(datos.getPresupuesto());
        existente.setMetodologia(datos.getMetodologia());
        existente.setEstado(datos.getEstado());
        return proyectoRepository.save(existente);
    }
 
    public void agregarMiembro(String proyectoId, Proyecto.MiembroProyecto miembro) {
        Proyecto p = proyectoRepository.findById(proyectoId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        p.getMiembros().removeIf(m -> m.getUsuarioId().equals(miembro.getUsuarioId()));
        p.getMiembros().add(miembro);
        proyectoRepository.save(p);
    }
 
    public void eliminar(String id) {
        proyectoRepository.deleteById(id);
    }
 
    public long contarActivos() {
        return proyectoRepository.countByEstado(Proyecto.EstadoProyecto.ACTIVO);
    }
}
