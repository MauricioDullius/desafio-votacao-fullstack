package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.model.entity.Vote;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteMapperTest {

    @Autowired
    private VoteMapper mapper;

    @Test
    void shouldMapToEntity() {
        VoteDTO dto = new VoteDTO();
        dto.setId(1L);
        dto.setAgendaId(10L);
        dto.setMemberCpf("12345678901");
        dto.setVote(VoteState.YES);

        Vote entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getVote(), entity.getVote());
        // Note: agenda and member associations won't be set by mapper directly, just IDs/CPF in DTO
    }

    @Test
    void shouldMapToDTO() {
        Vote vote = new Vote();
        vote.setId(2L);
        vote.setVote(VoteState.NO);

        Agenda agenda = new Agenda();
        agenda.setId(20L);
        vote.setAgenda(agenda);

        Member member = new Member();
        member.setCpf("10987654321");
        vote.setMember(member);

        VoteDTO dto = mapper.toDTO(vote);

        assertNotNull(dto);
        assertEquals(vote.getId(), dto.getId());
        assertEquals(vote.getVote(), dto.getVote());
        assertEquals(agenda.getId(), dto.getAgendaId());
        assertEquals(member.getCpf(), dto.getMemberCpf());
    }

    @Test
    void shouldMapToDTOList() {
        Vote vote1 = new Vote();
        vote1.setId(1L);
        vote1.setVote(VoteState.YES);

        Vote vote2 = new Vote();
        vote2.setId(2L);
        vote2.setVote(VoteState.NO);

        List<VoteDTO> dtos = mapper.toDTOList(Arrays.asList(vote1, vote2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(VoteState.YES, dtos.get(0).getVote());
        assertEquals(VoteState.NO, dtos.get(1).getVote());
    }
}