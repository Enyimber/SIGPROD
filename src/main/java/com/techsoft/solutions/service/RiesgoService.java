package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Riesgo;
import com.techsoft.solutions.repository.RiesgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class RiesgoService {
	@Autowired
    private RiesgoRepository riesgoRepository;
 
    public List<Riesgo> listarPorProyecto(String proyectoId) {
        return riesgoRepository.findByProyectoId(proyectoId);
    }
 
    public List<Riesgo> listarIdentificados(String proyectoId) {
        return riesgoRepository.findByProyectoIdAndEstado(proyectoId, Riesgo.EstadoRiesgo.IDENTIFICADO);
    }
 
    public Optional<Riesgo> buscarPorId(String id) {
        return riesgoRepository.findById(id);
    }
 
    public Riesgo crear(Riesgo riesgo) {
        riesgo.setEstado(Riesgo.EstadoRiesgo.IDENTIFICADO);
        return riesgoRepository.save(riesgo);
    }
 
    public Riesgo actualizar(String id, Riesgo datos) {
        Riesgo existente = riesgoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Riesgo no encontrado"));
        existente.setDescripcion(datos.getDescripcion());
        existente.setProbabilidad(datos.getProbabilidad());
        existente.setImpacto(datos.getImpacto());
        existente.setPlanMitigacion(datos.getPlanMitigacion());
        existente.setResponsableId(datos.getResponsableId());
        existente.setEstado(datos.getEstado());
        return riesgoRepository.save(existente);
    }
 
    public void eliminar(String id) {
        riesgoRepository.deleteById(id);
    }
}
