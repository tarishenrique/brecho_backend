package com.brecho.mapper;

import com.brecho.dto.ProdutoDTO;
import com.brecho.model.Produto;
import com.brecho.model.ProdutoImagem;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProdutoMapper {
    @Mapping(target = "vendedorId", source = "vendedor.usuarioId")
    @Mapping(target = "nomeVendedor", source = "vendedor.nome")
    @Mapping(target = "urlsImagens", source = "imagens", qualifiedByName = "imagensToUrls")
    ProdutoDTO toDTO(Produto produto);

    List<ProdutoDTO> toDTOList(List<Produto> produtos);

    @Mapping(target = "produtoId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "imagens", ignore = true)
    @Mapping(target = "itensPedido", ignore = true)
    Produto toEntity(ProdutoDTO produtoDTO);

    @Mapping(target = "produtoId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "imagens", ignore = true)
    @Mapping(target = "itensPedido", ignore = true)
    void updateEntityFromDTO(ProdutoDTO produtoDTO, @MappingTarget Produto produto);

    @Named("imagensToUrls")
    default List<String> imagensToUrls(List<ProdutoImagem> imagens) {
        if (imagens == null) return null;
        return imagens.stream()
                .map(ProdutoImagem::getUrlImagem)
                .collect(Collectors.toList());
    }
}
