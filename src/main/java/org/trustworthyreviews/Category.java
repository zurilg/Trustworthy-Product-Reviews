package org.trustworthyreviews;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    /**
     * Empty constructor for JPA
     */
    public Category() { }

    /**
     * Constructor for Category
     *
     * @param name The name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Getter for ID
     *
     * @return The unique identifier for the category
     */
    public UUID getId() {
        return id;
    }
    /**
     * Setter for ID
     */
    public void setId(UUID id) {
        this.id = id;
    }
    /**
     * Getter for name
     *
     * @return The name of the category
     */
    public String getName() {
        return name;
    }
    /**
     * Setter for name
     *
     * @param name The name of the category
     */
    public void setName(String name) {
        this.name = name;
    }
}
