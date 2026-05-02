package com.techsoft.solutions.service;

import com.techsoft.solutions.model.Usuario;
import com.techsoft.solutions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(
            "ROLE_" + usuario.getRolSistema().name()
        );

        return new User(
            usuario.getCorreo(),
            usuario.getPasswordHash(),
            List.of(authority)
        );
    }
}
