package com.techsoft.solutions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sprints")
public class Sprint {

    @Id
    private String id;

    @Indexed
    private String proyectoId;

    private Integer numero;
    private String objetivo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Integer capacidadHoras;

    private EstadoSprint estado = EstadoSprint.PLANIFICADO;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public enum EstadoSprint {
        PLANIFICADO, ACTIVO, COMPLETADO, CANCELADO
    }
}
