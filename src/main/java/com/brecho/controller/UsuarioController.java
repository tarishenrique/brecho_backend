package com.brecho.controller;

import com.brecho.dto.UsuarioCreateDTO;
import com.brecho.dto.UsuarioDTO;
import com.brecho.model.Usuario;
import com.brecho.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> getAllUsuarios(@PageableDefault(size = 20) Pageable pageable) {
        Page<UsuarioDTO> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.findByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuario = usuarioService.create(usuarioCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id,
                                                    @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuario = usuarioService.update(id, usuarioDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> registrationRequest) {
        String username = registrationRequest.get("nome");
        String password = registrationRequest.get("senha");
        String role = registrationRequest.getOrDefault("perfil", "ROLE_USER"); // Define ROLE_USER como padrão

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return new ResponseEntity<>("Username e senha são obrigatórios.", HttpStatus.BAD_REQUEST);
        }

        try {
            Usuario novoUsuario = usuarioService.createUser(username, password, role);
            return new ResponseEntity<>("Usuário " + novoUsuario.getNome() + " criado com sucesso!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict para usuário existente
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
