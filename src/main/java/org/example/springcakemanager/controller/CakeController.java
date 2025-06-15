package org.example.springcakemanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcakemanager.dto.CakeDTO;
import org.example.springcakemanager.mapper.CakeMapper;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.service.CakeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.springcakemanager.utils.ControllerUtils.createPageable;

@Slf4j
@RestController
@RequestMapping("/api/v1/cakes")
@Tag(name = "Cake API", description = "CRUD operations for cakes")
@RequiredArgsConstructor
public class CakeController {

    private final CakeService service;
    private final CakeMapper mapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific cake by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cake retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cake not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CakeDTO> getCake(@PathVariable Long id) {
        log.info("Fetching cake with ID {}", id);
        CakeEntity entity = service.getCakeById(id);
        return ResponseEntity.ok(mapper.toDTO(entity));
    }

    @GetMapping
    @Operation(summary = "List all cakes (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cakes listed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CakeDTO>> listCakes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Listing cakes - page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Pageable pageable = createPageable(page, size, sortBy);
        Page<CakeEntity> entities = service.findAll(pageable);
        List<CakeDTO> dtos = entities.getContent().stream().map(mapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(new PageImpl<>(dtos, pageable, entities.getTotalElements()));
    }

    @PostMapping
    @Operation(summary = "Add a new cake")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cake created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CakeDTO> addCake(@Valid @RequestBody CakeDTO dto) {
        log.info("Adding new cake: {}", dto.title());
        CakeEntity entity = service.save(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(entity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing cake")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cake updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Cake not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CakeDTO> updateCake(@PathVariable("id") Long cakeId, @Valid @RequestBody CakeDTO dto) {
        log.info("Updating cake with ID {}", cakeId);
        CakeEntity updated = service.update(cakeId, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cake")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cake deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cake not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCake(@PathVariable("id") Long cakeId) {
        log.info("Deleting cake with ID {}", cakeId);
        service.delete(cakeId);
        return ResponseEntity.noContent().build();
    }

}
