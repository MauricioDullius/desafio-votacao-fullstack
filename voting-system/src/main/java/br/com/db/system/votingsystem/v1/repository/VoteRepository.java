package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.entity.Vote;
import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByAgendaId(Long agendaId);

    boolean existsByMemberIdAndAgendaId(Long memberId, Long agendaId);

    long countByAgendaIdAndVote(Long agendaId, VoteState vote);

    @Query("SELECT v.agenda.id AS agendaId, v.vote AS vote, COUNT(v) AS total " +
            "FROM Vote v WHERE v.agenda.id IN :agendaIds GROUP BY v.agenda.id, v.vote")
    List<VoteCountProjection> countGroupedByAgendaIdsAndVote(@Param("agendaIds") List<Long> agendaIds);
}
