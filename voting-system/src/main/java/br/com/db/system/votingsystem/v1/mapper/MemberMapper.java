package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDTO toDTO(Member member);

    Member toEntity(MemberDTO dto);

    List<MemberDTO> toDTOList(List<Member> members);

    List<Member> toEntityList(List<MemberDTO> dtos);
}