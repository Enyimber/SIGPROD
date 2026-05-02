package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Defecto;
import com.techsoft.solutions.repository.DefectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefectoService {

    @Autowired
    private DefectoRepository defectoRepository;

    public List<Defecto> listarPorProyecto(String proyectoId) {
        return defectoRepository.findByProyectoId(proyectoId);
    }

    public List<Defecto> listarAbiertos(String proyectoId) {
        return defectoRepository.findByProyectoIdAndEstado(proyectoId, Defecto.EstadoDefecto.NUEVO);
    }

    public List<Defecto> listarAsignadosA(String usuarioId) {
        return defectoRepository.findByAsignadoAId(usuarioId);
    }

    public Optional<Defecto> buscarPorId(String id) {
        return defectoRepository.findById(id);
    }

    public Defecto reportar(Defecto defecto) {
        defecto.setEstado(Defecto.EstadoDefecto.NUEVO);
        return defectoRepository.save(defecto);
    }

    public Defecto actualizar(String id, Defecto datos) {
        Defecto existente = defectoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defecto no encontrado: " + id));
        existente.setTitulo(datos.getTitulo());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPasosReproduccion(datos.getPasosReproduccion());
        existente.setSeveridad(datos.getSeveridad());
        existente.setPrioridad(datos.getPrioridad());
        existente.setEstado(datos.getEstado());
        existente.setAsignadoAId(datos.getAsignadoAId());
        existente.setAmbiente(datos.getAmbiente());
        existente.setEvidenciaUrl(datos.getEvidenciaUrl());
        return defectoRepository.save(existente);
    }

    public Defecto cambiarEstado(String id, Defecto.EstadoDefecto nuevoEstado) {
        Defecto d = defectoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Defecto no encontrado: " + id));
        d.setEstado(nuevoEstado);
        return defectoRepository.save(d);
    }

    public void eliminar(String id) {
        defectoRepository.deleteById(id);
    }

    public long contarCriticos(String proyectoId) {
        return defectoRepository.countByProyectoIdAndSeveridad(proyectoId, Defecto.Severidad.CRITICO);
    }

    public long contarAbiertos(String proyectoId) {
        return defectoRepository.countByProyectoIdAndEstado(proyectoId, Defecto.EstadoDefecto.NUEVO);
    }
}
