package com.brecho.repository;

import com.brecho.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.id != :id")
    Optional<Usuario> findByEmailAndIdNot(@Param("email") String email, @Param("usuarioId") Long usuarioId);

    Optional<Usuario> findByNome(String username);
    boolean existsByNome(String username);

}
