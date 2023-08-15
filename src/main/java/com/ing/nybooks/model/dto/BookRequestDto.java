package com.ing.nybooks.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ing.nybooks.model.Const.AUTHOR_NAME_MUST_NOT_BE_EMPTY;

/**
 * Data Transfer Object (DTO) for book request information.
 */
@Getter
@Setter
public class BookRequestDto {

    /**
     * The author's name. Must not be null or empty.
     */
    @NotNull(message = AUTHOR_NAME_MUST_NOT_BE_EMPTY)
    @NotEmpty(message = AUTHOR_NAME_MUST_NOT_BE_EMPTY)
    private String author;

    /**
     * The list of publication years.
     */
    private List<Integer> year;
}
