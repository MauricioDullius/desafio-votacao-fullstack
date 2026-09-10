package br.com.db.system.votingsystem.v1.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    void testGettersAndSetters() {
        Member member = new Member();

        member.setId(1L);
        member.setName("Mauricio D");
        member.setCpf("12345678901");
        member.setActive(true);

        assertEquals(1L, member.getId());
        assertEquals("Mauricio D", member.getName());
        assertEquals("12345678901", member.getCpf());
        assertTrue(member.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("Mauricio D");
        member1.setCpf("12345678901");
        member1.setActive(true);

        Member member2 = new Member();
        member2.setId(1L);
        member2.setName("Mauricio D");
        member2.setCpf("12345678901");
        member2.setActive(true);

        Member member3 = new Member();
        member3.setId(2L);
        member3.setName("Mauricio S");
        member3.setCpf("10987654321");
        member3.setActive(false);

        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());

        assertNotEquals(member1, member3);
        assertNotEquals(member1.hashCode(), member3.hashCode());
    }
}