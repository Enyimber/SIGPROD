package com.techsoft.solutions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "proyectos")

public class Proyecto {
	@Id
    private String id;
 
    private String nombre;
    private String descripcion;
 
    /** Nombre del cliente */
    private String cliente;
 
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
 
    private Double presupuesto;
 
    private Metodologia metodologia;
 
    private EstadoProyecto estado = EstadoProyecto.ACTIVO;
    private String productOwnerId;
    private String projectManagerId;
    private List<MiembroProyecto> miembros = new ArrayList<>();
 
    @CreatedDate
    private LocalDateTime fechaCreacion;
 
    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
 
    public enum Metodologia {
        SCRUM, KANBAN, CASCADA
    }
 
    public enum EstadoProyecto {
        ACTIVO, PAUSADO, COMPLETADO, CANCELADO
    }
 
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MiembroProyecto {
        private String usuarioId;
        private String rolEnProyecto;
        private Integer porcentajeDedicacion;
        private Double tarifaHora;
    }
}
