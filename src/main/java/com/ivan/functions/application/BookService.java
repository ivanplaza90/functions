package com.ivan.functions.application;

import com.ivan.functions.domain.Book;
import com.ivan.functions.domain.BookDate;
import com.ivan.functions.domain.BookMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BookService {

    private BookMapper bookMapper;

    public BookService(final BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public Optional<BookDate> filter(final String filter, final List<Book> books) {
        printNotPublishedBooks(books);
        return books.stream()
            .filter(book -> book.getTitle().contains(filter))
            .filter(book -> book.getSummary().contains(filter))
            .filter(book -> book.getAuthor().getBio().contains(filter))
            .sorted(Comparator.comparing(Book::getPublicationTimestamp))
            .findFirst()
            .map(bookMapper::toBookDate);

    }

    private void printNotPublishedBooks(final List<Book> books) {
        books.stream()
            .filter(book -> book.getPublicationTimestamp() != null)
            .forEach(book -> System.out.println("Not published book" + book));
    }
}
