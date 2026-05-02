package com.techsoft.solutions.controller;

import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/auth")
public class AuthController {
	@Autowired
    private UsuarioService usuarioService;
 
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("errorMsg", "Correo o contraseña incorrectos.");
        if (logout != null) model.addAttribute("logoutMsg", "Sesión cerrada correctamente.");
        return "auth/login";
    }
 
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Usuario.RolSistema.values());
        return "auth/registro";
    }
 
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult result,
                            RedirectAttributes flash,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.RolSistema.values());
            return "auth/registro";
        }
        try {
            usuarioService.crear(usuario);
            flash.addFlashAttribute("successMsg", "Usuario registrado. Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("roles", Usuario.RolSistema.values());
            return "auth/registro";
        }
    }
}
