package com.ivan.functions.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private int id;
    private String title;
    private String publicationTimestamp;
    private int pages;
    private String summary;
    private Author author;
}
