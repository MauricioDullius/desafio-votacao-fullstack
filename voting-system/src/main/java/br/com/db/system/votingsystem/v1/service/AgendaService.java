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
import br.com.db.system.votingsystem.v1.util.DateUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendaMapper mapper;

    @Autowired
    private AssemblyService assemblyService;

    @Autowired
    private VoteRepository voteRepository;

    public Page<AgendaResponseDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all agendas");

        Page<Agenda> agendaPage = repository.findAll(pageable);

        Map<Long, Map<VoteState, Long>> voteCounts = resolveVoteCounts(agendaPage.getContent());
        Page<AgendaResponseDTO> agendaDTOPage = agendaPage.map(agenda -> toDTOWithState(agenda, voteCounts));

        logger.info("Found {} agendas", agendaDTOPage.getNumberOfElements());

        return agendaDTOPage;
    }

    public AgendaResponseDTO findById(Long id) {
        logger.info("Searching for agenda with id {}", id);
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", id);
                    return new ResourceNotFoundException("Agenda not found with id: " + id);
                });
        logger.info("Agenda found with id {}", id);
        return toDTOWithState(agenda);
    }

    private AgendaResponseDTO toDTOWithState(Agenda agenda) {
        return toDTOWithState(agenda, resolveVoteCounts(List.of(agenda)));
    }

    private AgendaResponseDTO toDTOWithState(Agenda agenda, Map<Long, Map<VoteState, Long>> voteCounts) {
        AgendaResponseDTO dto = mapper.toDTO(agenda);
        dto.setState(resolveState(agenda, voteCounts));
        return dto;
    }

    /**
     * Batches the vote COUNTs for every closed agenda in the given list into a single
     * GROUP BY query instead of issuing 2 COUNT queries per agenda, keeping the paginated
     * listing endpoint from doing N+1 queries per page.
     */
    private Map<Long, Map<VoteState, Long>> resolveVoteCounts(List<Agenda> agendas) {
        List<Long> closedAgendaIds = agendas.stream()
                .filter(agenda -> agenda.getEnd() == null || !LocalDateTime.now().isBefore(agenda.getEnd()))
                .map(Agenda::getId)
                .toList();

        if (closedAgendaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Map<VoteState, Long>> voteCounts = new HashMap<>();
        for (VoteCountProjection row : voteRepository.countGroupedByAgendaIdsAndVote(closedAgendaIds)) {
            voteCounts.computeIfAbsent(row.getAgendaId(), id -> new EnumMap<>(VoteState.class))
                    .put(row.getVote(), row.getTotal());
        }
        return voteCounts;
    }

    private AgendaState resolveState(Agenda agenda, Map<Long, Map<VoteState, Long>> voteCounts) {
        if (agenda.getEnd() != null && LocalDateTime.now().isBefore(agenda.getEnd())) {
            return AgendaState.IN_VOTING;
        }

        Map<VoteState, Long> counts = voteCounts.getOrDefault(agenda.getId(), Collections.emptyMap());
        long yesVotes = counts.getOrDefault(VoteState.YES, 0L);
        long noVotes = counts.getOrDefault(VoteState.NO, 0L);

        return yesVotes > noVotes ? AgendaState.APPROVED : AgendaState.REJECTED;
    }

    public Agenda findByIdEntity(Long id) {
        logger.info("Searching for agenda with id {}", id);
        Agenda agenda = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", id);
                    return new ResourceNotFoundException("Agenda not found with id: " + id);
                });
        logger.info("Agenda found with id {}", id);
        return agenda;
    }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        logger.info("Creating agenda with description '{}'", dto.getDescription());

        if( dto.getId() != null ) dto.setId(null);

        Assembly assembly = assemblyService.findByIdEntity(dto.getAssemblyId());

        Agenda agenda = mapper.toEntity(dto);
        agenda.setAssembly(assembly);
        agenda.setStart(dto.getStart() == null ? LocalDateTime.now() : dto.getStart());
        agenda.setEnd(dto.getEnd() == null ? agenda.getStart().plusMinutes(1) : dto.getEnd());

        DateUtils.validateDates(agenda.getStart(), agenda.getEnd());

        agenda = repository.save(agenda);
        logger.info("Agenda created successfully with id {}", agenda.getId());

        return toDTOWithState(agenda);
    }

    public AgendaResponseDTO update(AgendaRequestDTO dto) {
        logger.info("Updating agenda with id {}", dto.getId());
        if (dto.getAssemblyId() == null) {
            logger.warn("Invalid request: Assembly ID must be provided");
            throw new InvalidRequestException("Assembly ID must be provided");
        }

        Agenda agenda = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Agenda not found with id {}", dto.getId());
                    return new ResourceNotFoundException("Agenda not found with id: " + dto.getId());
                });

        Assembly assembly = assemblyService.findByIdEntity(dto.getAssemblyId());

        agenda.setDescription(dto.getDescription());
        agenda.setStart(dto.getStart());
        agenda.setEnd(dto.getEnd());
        agenda.setAssembly(assembly);

        DateUtils.validateDates(agenda.getStart(), agenda.getEnd());

        agenda = repository.save(agenda);
        logger.info("Agenda updated successfully with id {}", agenda.getId());

        return toDTOWithState(agenda);
    }

    public void deleteById(Long id) {
        logger.info("Deleting agenda with id {}", id);
        if (!repository.existsById(id)) {
            logger.error("Agenda not found with id {}", id);
            throw new ResourceNotFoundException("Agenda not found with id: " + id);
        }
        repository.deleteById(id);
        logger.info("Agenda deleted with id {}", id);
    }
}
