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
@Document(collection = "riesgos")
public class Riesgo {

    @Id
    private String id;

    @Indexed
    private String proyectoId;

    private String descripcion;
    private Integer probabilidad;   // 1-5
    private Integer impacto;        // 1-5
    private String planMitigacion;

    @Indexed
    private String responsableId;

    private EstadoRiesgo estado = EstadoRiesgo.IDENTIFICADO;

    @CreatedDate
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public enum EstadoRiesgo {
        IDENTIFICADO, MITIGADO, MATERIALIZADO, CERRADO
    }

    /** Criticidad = probabilidad × impacto (1-25) */
    public int getCriticidad() {
        if (probabilidad == null || impacto == null) return 0;
        return probabilidad * impacto;
    }
}
