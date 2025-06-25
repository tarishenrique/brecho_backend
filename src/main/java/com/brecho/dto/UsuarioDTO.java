package com.brecho.dto;

import com.brecho.model.Pedido;
import com.brecho.model.Produto;
import com.brecho.model.UsuarioTipo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long usuarioId;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    private String telefone;
    private String endereco;
    private LocalDateTime dataCriacao;
    private UsuarioTipo usuarioTipo;
    private List<Produto> produtos;
    private List<Pedido> pedidos;

}
