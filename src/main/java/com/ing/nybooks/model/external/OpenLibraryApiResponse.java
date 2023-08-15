package com.ing.nybooks.model.external;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Represents the response from the Open Library API.
 */
@Getter
@Setter
public class OpenLibraryApiResponse {
    private List<String> publishers;
    private List<Map<String, String>> languages;
    private Map<String, List<String>> identifiers;
    private String title;
    private String physical_format;
    private int number_of_pages;
    private List<String> isbn_13;
    private List<String> isbn_10;
    private String publish_date;
}
