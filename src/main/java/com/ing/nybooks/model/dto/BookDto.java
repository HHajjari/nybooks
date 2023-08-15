package com.ing.nybooks.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Data Transfer Object (DTO) for book response information.
 */
@Getter
@Setter
public class BookDto {
    /**
     * The name of the book.
     */
    private String name;

    /**
     * The publisher of the book.
     */
    private String publisher;

    /**
     * The author's name.
     */
    private String author;

    /**
     * The publication year of the book.
     */
    private int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto book = (BookDto) o;
        return year == book.year && Objects.equals(name, book.name) && Objects.equals(publisher, book.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publisher, year);
    }
}