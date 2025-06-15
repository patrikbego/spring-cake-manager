package org.example.springcakemanager.mapper;

import org.example.springcakemanager.dto.CakeDTO;
import org.example.springcakemanager.model.CakeEntity;
import org.springframework.stereotype.Component;

@Component
public class CakeMapper {

    public CakeDTO toDTO(CakeEntity entity) {
        return new CakeDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getImage()
        );
    }

    public CakeEntity toEntity(CakeDTO dto) {
        CakeEntity entity = new CakeEntity();
        if (dto.id() != null) {
            entity.setId(dto.id());
        }
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setImage(dto.image());
        return entity;
    }
}
