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
@Document(collection = "tareas")
public class Tarea {

    @Id
    private String id;

    @Indexed
    private String historiaId;

    private String titulo;
    private String descripcion;

    private TipoTarea tipo;

    @Indexed
    private String asignadoId;   // ref Usuario

    private Integer horasEstimadas;
    private Integer horasReales;

    private EstadoTarea estado = EstadoTarea.TODO;

    private LocalDate fechaLimite;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public enum TipoTarea {
        FRONTEND, BACKEND, QA, DEVOPS, ANALISIS, DISENO
    }

    public enum EstadoTarea {
        TODO, IN_PROGRESS, CODE_REVIEW, TESTING, DONE
    }
}
