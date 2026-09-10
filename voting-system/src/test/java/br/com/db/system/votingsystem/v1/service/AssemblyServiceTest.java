package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.exception.BusinessRuleException;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssemblyServiceTest {

    @InjectMocks
    private AssemblyService service;

    @Mock
    private AssemblyRepository repository;

    @Mock
    private AssemblyMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAssemblySuccessfully() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setName("Test Assembly");
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Assembly entity = new Assembly();
        entity.setName(dto.getName());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());

        Assembly savedEntity = new Assembly();
        savedEntity.setId(1L);
        savedEntity.setName(dto.getName());
        savedEntity.setStart(dto.getStart());
        savedEntity.setEnd(dto.getEnd());

        AssemblyDTO savedDto = new AssemblyDTO();
        savedDto.setId(1L);
        savedDto.setName(dto.getName());
        savedDto.setStart(dto.getStart());
        savedDto.setEnd(dto.getEnd());

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(savedDto);

        AssemblyDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(savedDto.getId(), result.getId());
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(savedEntity);
    }

    @Test
    void shouldThrowWhenCreateAssemblyWithInvalidDates() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setName("Invalid Dates Assembly");
        dto.setStart(LocalDateTime.now().plusDays(2));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Assembly entity = new Assembly();
        entity.setName(dto.getName());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());

        when(mapper.toEntity(dto)).thenReturn(entity);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            service.create(dto);
        });

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date"));
        verify(mapper).toEntity(dto);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateAssemblySuccessfully() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(1L);
        dto.setName("Updated Assembly");
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(3));

        Assembly existing = new Assembly();
        existing.setId(dto.getId());
        existing.setName("Old Assembly");
        existing.setStart(LocalDateTime.now().plusDays(1));
        existing.setEnd(LocalDateTime.now().plusDays(2));

        Assembly updated = new Assembly();
        updated.setId(dto.getId());
        updated.setName(dto.getName());
        updated.setStart(dto.getStart());
        updated.setEnd(dto.getEnd());

        AssemblyDTO updatedDto = new AssemblyDTO();
        updatedDto.setId(dto.getId());
        updatedDto.setName(dto.getName());
        updatedDto.setStart(dto.getStart());
        updatedDto.setEnd(dto.getEnd());

        when(repository.findById(dto.getId())).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            AssemblyDTO sourceDto = invocation.getArgument(0);
            Assembly targetEntity = invocation.getArgument(1);
            targetEntity.setName(sourceDto.getName());
            targetEntity.setStart(sourceDto.getStart());
            targetEntity.setEnd(sourceDto.getEnd());
            return null;
        }).when(mapper).updateFromDTO(dto, existing);

        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(updatedDto);

        AssemblyDTO result = service.update(dto);

        assertNotNull(result);
        assertEquals(updatedDto.getName(), result.getName());
        verify(repository).findById(dto.getId());
        verify(mapper).updateFromDTO(dto, existing);
        verify(repository).save(existing);
        verify(mapper).toDTO(updated);
    }

    @Test
    void shouldThrowWhenUpdateAssemblyNotFound() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(99L);
        dto.setName("Nonexistent Assembly");
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        assertEquals("Assembly not found with id: " + dto.getId(), ex.getMessage());
        verify(repository).findById(dto.getId());
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void shouldThrowWhenUpdateAssemblyWithInvalidDates() {
        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(1L);
        dto.setName("Assembly Invalid Dates");
        dto.setStart(LocalDateTime.now().plusDays(3));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Assembly existing = new Assembly();
        existing.setId(dto.getId());
        existing.setName("Old Assembly");
        existing.setStart(LocalDateTime.now().plusDays(1));
        existing.setEnd(LocalDateTime.now().plusDays(2));

        when(repository.findById(dto.getId())).thenReturn(Optional.of(existing));

        doAnswer(invocation -> {
            AssemblyDTO sourceDto = invocation.getArgument(0);
            Assembly targetEntity = invocation.getArgument(1);
            targetEntity.setName(sourceDto.getName());
            targetEntity.setStart(sourceDto.getStart());
            targetEntity.setEnd(sourceDto.getEnd());
            return null;
        }).when(mapper).updateFromDTO(dto, existing);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            service.update(dto);
        });

        assertTrue(ex.getMessage().contains("Start date cannot be later than the end date"));
        verify(repository).findById(dto.getId());
        verify(mapper).updateFromDTO(dto, existing);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldFindAssemblyByIdSuccessfully() {
        Long id = 1L;

        Assembly assembly = new Assembly();
        assembly.setId(id);
        assembly.setName("Test Assembly");
        assembly.setStart(LocalDateTime.now().plusDays(1));
        assembly.setEnd(LocalDateTime.now().plusDays(2));

        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(id);
        dto.setName(assembly.getName());
        dto.setStart(assembly.getStart());
        dto.setEnd(assembly.getEnd());

        when(repository.findById(id)).thenReturn(Optional.of(assembly));
        when(mapper.toDTO(assembly)).thenReturn(dto);

        AssemblyDTO result = service.findById(id);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(repository).findById(id);
        verify(mapper).toDTO(assembly);
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(id);
        });

        assertEquals("Assembly not found with id: " + id, ex.getMessage());
        verify(repository).findById(id);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldFindAllAssembliesSuccessfully() {
        Assembly assembly = new Assembly();
        assembly.setId(1L);
        assembly.setName("Assembly 1");
        assembly.setStart(LocalDateTime.now().plusDays(1));
        assembly.setEnd(LocalDateTime.now().plusDays(2));

        AssemblyDTO dto = new AssemblyDTO();
        dto.setId(1L);
        dto.setName(assembly.getName());
        dto.setStart(assembly.getStart());
        dto.setEnd(assembly.getEnd());

        Page<Assembly> page = new PageImpl<>(List.of(assembly));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDTO(assembly)).thenReturn(dto);

        Page<AssemblyDTO> result = service.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(any(Pageable.class));
        verify(mapper).toDTO(assembly);
    }
}