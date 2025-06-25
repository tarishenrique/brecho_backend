package com.brecho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha;

    @Column(length = 15)
    private String telefone;

    @Column(length = 200)
    private String endereco;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "tipo_usuario")
//    private TipoUsuario tipoUsuario;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioTipoId", nullable = false)
    private UsuarioTipo usuarioTipo;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

    @ManyToMany(fetch = FetchType.EAGER) // Carrega os roles junto com o usuário
    @JoinTable(
            name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usuarioId"),
            inverseJoinColumns = @JoinColumn(name = "perfilId")
    )
    private Set<Perfil> roles = new HashSet<>();

    public void addRole(Perfil role) {
        this.roles.add(role);
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
//        if (tipoUsuario == null) {
//            tipoUsuario = TipoUsuario.CLIENTE;
//        }
    }



}
