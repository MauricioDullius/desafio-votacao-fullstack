package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.client.FakeCpfValidatorClient;
import br.com.db.system.votingsystem.v1.client.dto.ValidationResult;
import br.com.db.system.votingsystem.v1.dto.MemberDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.MemberMapper;
import br.com.db.system.votingsystem.v1.model.entity.Member;
import br.com.db.system.votingsystem.v1.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @InjectMocks
    private MemberService service;

    @Mock
    private MemberRepository repository;

    @Mock
    private MemberMapper mapper;

    @Mock
    private FakeCpfValidatorClient cpfValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindMemberByIdSuccessfully() {
        Long memberId = 1L;

        Member member = new Member();
        member.setId(memberId);
        member.setName("John Doe");
        member.setCpf("12345678900");
        member.setActive(true);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberId);
        memberDTO.setName("John Doe");
        memberDTO.setCpf("12345678900");
        memberDTO.setActive(true);

        when(repository.findById(memberId)).thenReturn(Optional.of(member));
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = service.findById(memberId);

        assertNotNull(result);
        assertEquals(memberDTO.getId(), result.getId());
        assertEquals(memberDTO.getName(), result.getName());
        assertEquals(memberDTO.getCpf(), result.getCpf());
        assertTrue(result.isActive());

        verify(repository).findById(memberId);
        verify(mapper).toDTO(member);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        Long memberId = 99L;

        when(repository.findById(memberId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(memberId);
        });

        assertEquals("Member not found with id: " + memberId, ex.getMessage());
        verify(repository).findById(memberId);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldFindMemberByCpfSuccessfully() {
        String cpf = "12345678900";

        Member member = new Member();
        member.setId(1L);
        member.setCpf(cpf);
        member.setName("Jane Doe");
        member.setActive(true);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);
        memberDTO.setCpf(cpf);
        memberDTO.setName("Jane Doe");
        memberDTO.setActive(true);

        when(repository.findMemberByCpf(cpf)).thenReturn(member);
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = service.findByCpf(cpf);

        assertNotNull(result);
        assertEquals(cpf, result.getCpf());
        verify(repository).findMemberByCpf(cpf);
        verify(mapper).toDTO(member);
    }

    @Test
    void shouldThrowWhenFindByCpfNotFound() {
        String cpf = "00000000000";

        when(repository.findMemberByCpf(cpf)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.findByCpf(cpf);
        });

        assertEquals("Member not found with CPF: " + cpf, ex.getMessage());
        verify(repository).findMemberByCpf(cpf);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldCreateMemberSuccessfully() {
        MemberDTO dto = new MemberDTO();
        dto.setName("New Member");
        dto.setCpf("12345678900");
        dto.setActive(true);

        Member memberEntity = new Member();
        memberEntity.setName(dto.getName());
        memberEntity.setCpf(dto.getCpf());
        memberEntity.setActive(dto.isActive());

        Member savedMember = new Member();
        savedMember.setId(1L);
        savedMember.setName(dto.getName());
        savedMember.setCpf(dto.getCpf());
        savedMember.setActive(dto.isActive());

        MemberDTO savedDTO = new MemberDTO();
        savedDTO.setId(1L);
        savedDTO.setName(dto.getName());
        savedDTO.setCpf(dto.getCpf());
        savedDTO.setActive(dto.isActive());

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(true);

        when(repository.findMemberByCpf(dto.getCpf())).thenReturn(null);
        when(cpfValidator.validateCpf(dto.getCpf())).thenReturn(validationResult);
        when(mapper.toEntity(dto)).thenReturn(memberEntity);
        when(repository.save(memberEntity)).thenReturn(savedMember);
        when(mapper.toDTO(savedMember)).thenReturn(savedDTO);

        MemberDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(savedDTO.getId(), result.getId());
        verify(repository).findMemberByCpf(dto.getCpf());
        verify(cpfValidator).validateCpf(dto.getCpf());
        verify(mapper).toEntity(dto);
        verify(repository).save(memberEntity);
        verify(mapper).toDTO(savedMember);
    }

    @Test
    void shouldThrowWhenCreateMemberCpfExists() {
        MemberDTO dto = new MemberDTO();
        dto.setCpf("12345678900");
        dto.setName("New Member");

        Member existingMember = new Member();
        existingMember.setId(1L);

        when(repository.findMemberByCpf(dto.getCpf())).thenReturn(existingMember);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            service.create(dto);
        });

        assertEquals("Member with CPF: " + dto.getCpf() + " already exists", ex.getMessage());
        verify(repository).findMemberByCpf(dto.getCpf());
        verifyNoMoreInteractions(cpfValidator, mapper, repository);
    }

    @Test
    void shouldThrowWhenCreateMemberCpfInvalid() {
        MemberDTO dto = new MemberDTO();
        dto.setCpf("invalidCpf");
        dto.setName("New Member");

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(false);


        when(repository.findMemberByCpf(dto.getCpf())).thenReturn(null);
        when(cpfValidator.validateCpf(dto.getCpf())).thenReturn(validationResult);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });

        assertEquals("Invalid CPF: " + dto.getCpf(), ex.getMessage());
        verify(repository).findMemberByCpf(dto.getCpf());
        verify(cpfValidator).validateCpf(dto.getCpf());
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void shouldUpdateMemberSuccessfully() {
        MemberDTO dto = new MemberDTO();
        dto.setId(1L);
        dto.setName("Updated Name");
        dto.setCpf("12345678900");
        dto.setActive(false);

        Member existingMember = new Member();
        existingMember.setId(dto.getId());
        existingMember.setName("Old Name");
        existingMember.setCpf("11111111111");
        existingMember.setActive(true);

        Member updatedMember = new Member();
        updatedMember.setId(dto.getId());
        updatedMember.setName(dto.getName());
        updatedMember.setCpf(dto.getCpf());
        updatedMember.setActive(dto.isActive());

        MemberDTO updatedDTO = new MemberDTO();
        updatedDTO.setId(dto.getId());
        updatedDTO.setName(dto.getName());
        updatedDTO.setCpf(dto.getCpf());
        updatedDTO.setActive(dto.isActive());

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(true);


        when(repository.findById(dto.getId())).thenReturn(Optional.of(existingMember));
        when(cpfValidator.validateCpf(dto.getCpf())).thenReturn(validationResult);
        when(repository.save(existingMember)).thenReturn(updatedMember);
        when(mapper.toDTO(updatedMember)).thenReturn(updatedDTO);

        MemberDTO result = service.update(dto);

        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        verify(repository).findById(dto.getId());
        verify(cpfValidator).validateCpf(dto.getCpf());
        verify(repository).save(existingMember);
        verify(mapper).toDTO(updatedMember);
    }

    @Test
    void shouldThrowWhenUpdateMemberNotFound() {
        MemberDTO dto = new MemberDTO();
        dto.setId(99L);
        dto.setName("Updated Name");
        dto.setCpf("11111111111");

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        assertEquals("Member not found with id: " + dto.getId(), ex.getMessage());
        verify(repository).findById(dto.getId());
        verifyNoMoreInteractions(cpfValidator, repository, mapper);
    }

    @Test
    void shouldThrowWhenUpdateMemberCpfInvalid() {
        MemberDTO dto = new MemberDTO();
        dto.setId(1L);
        dto.setName("New Member");
        dto.setCpf("invalidCpf");

        Member existingMember = new Member();
        existingMember.setId(dto.getId());

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(false);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(existingMember));
        when(cpfValidator.validateCpf(dto.getCpf())).thenReturn(validationResult);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        assertEquals("Invalid CPF: " + dto.getCpf(), ex.getMessage());
        verify(repository).findById(dto.getId());
        verify(cpfValidator).validateCpf(dto.getCpf());
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void shouldDeleteMemberByCpfSuccessfully() {
        String cpf = "12345678900";

        Member member = new Member();
        member.setCpf(cpf);

        when(repository.findMemberByCpf(cpf)).thenReturn(member);

        service.deleteByCpf(cpf);

        verify(repository).findMemberByCpf(cpf);
        verify(repository).delete(member);
    }

    @Test
    void shouldThrowWhenDeleteByCpfNotFound() {
        String cpf = "00000000000";

        when(repository.findMemberByCpf(cpf)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteByCpf(cpf);
        });

        assertEquals("Member not found with CPF: " + cpf, ex.getMessage());
        verify(repository).findMemberByCpf(cpf);
        verify(repository, never()).delete(any());
    }
}