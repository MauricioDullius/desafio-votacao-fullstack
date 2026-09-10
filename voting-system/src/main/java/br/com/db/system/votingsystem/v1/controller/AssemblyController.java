package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.controller.doc.AssemblyControllerDoc;
import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.service.AssemblyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assembly/v1")
public class AssemblyController implements AssemblyControllerDoc {

    @Autowired
    private AssemblyService service;

    @Override
    @GetMapping
    public ResponseEntity<Page<AssemblyDTO>> findAll(@RequestParam(defaultValue = "0") int page,
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
    public ResponseEntity<AssemblyDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<AssemblyDTO> create(@RequestBody @Valid AssemblyDTO assemblyDTO) {
        AssemblyDTO created = service.create(assemblyDTO);
        return ResponseEntity.status(201).body(created);
    }

    @Override
    @PutMapping
    public ResponseEntity<AssemblyDTO> update(@RequestBody @Valid AssemblyDTO assemblyDTO) {
        return ResponseEntity.ok(service.update(assemblyDTO));
    }
}