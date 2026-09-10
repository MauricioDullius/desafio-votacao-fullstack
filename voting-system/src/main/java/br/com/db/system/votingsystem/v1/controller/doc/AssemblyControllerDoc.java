package br.com.db.system.votingsystem.v1.controller.doc;

import br.com.db.system.votingsystem.v1.dto.AssemblyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Assembly")
public interface AssemblyControllerDoc {

    @Operation(summary = "List all assemblies paginated",
            description = "Returns a page of AssemblyDTO, sorted by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
    })
    ResponseEntity<Page<AssemblyDTO>> findAll(
            @Parameter(description = "Page number, starting at 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size, max 30") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Find assembly by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assembly found"),
            @ApiResponse(responseCode = "404", description = "Assembly not found", content = @Content)
    })
    ResponseEntity<AssemblyDTO> findById(@Parameter(description = "ID of the assembly") @PathVariable Long id);

    @Operation(summary = "Create a new assembly")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Assembly created"),
            @ApiResponse(responseCode = "400", description = "Invalid assembly data", content = @Content)
    })
    ResponseEntity<AssemblyDTO> create(@RequestBody AssemblyDTO assemblyDTO);

    @Operation(summary = "Update an existing assembly")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assembly updated"),
            @ApiResponse(responseCode = "400", description = "Invalid assembly data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assembly not found", content = @Content)
    })
    ResponseEntity<AssemblyDTO> update(@RequestBody AssemblyDTO assemblyDTO);
}