package com.techsoft.solutions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bitacora_auditoria")
public class BitacoraAuditoria {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String accion;        // CREATE, UPDATE, DELETE, LOGIN, etc.
    private String entidad;       // Proyecto, Historia, Tarea, etc.
    private String entidadId;

    private String datosAnteriores; // JSON string
    private String datosNuevos;     // JSON string

    private String ip;
    private String descripcion;

    @CreatedDate
    private LocalDateTime fecha;
}
