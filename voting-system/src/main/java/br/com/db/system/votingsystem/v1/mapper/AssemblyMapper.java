package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AgendaMapper.class})
public interface AssemblyMapper {

    @Mapping(target = "agendas", source = "agendas")
    AssemblyDTO toDTO(Assembly assembly);

    @Mapping(target = "agendas", ignore = true)
    Assembly toEntity(AssemblyDTO dto);

    @Mapping(target = "agendas", ignore = true)
    void updateFromDTO(AssemblyDTO dto, @MappingTarget Assembly entity);

    List<AssemblyDTO> toDTOList(List<Assembly> entities);
}