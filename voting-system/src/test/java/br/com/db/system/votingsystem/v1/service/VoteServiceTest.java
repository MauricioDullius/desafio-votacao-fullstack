package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.VoteDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.VoteMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.model.entity.Vote;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import br.com.db.system.votingsystem.v1.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class VoteServiceTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private VoteMapper mapper;

    @Mock
    private AgendaService agendaService;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAllVotesSuccessfully() {
        Vote vote = new Vote();
        VoteDTO dto = new VoteDTO();

        Page<Vote> page = new PageImpl<>(List.of(vote));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDTO(any(Vote.class))).thenReturn(dto);

        Page<VoteDTO> result = service.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(any(Pageable.class));
        verify(mapper).toDTO(vote);
    }

    @Test
    void shouldFindVotesByAgendaIdSuccessfully() {
        Vote vote = new Vote();
        VoteDTO dto = new VoteDTO();

        when(repository.findByAgendaId(1L)).thenReturn(List.of(vote));
        when(mapper.toDTOList(anyList())).thenReturn(List.of(dto));

        List<VoteDTO> result = service.findByAgendaId(1L);

        assertEquals(1, result.size());
        verify(repository).findByAgendaId(1L);
        verify(mapper).toDTOList(List.of(vote));
    }

    @Test
    void shouldFindVoteByIdSuccessfully() {
        Vote vote = new Vote();
        VoteDTO dto = new VoteDTO();

        when(repository.findById(1L)).thenReturn(Optional.of(vote));
        when(mapper.toDTO(vote)).thenReturn(dto);

        VoteDTO result = service.findById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
        verify(mapper).toDTO(vote);
    }

    @Test
    void shouldThrowWhenVoteByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void shouldCreateVoteSuccessfully() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(10L);
        Agenda agenda = new Agenda();
        agenda.setId(1L);

        Vote vote = new Vote();
        Vote savedVote = new Vote();
        savedVote.setId(1L);
        VoteDTO savedDto = new VoteDTO();
        savedDto.setId(1L);

        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(member);
        when(agendaService.findByIdEntity(dto.getAgendaId())).thenReturn(agenda);
        when(repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(vote);
        when(repository.save(vote)).thenReturn(savedVote);
        when(mapper.toDTO(savedVote)).thenReturn(savedDto);

        VoteDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(savedDto.getId(), result.getId());
    }

    @Test
    void shouldThrowWhenCreateVoteMemberAlreadyVoted() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.NO);

        Member member = new Member();
        member.setId(10L);
        Agenda agenda = new Agenda();
        agenda.setId(1L);

        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(member);
        when(agendaService.findByIdEntity(dto.getAgendaId())).thenReturn(agenda);
        when(repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> service.create(dto));
    }

    @Test
    void shouldThrowWhenCreateVoteMemberNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setMemberCpf("00000000000");
        dto.setAgendaId(1L);
        dto.setVote(VoteState.YES);

        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void shouldUpdateVoteSuccessfully() {
        VoteDTO dto = new VoteDTO();
        dto.setId(5L);
        dto.setVote(VoteState.NO);
        dto.setMemberCpf("99999999999");

        Vote vote = new Vote();
        Member member = new Member();

        when(repository.findById(dto.getId())).thenReturn(Optional.of(vote));
        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(member);
        when(repository.save(vote)).thenReturn(vote);
        when(mapper.toDTO(vote)).thenReturn(dto);

        VoteDTO result = service.update(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
    }

    @Test
    void shouldThrowWhenUpdateVoteNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setId(99L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(dto));
    }

    @Test
    void shouldThrowWhenUpdateVoteMemberNotFound() {
        VoteDTO dto = new VoteDTO();
        dto.setId(5L);
        dto.setMemberCpf("123");
        dto.setVote(VoteState.YES);

        Vote vote = new Vote();

        when(repository.findById(dto.getId())).thenReturn(Optional.of(vote));
        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.update(dto));
    }

    @Test
    void shouldDeleteVoteSuccessfully() {
        when(repository.existsById(7L)).thenReturn(true);
        doNothing().when(repository).deleteById(7L);

        assertDoesNotThrow(() -> service.deleteById(7L));
        verify(repository).deleteById(7L);
    }

    @Test
    void shouldThrowWhenDeleteVoteNotFound() {
        when(repository.existsById(77L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(77L));
    }

    @Test
    void shouldThrowWhenVotingSessionIsClosed() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(10L);

        Agenda agenda = new Agenda();
        agenda.setId(1L);
        agenda.setEnd(LocalDateTime.now().minusMinutes(1));

        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(member);
        when(agendaService.findByIdEntity(dto.getAgendaId())).thenReturn(agenda);

        assertThrows(BusinessRuleException.class, () -> service.create(dto));
        verify(repository, never()).existsByMemberIdAndAgendaId(anyLong(), anyLong());
        verify(repository, never()).save(any(Vote.class));
    }

    @Test
    void shouldAllowVoteWhenSessionIsStillOpen() {
        VoteDTO dto = new VoteDTO();
        dto.setAgendaId(1L);
        dto.setMemberCpf("12345678900");
        dto.setVote(VoteState.YES);

        Member member = new Member();
        member.setId(10L);

        Agenda agenda = new Agenda();
        agenda.setId(1L);
        agenda.setEnd(LocalDateTime.now().plusMinutes(1));

        Vote vote = new Vote();
        Vote savedVote = new Vote();
        savedVote.setId(1L);
        VoteDTO savedDto = new VoteDTO();
        savedDto.setId(1L);

        when(memberService.findByCpfEntity(dto.getMemberCpf())).thenReturn(member);
        when(agendaService.findByIdEntity(dto.getAgendaId())).thenReturn(agenda);
        when(repository.existsByMemberIdAndAgendaId(member.getId(), agenda.getId())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(vote);
        when(repository.save(vote)).thenReturn(savedVote);
        when(mapper.toDTO(savedVote)).thenReturn(savedDto);

        VoteDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(savedDto.getId(), result.getId());
    }
}