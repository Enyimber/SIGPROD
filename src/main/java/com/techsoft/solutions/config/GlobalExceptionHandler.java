package com.techsoft.solutions.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejo global de excepciones.
 * - Para rutas /api/** responde JSON.
 * - Para rutas web responde una página de error.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // ── Manejo JSON para la API REST ───────────────────────
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntime(RuntimeException ex, HttpServletRequest request) {
        if (request.getRequestURI().startsWith(getBasePath(request) + "/api/")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 404, "error", "Not Found", "message", ex.getMessage()
            ));
        }
        // Para web: redirige a página de error
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMsg", ex.getMessage());
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        if (request.getRequestURI().startsWith(getBasePath(request) + "/api/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 400, "error", "Bad Request", "message", ex.getMessage()
            ));
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMsg", ex.getMessage());
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }

    private String getBasePath(HttpServletRequest request) {
        String ctx = request.getContextPath();
        return ctx != null ? ctx : "";
    }
}
