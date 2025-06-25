package com.brecho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_tipo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioTipoId;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
}
