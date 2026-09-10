package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.service.AssemblyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssemblyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssemblyService service;

    @InjectMocks
    private AssemblyController controller;

    private ObjectMapper objectMapper;
    private AssemblyDTO assemblyDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        assemblyDTO = new AssemblyDTO();
        assemblyDTO.setId(1L);
        assemblyDTO.setName("Test Assembly");
        assemblyDTO.setStart(LocalDateTime.now().plusMinutes(5));
        assemblyDTO.setEnd(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void shouldReturnAllAssemblies() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Page<AssemblyDTO> page = new PageImpl<>(Collections.singletonList(assemblyDTO), pageable, 1);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/assembly/v1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)));
    }

    @Test
    void shouldReturnAssemblyById() throws Exception {
        when(service.findById(1L)).thenReturn(assemblyDTO);

        mockMvc.perform(get("/api/assembly/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldCreateAssembly() throws Exception {
        when(service.create(any(AssemblyDTO.class))).thenReturn(assemblyDTO);

        mockMvc.perform(post("/api/assembly/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assemblyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldUpdateAssembly() throws Exception {
        when(service.update(any(AssemblyDTO.class))).thenReturn(assemblyDTO);

        mockMvc.perform(put("/api/assembly/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assemblyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}