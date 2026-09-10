package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(target = "assemblyId", source = "assembly.id")
    @Mapping(target = "state", ignore = true)
    AgendaResponseDTO toDTO(Agenda agenda);

    @Mapping(target = "votes", ignore = true)
    @Mapping(target = "assembly", ignore = true)
    Agenda toEntity(AgendaRequestDTO dto);

    List<AgendaResponseDTO> toDTOList(List<Agenda> agendas);
}
