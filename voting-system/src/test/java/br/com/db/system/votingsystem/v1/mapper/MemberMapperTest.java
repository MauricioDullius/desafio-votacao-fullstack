package br.com.db.system.votingsystem.v1.mapper;

import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberMapperTest {

    @Autowired
    private MemberMapper mapper;

    @Test
    void shouldMapToEntity() {
        MemberDTO dto = new MemberDTO();
        dto.setId(1L);
        dto.setName("João");
        dto.setCpf("12345678901");
        dto.setActive(true);

        Member entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCpf(), entity.getCpf());
        assertTrue(entity.isActive());
    }

    @Test
    void shouldMapToDTO() {
        Member entity = new Member();
        entity.setId(1L);
        entity.setName("Maria");
        entity.setCpf("10987654321");
        entity.setActive(false);

        MemberDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getCpf(), dto.getCpf());
        assertFalse(dto.isActive());
    }

    @Test
    void shouldMapToDTOList() {
        Member m1 = new Member();
        m1.setId(1L);
        m1.setName("A");
        m1.setCpf("11111111111");
        m1.setActive(true);

        Member m2 = new Member();
        m2.setId(2L);
        m2.setName("B");
        m2.setCpf("22222222222");
        m2.setActive(false);

        List<MemberDTO> dtos = mapper.toDTOList(Arrays.asList(m1, m2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("A", dtos.get(0).getName());
        assertEquals("B", dtos.get(1).getName());
    }

    @Test
    void shouldMapToEntityList() {
        MemberDTO d1 = new MemberDTO();
        d1.setId(1L);
        d1.setName("X");
        d1.setCpf("99999999999");
        d1.setActive(true);

        MemberDTO d2 = new MemberDTO();
        d2.setId(2L);
        d2.setName("Y");
        d2.setCpf("88888888888");
        d2.setActive(false);

        List<Member> entities = mapper.toEntityList(Arrays.asList(d1, d2));

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("X", entities.get(0).getName());
        assertEquals("Y", entities.get(1).getName());
    }
}