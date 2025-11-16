package org.trustworthyreviews;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Test class for Category entity.
 *
 * @version 11-17-2025
 */
public class CategoryTest {
    /**
     * Test method for creating a Category and verifying its name.
     */
    @org.junit.Test
    public void testCategoryCreation() {
        Category category = new Category("Electronics");
        assertEquals("Electronics", category.getName());
    }

    /**
     * Test method for Category setters.
     */
    @org.junit.Test
    public void testCategorySetters(){
        Category category = new Category("Books");
        category.setName("Movies");
        UUID id = UUID.randomUUID();
        category.setId(id);
        assertEquals("Movies", category.getName());
        assertEquals(id, category.getId());
    }
}
