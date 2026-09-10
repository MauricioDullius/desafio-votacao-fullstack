package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.model.entity.Vote;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "agendaId", source = "agenda.id")
    @Mapping(target = "memberCpf", source = "member.cpf")
    VoteDTO toDTO(Vote vote);

    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "member", ignore = true)
    Vote toEntity(VoteDTO dto);

    List<VoteDTO> toDTOList(List<Vote> votes);
}
