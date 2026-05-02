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
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historias_usuario")
public class HistoriaUsuario {

    @Id
    private String id;

    @Indexed
    private String proyectoId;

    private String epicaId;

    @Indexed
    private String sprintId;

    private String titulo;
    private String narrativa;  // Como [rol], quiero [acción], para [objetivo]

    private PrioridadMoSCoW prioridadMoscow;

    private Integer estimacionPuntos;
    private Integer valorNegocio;

    private EstadoHistoria estado = EstadoHistoria.BACKLOG;

    private List<CriterioAceptacion> criteriosAceptacion = new ArrayList<>();

    @CreatedDate
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public enum PrioridadMoSCoW {
        MUST, SHOULD, COULD, WONT
    }

    public enum EstadoHistoria {
        BACKLOG, SPRINT, EN_PROGRESO, PARA_ACEPTACION, ACEPTADA, RECHAZADA
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriterioAceptacion {
        private String tipo;   // GIVEN / WHEN / THEN
        private String descripcion;
        private int orden;
    }
}
