package org.example.springcakemanager.mapper;

import org.example.springcakemanager.dto.CakeDTO;
import org.example.springcakemanager.model.CakeEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CakeMapperTest {
    private final CakeMapper mapper = new CakeMapper();

    @Test
    void toDTO_mapsAllFields() {
        CakeEntity entity = new CakeEntity(1L, "Title", "Desc", "img.png");

        CakeDTO dto = mapper.toDTO(entity);

        assertEquals(1L, dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Desc", dto.description());
        assertEquals("img.png", dto.image());
    }

    @Test
    void toEntity_mapsAllFields() {
        CakeDTO dto = new CakeDTO(2L, "T", "D", "img.jpg");

        CakeEntity entity = mapper.toEntity(dto);

        assertEquals(2L, entity.getId());
        assertEquals("T", entity.getTitle());
        assertEquals("D", entity.getDescription());
        assertEquals("img.jpg", entity.getImage());
    }

    @Test
    void toEntity_handlesNullId() {
        CakeDTO dto = new CakeDTO(null, "T", "D", "img.jpg");

        CakeEntity entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("T", entity.getTitle());
    }
}
