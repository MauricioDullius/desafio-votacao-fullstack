package br.com.db.system.votingsystem.v1.service;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import br.com.db.system.votingsystem.v1.exception.InvalidRequestException;
import br.com.db.system.votingsystem.v1.exception.ResourceNotFoundException;
import br.com.db.system.votingsystem.v1.mapper.AssemblyMapper;
import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import br.com.db.system.votingsystem.v1.repository.AssemblyRepository;
import br.com.db.system.votingsystem.v1.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AssemblyService {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyService.class);

    @Autowired
    private AssemblyRepository repository;

    @Autowired
    private AssemblyMapper mapper;

    public AssemblyDTO create(AssemblyDTO dto) {
        logger.info("Creating assembly with name '{}'", dto.getName());

        if( dto.getId() != null ) dto.setId(null);

        Assembly assembly = mapper.toEntity(dto);
        DateUtils.validateDates(assembly.getStart(), assembly.getEnd());

        Assembly saved = repository.save(assembly);
        logger.info("Assembly created successfully with id {}", saved.getId());

        return mapper.toDTO(saved);
    }

    public AssemblyDTO update(AssemblyDTO dto) {
        logger.info("Updating assembly with id {}", dto.getId());

        Assembly assembly = repository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", dto.getId());
                    return new ResourceNotFoundException("Assembly not found with id: " + dto.getId());
                });

        mapper.updateFromDTO(dto, assembly);
        DateUtils.validateDates(assembly.getStart(), assembly.getEnd());

        Assembly updated = repository.save(assembly);
        logger.info("Assembly updated successfully with id {}", updated.getId());

        return mapper.toDTO(updated);
    }

    public Page<AssemblyDTO> findAll(Pageable pageable) {
        logger.info("Retrieving all assemblies");
        Page<Assembly> assembliesPage = repository.findAll(pageable);
        logger.info("Found {} assemblies", assembliesPage.getNumberOfElements());
        return assembliesPage.map(mapper::toDTO);
    }

    public AssemblyDTO findById(Long id) {
        logger.info("Searching for assembly with id {}", id);
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", id);
                    return new ResourceNotFoundException("Assembly not found with id: " + id);
                });
        logger.info("Assembly found with id {}", id);
        return mapper.toDTO(assembly);
    }

    public Assembly findByIdEntity(Long id) {
        logger.info("Searching for assembly with id {}", id);
        Assembly assembly = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Assembly not found with id {}", id);
                    return new ResourceNotFoundException("Assembly not found with id: " + id);
                });
        logger.info("Assembly found with id {}", id);
        return assembly;
    }
}