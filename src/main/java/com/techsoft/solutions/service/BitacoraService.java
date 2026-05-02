package com.techsoft.solutions.service;

import com.techsoft.solutions.model.BitacoraAuditoria;
import com.techsoft.solutions.repository.BitacoraAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraAuditoriaRepository auditoriaRepository;

    @Async
    public void registrar(String usuarioId, String accion, String entidad,
                          String entidadId, String descripcion, String ip) {
        BitacoraAuditoria entrada = new BitacoraAuditoria();
        entrada.setUsuarioId(usuarioId);
        entrada.setAccion(accion);
        entrada.setEntidad(entidad);
        entrada.setEntidadId(entidadId);
        entrada.setDescripcion(descripcion);
        entrada.setIp(ip != null ? ip : "0.0.0.0");
        auditoriaRepository.save(entrada);
    }

    @Async
    public void registrar(String usuarioId, String accion, String entidad,
                          String entidadId, String datosAnteriores, String datosNuevos,
                          String descripcion, String ip) {
        BitacoraAuditoria entrada = new BitacoraAuditoria();
        entrada.setUsuarioId(usuarioId);
        entrada.setAccion(accion);
        entrada.setEntidad(entidad);
        entrada.setEntidadId(entidadId);
        entrada.setDatosAnteriores(datosAnteriores);
        entrada.setDatosNuevos(datosNuevos);
        entrada.setDescripcion(descripcion);
        entrada.setIp(ip != null ? ip : "0.0.0.0");
        auditoriaRepository.save(entrada);
    }

    public List<BitacoraAuditoria> listarRecientes() {
        return auditoriaRepository.findTop50ByOrderByFechaDesc();
    }

    public List<BitacoraAuditoria> listarPorUsuario(String usuarioId) {
        return auditoriaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<BitacoraAuditoria> listarPorEntidad(String entidad, String entidadId) {
        return auditoriaRepository.findByEntidadAndEntidadIdOrderByFechaDesc(entidad, entidadId);
    }
}
