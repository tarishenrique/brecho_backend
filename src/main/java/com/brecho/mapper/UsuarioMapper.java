package com.brecho.mapper;

import com.brecho.dto.UsuarioCreateDTO;
import com.brecho.dto.UsuarioDTO;
import com.brecho.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);

    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "usuarioTipo", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Usuario toEntity(UsuarioCreateDTO usuarioCreateDTO);

    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "usuarioTipo", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "senha", ignore = true)
    void updateEntityFromDTO(UsuarioDTO usuarioDTO, @MappingTarget Usuario usuario);

}
