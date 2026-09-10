package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AgendaMapper;
import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.model.enums.AgendaState;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import br.com.db.system.votingsystem.v1.repository.AgendaRepository;
import br.com.db.system.votingsystem.v1.repository.VoteCountProjection;
import br.com.db.system.votingsystem.v1.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AssemblyService assemblyService;

    @Mock
    private AgendaMapper agendaMapper;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Assembly assembly;
    private Agenda agenda;
    private AgendaRequestDTO agendaRequestDTO;
    private AgendaResponseDTO agendaResponseDTO;

    private record VoteCount(Long agendaId, VoteState vote, Long total) implements VoteCountProjection {
        @Override
        public Long getAgendaId() {
            return agendaId;
        }

        @Override
        public VoteState getVote() {
            return vote;
        }

        @Override
        public Long getTotal() {
            return total;
        }
    }

    @BeforeEach
    void setup() {
        assembly = new Assembly();
        assembly.setId(1L);

        agenda = new Agenda();
        agenda.setId(1L);
        agenda.setDescription("Test agenda");
        agenda.setAssembly(assembly);
        agenda.setStart(LocalDateTime.now().plusMinutes(1));
        agenda.setEnd(LocalDateTime.now().plusMinutes(2));

        agendaRequestDTO = new AgendaRequestDTO();
        agendaRequestDTO.setId(1L);
        agendaRequestDTO.setDescription("Test agenda");
        agendaRequestDTO.setAssemblyId(1L);
        agendaRequestDTO.setStart(agenda.getStart());
        agendaRequestDTO.setEnd(agenda.getEnd());

        agendaResponseDTO = new AgendaResponseDTO();
        agendaResponseDTO.setId(1L);
        agendaResponseDTO.setDescription("Test agenda");
        agendaResponseDTO.setAssemblyId(1L);
        agendaResponseDTO.setStart(agenda.getStart());
        agendaResponseDTO.setEnd(agenda.getEnd());
        agendaResponseDTO.setState(AgendaState.IN_VOTING);
    }

    @Test
    void shouldCreateAgenda() {
        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaMapper.toEntity(agendaRequestDTO)).thenReturn(agenda);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaResponseDTO);

        AgendaResponseDTO result = agendaService.create(agendaRequestDTO);

        assertNotNull(result);
        assertEquals(agendaRequestDTO.getDescription(), result.getDescription());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenCreateAgendaWithAssemblyNotFound() {
        when(assemblyService.findByIdEntity(1L)).thenThrow(new ResourceNotFoundException("Assembly not found with id: 1"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.create(agendaRequestDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(assemblyService).findByIdEntity(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenCreateAgendaWithInvalidDates() {
        agendaRequestDTO.setStart(LocalDateTime.now().minusDays(1));
        agendaRequestDTO.setEnd(LocalDateTime.now().minusDays(2));

        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaMapper.toEntity(agendaRequestDTO)).thenReturn(agenda);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> agendaService.create(agendaRequestDTO));

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date or earlier than the current date."));
        verify(assemblyService).findByIdEntity(1L);
        verifyNoInteractions(agendaRepository);
    }

    @Test
    void shouldUpdateAgenda() {
        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaResponseDTO);

        AgendaResponseDTO result = agendaService.update(agendaRequestDTO);

        assertNotNull(result);
        assertEquals(agendaRequestDTO.getDescription(), result.getDescription());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowWhenUpdateAssemblyNotFound() {
        when(assemblyService.findByIdEntity(1L)).thenThrow(new ResourceNotFoundException("Assembly not found with id: 1"));
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.update(agendaRequestDTO));

        assertEquals("Assembly not found with id: 1", ex.getMessage());
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithInvalidDates() {
        agendaRequestDTO.setStart(LocalDateTime.now().minusDays(2));
        agendaRequestDTO.setEnd(LocalDateTime.now().minusDays(1));

        when(assemblyService.findByIdEntity(1L)).thenReturn(assembly);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> agendaService.update(agendaRequestDTO));

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date or earlier than the current date."));
        verify(assemblyService).findByIdEntity(1L);
        verify(agendaRepository).findById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    void shouldThrowWhenUpdateAgendaWithNullAssemblyId() {
        agendaRequestDTO.setAssemblyId(null);

        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> agendaService.update(agendaRequestDTO));

        assertEquals("Assembly ID must be provided", ex.getMessage());
        verifyNoInteractions(assemblyService, agendaRepository);
    }

    @Test
    void shouldDeleteAgendaById() {
        when(agendaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(agendaRepository).deleteById(1L);

        assertDoesNotThrow(() -> agendaService.deleteById(1L));

        verify(agendaRepository).existsById(1L);
        verify(agendaRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteAgendaNotFound() {
        when(agendaRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> agendaService.deleteById(1L));

        assertEquals("Agenda not found with id: 1", ex.getMessage());
        verify(agendaRepository).existsById(1L);
        verifyNoMoreInteractions(agendaRepository);
    }

    @Test
    void shouldResolveInVotingStateWithoutQueryingVotes() {
        // agenda.end is in the future (see setup), so the state must resolve
        // to IN_VOTING purely from the dates, without touching the vote counts.
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaResponseDTO);

        AgendaResponseDTO result = agendaService.findById(1L);

        assertEquals(AgendaState.IN_VOTING, result.getState());
        verifyNoInteractions(voteRepository);
    }

    @Test
    void shouldResolveApprovedStateWhenYesVotesAreMajority() {
        agenda.setEnd(LocalDateTime.now().minusMinutes(1));
        agendaResponseDTO.setEnd(agenda.getEnd());

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaResponseDTO);
        when(voteRepository.countGroupedByAgendaIdsAndVote(List.of(1L))).thenReturn(List.of(
                new VoteCount(1L, VoteState.YES, 3L),
                new VoteCount(1L, VoteState.NO, 1L)
        ));

        AgendaResponseDTO result = agendaService.findById(1L);

        assertEquals(AgendaState.APPROVED, result.getState());
        verify(voteRepository).countGroupedByAgendaIdsAndVote(List.of(1L));
    }

    @Test
    void shouldResolveRejectedStateWhenNoVotesAreMajorityOrTied() {
        agenda.setEnd(LocalDateTime.now().minusMinutes(1));
        agendaResponseDTO.setEnd(agenda.getEnd());

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaMapper.toDTO(agenda)).thenReturn(agendaResponseDTO);
        when(voteRepository.countGroupedByAgendaIdsAndVote(List.of(1L))).thenReturn(List.of(
                new VoteCount(1L, VoteState.YES, 2L),
                new VoteCount(1L, VoteState.NO, 2L)
        ));

        AgendaResponseDTO result = agendaService.findById(1L);

        assertEquals(AgendaState.REJECTED, result.getState());
    }
}
