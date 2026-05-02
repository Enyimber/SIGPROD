package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
	@Autowired
    private UsuarioService usuarioService;
 
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }
 
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles",   Usuario.RolSistema.values());
        return "usuarios/formulario";
    }
 
    @PostMapping("/nuevo")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          RedirectAttributes flash,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.RolSistema.values());
            return "usuarios/formulario";
        }
        try {
            usuarioService.crear(usuario);
            flash.addFlashAttribute("successMsg", "Usuario creado.");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("roles",    Usuario.RolSistema.values());
            return "usuarios/formulario";
        }
    }
 
    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable String id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        model.addAttribute("roles",   Usuario.RolSistema.values());
        return "usuarios/formulario";
    }
 
    @PostMapping("/{id}/editar")
    public String editar(@PathVariable String id,
                         @Valid @ModelAttribute("usuario") Usuario datos,
                         BindingResult result,
                         RedirectAttributes flash,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.RolSistema.values());
            return "usuarios/formulario";
        }
        usuarioService.actualizar(id, datos);
        flash.addFlashAttribute("successMsg", "Usuario actualizado.");
        return "redirect:/usuarios";
    }
 
    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable String id, RedirectAttributes flash) {
        usuarioService.desactivar(id);
        flash.addFlashAttribute("warningMsg", "Usuario desactivado.");
        return "redirect:/usuarios";
    }
}
