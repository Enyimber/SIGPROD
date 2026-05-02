package com.techsoft.solutions.service;

import com.techsoft.solutions.model.HistoriaUsuario;
import com.techsoft.solutions.repository.HistoriaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class HistoriaService {
	@Autowired
    private HistoriaUsuarioRepository historiaRepository;
 
    public List<HistoriaUsuario> listarPorProyecto(String proyectoId) {
        return historiaRepository.findByProyectoId(proyectoId);
    }
 
    public List<HistoriaUsuario> listarBacklog(String proyectoId) {
        return historiaRepository.findByProyectoIdAndEstado(
            proyectoId, HistoriaUsuario.EstadoHistoria.BACKLOG);
    }
 
    public List<HistoriaUsuario> listarPorSprint(String sprintId) {
        return historiaRepository.findBySprintId(sprintId);
    }
 
    public List<HistoriaUsuario> listarParaAceptacion(String proyectoId) {
        return historiaRepository.findByProyectoIdAndEstado(
            proyectoId, HistoriaUsuario.EstadoHistoria.PARA_ACEPTACION);
    }
 
    public Optional<HistoriaUsuario> buscarPorId(String id) {
        return historiaRepository.findById(id);
    }
 
    public HistoriaUsuario crear(HistoriaUsuario historia) {
        historia.setEstado(HistoriaUsuario.EstadoHistoria.BACKLOG);
        return historiaRepository.save(historia);
    }
 
    public HistoriaUsuario actualizar(String id, HistoriaUsuario datos) {
        HistoriaUsuario existente = historiaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        existente.setTitulo(datos.getTitulo());
        existente.setNarrativa(datos.getNarrativa());
        existente.setPrioridadMoscow(datos.getPrioridadMoscow());
        existente.setEstimacionPuntos(datos.getEstimacionPuntos());
        existente.setValorNegocio(datos.getValorNegocio());
        existente.setCriteriosAceptacion(datos.getCriteriosAceptacion());
        return historiaRepository.save(existente);
    }
 
    public HistoriaUsuario asignarASprint(String historiaId, String sprintId) {
        HistoriaUsuario h = historiaRepository.findById(historiaId)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        h.setSprintId(sprintId);
        h.setEstado(HistoriaUsuario.EstadoHistoria.SPRINT);
        return historiaRepository.save(h);
    }
 
    public HistoriaUsuario cambiarEstado(String id, HistoriaUsuario.EstadoHistoria nuevoEstado) {
        HistoriaUsuario h = historiaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Historia no encontrada"));
        h.setEstado(nuevoEstado);
        return historiaRepository.save(h);
    }
 
    public void eliminar(String id) {
        historiaRepository.deleteById(id);
    }
 
    public long contarAceptadas(String proyectoId) {
        return historiaRepository.countByProyectoIdAndEstado(
            proyectoId, HistoriaUsuario.EstadoHistoria.ACEPTADA);
    }
}
