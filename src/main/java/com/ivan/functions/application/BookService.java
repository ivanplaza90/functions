package com.ivan.functions.application;

import com.ivan.functions.domain.Book;
import com.ivan.functions.domain.BookDate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BookService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .withZone(ZoneId.systemDefault());

    public Optional<BookDate> filter(final String filter, final List<Book> books) {
        printNotPublishedBooks(books);
        return books.stream()
            .filter(book -> book.getTitle().contains(filter))
            .filter(book -> book.getSummary().contains(filter))
            .filter(book -> book.getAuthor().getBio().contains(filter))
            .sorted(Comparator.comparing(Book::getPublicationTimestamp, Comparator.nullsLast(Comparator.naturalOrder())))
            .findFirst()
            .map(book -> BookDate.builder()
                .book(book)
                .timestamp(DATE_TIME_FORMATTER.format(Instant.now()))
                .build());
    }

    private void printNotPublishedBooks(final List<Book> books) {
        books.stream()
            .filter(book -> book.getPublicationTimestamp() != null)
            .forEach(book -> System.out.println("Not published book: " + book));
    }
}
