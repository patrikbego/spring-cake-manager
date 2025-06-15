package org.example.springcakemanager.service;

import org.example.springcakemanager.exception.ResourceNotFoundException;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CakeServiceTest {

    @Mock
    private CakeRepository cakeRepository;

    private CakeService cakeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cakeService = new CakeService(cakeRepository);
    }

    @Test
    void delete_whenCakeExists_deletesCake() {
        Long id = 1L;
        when(cakeRepository.existsById(id)).thenReturn(true);

        cakeService.delete(id);

        verify(cakeRepository).deleteById(id);
    }

    @Test
    void delete_whenCakeDoesNotExist_throwsException() {
        Long id = 2L;
        when(cakeRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cakeService.delete(id));
        verify(cakeRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCakeById_whenExists_returnsCake() {
        Long id = 3L;
        CakeEntity cake = new CakeEntity(id, "T", "D", "I");
        when(cakeRepository.findById(id)).thenReturn(Optional.of(cake));

        CakeEntity result = cakeService.getCakeById(id);

        assertEquals(cake, result);
    }

    @Test
    void getCakeById_whenMissing_throwsException() {
        when(cakeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cakeService.getCakeById(99L));
    }

    @Test
    void update_whenCakeExists_updatesFields() {
        Long id = 4L;
        CakeEntity existing = new CakeEntity(id, "Old", "Old", "Old");
        CakeEntity update = new CakeEntity(null, "New", "NewDesc", "NewImg");

        when(cakeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(cakeRepository.save(any(CakeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CakeEntity result = cakeService.update(id, update);

        assertEquals("New", result.getTitle());
        assertEquals("NewDesc", result.getDescription());
        assertEquals("NewImg", result.getImage());
        verify(cakeRepository).save(existing);
    }

    @Test
    void update_whenCakeMissing_throwsException() {
        when(cakeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cakeService.update(1L, new CakeEntity()));
    }
}
