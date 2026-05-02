package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Tarea;
import com.techsoft.solutions.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class TareaService {
	@Autowired
    private TareaRepository tareaRepository;
 
    public List<Tarea> listarPorHistoria(String historiaId) {
        return tareaRepository.findByHistoriaId(historiaId);
    }
 
    public List<Tarea> listarMisTareas(String usuarioId) {
        return tareaRepository.findByAsignadoId(usuarioId);
    }
 
    public List<Tarea> listarMisTareasPorEstado(String usuarioId, Tarea.EstadoTarea estado) {
        return tareaRepository.findByAsignadoIdAndEstado(usuarioId, estado);
    }
 
    public Optional<Tarea> buscarPorId(String id) {
        return tareaRepository.findById(id);
    }
 
    public Tarea crear(Tarea tarea) {
        tarea.setEstado(Tarea.EstadoTarea.TODO);
        return tareaRepository.save(tarea);
    }
 
    public Tarea actualizar(String id, Tarea datos) {
        Tarea existente = tareaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        existente.setTitulo(datos.getTitulo());
        existente.setDescripcion(datos.getDescripcion());
        existente.setTipo(datos.getTipo());
        existente.setAsignadoId(datos.getAsignadoId());
        existente.setHorasEstimadas(datos.getHorasEstimadas());
        existente.setHorasReales(datos.getHorasReales());
        existente.setEstado(datos.getEstado());
        existente.setFechaLimite(datos.getFechaLimite());
        return tareaRepository.save(existente);
    }
 
    public Tarea cambiarEstado(String id, Tarea.EstadoTarea nuevoEstado) {
        Tarea t = tareaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        t.setEstado(nuevoEstado);
        return tareaRepository.save(t);
    }
 
    public void eliminar(String id) {
        tareaRepository.deleteById(id);
    }
 
    public long contarTareasEnProgreso(String usuarioId) {
        return tareaRepository.countByAsignadoIdAndEstado(usuarioId, Tarea.EstadoTarea.IN_PROGRESS);
    }
}
