package br.com.db.system.votingsystem.v1.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import br.com.db.system.votingsystem.v1.model.enums.VoteState;
import org.junit.jupiter.api.Test;

public class VoteTest {

    @Test
    void testGettersAndSetters() {
        Vote vote = new Vote();

        vote.setId(1L);

        Agenda agenda = new Agenda();
        agenda.setId(10L);
        vote.setAgenda(agenda);

        Member member = new Member();
        member.setId(20L);
        vote.setMember(member);

        vote.setVote(VoteState.YES);

        assertEquals(1L, vote.getId());
        assertEquals(10L, vote.getAgenda().getId());
        assertEquals(20L, vote.getMember().getId());
        assertEquals(VoteState.YES, vote.getVote());
    }

    @Test
    void testEqualsAndHashCode() {
        Vote vote1 = new Vote();
        vote1.setId(1L);

        Vote vote2 = new Vote();
        vote2.setId(1L);

        Vote vote3 = new Vote();
        vote3.setId(2L);

        assertEquals(vote1, vote2);
        assertEquals(vote1.hashCode(), vote2.hashCode());

        assertNotEquals(vote1, vote3);
        assertNotEquals(vote1.hashCode(), vote3.hashCode());
    }
}