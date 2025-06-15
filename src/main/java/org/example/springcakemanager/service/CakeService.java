package org.example.springcakemanager.service;

import lombok.extern.slf4j.Slf4j;
import org.example.springcakemanager.exception.ResourceNotFoundException;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.repository.CakeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CakeService {
    private final CakeRepository cakeRepository;

    public CakeService(CakeRepository cakeRepository) {
        this.cakeRepository = cakeRepository;
    }

    @Cacheable("cakesCache")
    public Page<CakeEntity> findAll(Pageable pageable) {
        log.info("Fetching all cakes - page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        return cakeRepository.findAll(pageable);
    }

    @Cacheable(value = "cakeByIdCache", key = "#id")
    public CakeEntity getCakeById(Long id) {
        log.info("Fetching cake by ID: {}", id);
        return cakeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cake not found"));
    }

    @Transactional
    @CacheEvict(value = "cakesCache", allEntries = true)
    public CakeEntity save(CakeEntity cake) {
        log.info("Saving new cake: {}", cake.getTitle());
        return cakeRepository.save(cake);
    }

    @Transactional
    @CacheEvict(value = {"cakesCache", "cakeByIdCache"}, key = "#id")
    public CakeEntity update(Long id, CakeEntity cake) {
        log.info("Updating cake ID {} with new data", id);
        return cakeRepository.findById(id).map(existingCake -> {
            updateCakeFields(existingCake, cake);
            return cakeRepository.save(existingCake);
        }).orElseThrow(() -> new ResourceNotFoundException("Cake not found"));
    }

    @Transactional
    @CacheEvict(value = {"cakesCache", "cakeByIdCache"}, key = "#id")
    public void delete(Long id) {
        log.info("Deleting cake ID {}", id);
        if (!cakeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cake not found");
        }
        cakeRepository.deleteById(id);
    }

    private void updateCakeFields(CakeEntity existing, CakeEntity cake) {
        existing.setTitle(cake.getTitle());
        existing.setDescription(cake.getDescription());
        existing.setImage(cake.getImage());
    }
}
