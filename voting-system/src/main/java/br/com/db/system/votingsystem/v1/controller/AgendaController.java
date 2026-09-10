package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.controller.doc.AgendaControllerDoc;
import br.com.db.system.votingsystem.v1.dto.AgendaRequestDTO;
import br.com.db.system.votingsystem.v1.dto.AgendaResponseDTO;
import br.com.db.system.votingsystem.v1.service.AgendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agenda/v1")
public class AgendaController implements AgendaControllerDoc {

    @Autowired
    private AgendaService service;

    @Override
    @GetMapping
    public ResponseEntity<Page<AgendaResponseDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        int maxSize = 30;
        if (size > maxSize) {
            size = maxSize;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<AgendaResponseDTO> create(@RequestBody @Valid AgendaRequestDTO agendaDTO) {
        return ResponseEntity.status(201).body(service.create(agendaDTO));
    }

    @Override
    @PutMapping
    public ResponseEntity<AgendaResponseDTO> update(@RequestBody @Valid AgendaRequestDTO agendaDTO) {
        return ResponseEntity.ok(service.update(agendaDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}