package org.example.springcakemanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    private CakeRepository repository;
    private ObjectMapper objectMapper;
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() throws Exception {
        repository = mock(CakeRepository.class);
        objectMapper = new ObjectMapper();
        String path = new File("src/test/resources/seed-data.json").getAbsolutePath();
        dataLoader = new DataLoader(repository, objectMapper, "file:" + path);
    }

    @Test
    void run_whenRepositoryEmpty_seedsData() throws Exception {
        when(repository.count()).thenReturn(0L);

        dataLoader.run();

        verify(repository).saveAll(argThat((List<CakeEntity> list) -> list.size() == 2));
    }

    @Test
    void run_whenRepositoryNotEmpty_doesNothing() throws Exception {
        when(repository.count()).thenReturn(5L);

        dataLoader.run();

        verify(repository, never()).saveAll(anyList());
    }
}
