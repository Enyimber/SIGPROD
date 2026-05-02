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
import java.util.List;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class Usuario {
	@Id
    private String id;
 
    private String nombres;
    private String apellidos;
 
    @Indexed(unique = true)
    private String correo;
 
    private String passwordHash;
    private String telefono;
    private String fotoUrl;
    
    private RolSistema rolSistema;
 
    private boolean activo = true;
 
    @CreatedDate
    private LocalDateTime fechaCreacion;
 
    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
 
    public enum RolSistema {
        ADMIN,
        PRODUCT_OWNER,
        PROJECT_MANAGER,
        ANALISTA,
        ARQUITECTO,
        DISENADOR_UX,
        FRONTEND,
        BACKEND,
        QA,
        DEVOPS
    }
}
