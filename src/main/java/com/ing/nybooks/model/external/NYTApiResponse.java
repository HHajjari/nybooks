package com.ing.nybooks.model.external;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the response from the New York Times (NYT) API.
 */
@Getter
@Setter
public class NYTApiResponse {
    private String status;
    private String copyright;
    private int num_results;
    private List<BookResult> results;

    @Getter
    @Setter
    public static class BookResult {
        private String title;
        private String description;
        private String contributor;
        private String author;
        private String contributor_note;
        private String price;
        private String age_group;
        private String publisher;
        private List<Isbn> isbns;
        private List<RankHistory> ranks_history;
        private List<Review> reviews;

        @Getter
        @Setter
        public static class Isbn {
            private String isbn10;
            private String isbn13;
        }

        @Getter
        @Setter
        public static class RankHistory {
            private String primary_isbn10;
            private String primary_isbn13;
            private int rank;
            private String list_name;
            private String display_name;
            private String published_date;
            private String bestsellers_date;
            private int weeks_on_list;
            private int rank_last_week;
            private int asterisk;
            private int dagger;
        }

        @Getter
        @Setter
        public static class Review {
            private String book_review_link;
            private String first_chapter_link;
            private String sunday_review_link;
            private String article_chapter_link;
        }
    }
}
