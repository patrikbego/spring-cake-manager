package org.example.springcakemanager.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.repository.CakeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final String cakeDataUrl;
    private final CakeRepository repository;
    private final ObjectMapper objectMapper;

    public DataLoader(CakeRepository repository,
                      ObjectMapper objectMapper,
                      @Value("${cake.data.url}") String cakeDataUrl) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.cakeDataUrl = cakeDataUrl;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            seedInitialData();
        }
    }

    private void seedInitialData() {
        try (InputStream is = openStreamWithTimeout(cakeDataUrl)) {
            logger.info("Starting initial data seeding from: {}", cakeDataUrl);
            List<CakeEntity> cakes = objectMapper.readValue(is, new TypeReference<>() {});
            repository.saveAll(cakes);
            logger.info("✅ Initial cake data seeded.");
        } catch (IOException ex) {
            logger.error("❌ Failed to load initial cake data: {}", ex.getMessage(), ex);
        }
    }

    private InputStream openStreamWithTimeout(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection http) {
            http.setConnectTimeout(5000);
            http.setReadTimeout(5000);
            return http.getInputStream();
        }
        return connection.getInputStream();
    }
}
