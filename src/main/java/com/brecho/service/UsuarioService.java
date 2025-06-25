package com.brecho.service;

import com.brecho.dto.UsuarioCreateDTO;
import com.brecho.dto.UsuarioDTO;
import com.brecho.exception.BusinessException;
import com.brecho.exception.ResourceNotFoundException;
import com.brecho.mapper.UsuarioMapper;
import com.brecho.model.Perfil;
import com.brecho.model.Usuario;
import com.brecho.repository.PerfilRepository;
import com.brecho.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO create(UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new BusinessException("Email já está em uso: " + usuarioCreateDTO.getEmail());
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioCreateDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioCreateDTO.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuarioSalvo);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Verificar se o email já está em uso por outro usuário
        if (usuarioRepository.findByEmailAndIdNot(usuarioDTO.getEmail(), id).isPresent()) {
            throw new BusinessException("Email já está em uso por outro usuário: " + usuarioDTO.getEmail());
        }

        usuarioMapper.updateEntityFromDTO(usuarioDTO, usuario);

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuarioAtualizado);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }


    public Usuario createUser(String username, String password, String roleName) {
        // Verifica se o usuário já existe
        if (usuarioRepository.existsByNome(username)) {
            throw new IllegalArgumentException("Usuário com este username já existe: " + username);
        }

        // Busca ou cria o papel (role)
        Perfil perfil = perfilRepository.findByNome(roleName)
                .orElseGet(() -> {
                    Perfil newPerfil = new Perfil();
                    newPerfil.setNome(roleName);
                    return perfilRepository.save(newPerfil);
                });

        // Cria o novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(username);
        novoUsuario.setSenha(passwordEncoder.encode(password)); // Criptografa a senha
        novoUsuario.addRole(perfil); // Adiciona o papel ao usuário

        return usuarioRepository.save(novoUsuario);
    }
}
