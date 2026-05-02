package com.techsoft.solutions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "defectos")
public class Defecto {

    @Id
    private String id;

    @Indexed
    private String proyectoId;

    private String sprintId;
    private String titulo;
    private String descripcion;
    private String pasosReproduccion;
    private String ambiente;

    private Severidad severidad;
    private Prioridad prioridad;

    private EstadoDefecto estado = EstadoDefecto.NUEVO;

    @Indexed
    private String reportadoPorId;

    @Indexed
    private String asignadoAId;

    private String evidenciaUrl;

    @CreatedDate
    private LocalDateTime fechaReporte;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public enum Severidad {
        CRITICO, MAYOR, MENOR, TRIVIAL
    }

    public enum Prioridad {
        ALTA, MEDIA, BAJA
    }

    public enum EstadoDefecto {
        NUEVO, ASIGNADO, EN_DESARROLLO, RESUELTO, EN_VERIFICACION, CERRADO, REABIERTO
    }
}
