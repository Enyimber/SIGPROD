package com.techsoft.solutions.service;
import com.techsoft.solutions.model.Sprint;
import com.techsoft.solutions.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class SprintService {
	@Autowired
    private SprintRepository sprintRepository;
 
    public List<Sprint> listarPorProyecto(String proyectoId) {
        return sprintRepository.findByProyectoIdOrderByNumeroAsc(proyectoId);
    }
 
    public Optional<Sprint> sprintActivo(String proyectoId) {
        return sprintRepository.findByProyectoIdAndEstado(proyectoId, Sprint.EstadoSprint.ACTIVO);
    }
 
    public Sprint crear(Sprint sprint) {
        long total = sprintRepository.countByProyectoId(sprint.getProyectoId());
        sprint.setNumero((int) total + 1);
        sprint.setEstado(Sprint.EstadoSprint.PLANIFICADO);
        return sprintRepository.save(sprint);
    }
 
    public Sprint actualizar(String id, Sprint datos) {
        Sprint existente = sprintRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));
        existente.setObjetivo(datos.getObjetivo());
        existente.setFechaInicio(datos.getFechaInicio());
        existente.setFechaFin(datos.getFechaFin());
        existente.setCapacidadHoras(datos.getCapacidadHoras());
        existente.setEstado(datos.getEstado());
        return sprintRepository.save(existente);
    }
 
    public Sprint iniciar(String id) {
        Sprint s = sprintRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));
        s.setEstado(Sprint.EstadoSprint.ACTIVO);
        return sprintRepository.save(s);
    }
 
    public Sprint completar(String id) {
        Sprint s = sprintRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sprint no encontrado"));
        s.setEstado(Sprint.EstadoSprint.COMPLETADO);
        return sprintRepository.save(s);
    }
 
    public Optional<Sprint> buscarPorId(String id) {
        return sprintRepository.findById(id);
    }
}
