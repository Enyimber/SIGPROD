package com.techsoft.solutions.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database:sigprod}")
    private String databaseName;

    /** Colecciones requeridas por SIGPROD */
    private static final List<String> COLECCIONES = Arrays.asList(
        "usuarios",
        "proyectos",
        "sprints",
        "historias_usuario",
        "tareas",
        "defectos",
        "riesgos",
        "bitacora_auditoria"
    );

    @Override
    public void run(ApplicationArguments args) {
        verificarYCrearBaseDatos();
        crearColeccionesYIndices();
    }

    private void verificarYCrearBaseDatos() {
        try {
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            // Si la BD no existe, MongoDB la crea automáticamente al primer insert.
            // Hacemos un ping para verificar conectividad.
            db.runCommand(new Document("ping", 1));
            log.info("✅ Conexión MongoDB OK — base de datos: '{}'", databaseName);
        } catch (Exception e) {
            log.error("❌ Error al conectar con MongoDB: {}", e.getMessage());
            throw new RuntimeException("No se pudo conectar a MongoDB: " + e.getMessage(), e);
        }
    }

    private void crearColeccionesYIndices() {
        // Verificar y crear colecciones que no existan
        for (String coleccion : COLECCIONES) {
            if (!mongoTemplate.collectionExists(coleccion)) {
                mongoTemplate.createCollection(coleccion);
                log.info("📦 Colección creada: '{}'", coleccion);
            } else {
                log.debug("📦 Colección ya existe: '{}'", coleccion);
            }
        }

        // Crear índices explícitos
        try {
            // usuarios: correo único
            mongoTemplate.indexOps("usuarios")
                .ensureIndex(new Index().on("correo", Sort.Direction.ASC).unique());

            // proyectos: estado, productOwnerId
            mongoTemplate.indexOps("proyectos")
                .ensureIndex(new Index().on("estado", Sort.Direction.ASC));

            // sprints: proyectoId + numero
            mongoTemplate.indexOps("sprints")
                .ensureIndex(new Index().on("proyectoId", Sort.Direction.ASC).on("numero", Sort.Direction.ASC));

            // historias_usuario: proyectoId, sprintId
            mongoTemplate.indexOps("historias_usuario")
                .ensureIndex(new Index().on("proyectoId", Sort.Direction.ASC));
            mongoTemplate.indexOps("historias_usuario")
                .ensureIndex(new Index().on("sprintId", Sort.Direction.ASC).sparse());

            // tareas: historiaId, asignadoId
            mongoTemplate.indexOps("tareas")
                .ensureIndex(new Index().on("historiaId", Sort.Direction.ASC));
            mongoTemplate.indexOps("tareas")
                .ensureIndex(new Index().on("asignadoId", Sort.Direction.ASC).sparse());

            // defectos: proyectoId, asignadoAId
            mongoTemplate.indexOps("defectos")
                .ensureIndex(new Index().on("proyectoId", Sort.Direction.ASC));
            mongoTemplate.indexOps("defectos")
                .ensureIndex(new Index().on("asignadoAId", Sort.Direction.ASC).sparse());

            // riesgos: proyectoId
            mongoTemplate.indexOps("riesgos")
                .ensureIndex(new Index().on("proyectoId", Sort.Direction.ASC));

            // bitácora: usuarioId, fecha
            mongoTemplate.indexOps("bitacora_auditoria")
                .ensureIndex(new Index().on("usuarioId", Sort.Direction.ASC).sparse());
            mongoTemplate.indexOps("bitacora_auditoria")
                .ensureIndex(new Index().on("fecha", Sort.Direction.DESC));

            log.info("✅ Índices MongoDB creados/verificados correctamente");
        } catch (Exception e) {
            log.warn("⚠️  Error creando índices (pueden ya existir): {}", e.getMessage());
        }
    }
}
