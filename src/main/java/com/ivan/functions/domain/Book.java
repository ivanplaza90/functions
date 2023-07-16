package com.ivan.functions.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book implements Comparable<Book>{

    private int id;
    private String title;
    private String publicationTimestamp;
    private int pages;
    private String summary;
    private Author author;

    @Override
    public int compareTo(Book o) {
        return publicationTimestamp.compareTo(o.publicationTimestamp)
                & author.getBio().compareTo(o.getAuthor().getBio());
    }
}
