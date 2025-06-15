package org.example.springcakemanager.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ControllerUtilsTest {

    @Test
    void createPageable_validInput_returnsPageable() {
        Pageable pageable = ControllerUtils.createPageable(1, 5, "title");
        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals("title", pageable.getSort().iterator().next().getProperty());
    }

    @Test
    void createPageable_invalidSize_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ControllerUtils.createPageable(0, 0, "id"));
    }

    @Test
    void createPageable_unknownSortField_defaultsToId() {
        Pageable pageable = ControllerUtils.createPageable(0, 10, "unknown");
        assertEquals("id", pageable.getSort().iterator().next().getProperty());
    }
}
