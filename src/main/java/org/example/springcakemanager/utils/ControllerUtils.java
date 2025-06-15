package org.example.springcakemanager.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ControllerUtils {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "title", "description", "image");
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final int MAX_PAGE_SIZE = 100;

    public static Pageable createPageable(int page, int size, String sortBy) {
        // Validate size to prevent excessively large requests.
        int finalSize = Math.min(size, MAX_PAGE_SIZE);
        if (finalSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0.");
        }

        // Default to "id" if sortBy field is not valid
        String validatedSortBy = ALLOWED_SORT_FIELDS.stream()
                .filter(f -> f.equalsIgnoreCase(sortBy))
                .findFirst()
                .orElse(DEFAULT_SORT_FIELD);

        // Ensure the page number is non-negative
        int validatedPage = Math.max(page, 0);

        // Return pageable object
        return PageRequest.of(validatedPage, finalSize, Sort.by(validatedSortBy));
    }
}
