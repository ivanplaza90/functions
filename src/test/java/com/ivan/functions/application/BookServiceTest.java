package com.ivan.functions.application;

import com.ivan.functions.domain.Author;
import com.ivan.functions.domain.Book;
import com.ivan.functions.domain.BookDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .withZone(ZoneId.systemDefault());
    @Mock
    private Book book;

    @Mock
    private Book anotherBook;

    @Mock
    private BookDate bookDate;

    @Mock
    private Author author;

    private BookService bookService;

    @Test
    void it_should_exists() {
        assertThat(bookService).isNotNull();
    }

    @Test
    void it_should_filter_a_book() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("********abcd**********");
        given(book.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********abc*********");

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response.get().getBook())
            .as("It should filter a book by name")
            .isEqualTo(book);
    }

    @Test
    void it_should_not_filter_a_book_when_name_not_contains_the_filter() {
        given(book.getTitle()).willReturn("*********a*********");

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response)
            .as("It should not filter a book when name not contains the filter")
            .isEmpty();
    }

    @Test
    void it_should_not_filter_a_book_when_summary_not_contains_the_filter() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("*********a*********");

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response)
            .as("It should not filter a book when summary not contains the filter")
            .isEmpty();
    }

    @Test
    void it_should_not_filter_a_book_when_author_bio_not_contains_the_filter() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("********abcd**********");
        given(book.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********a*********");

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response)
                .as("It should not filter a book when author bio not contains the filter")
                .isEmpty();
    }

    @Test
    void it_should_filter_a_book_given_multiple_books() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("********abcd**********");
        given(book.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********abc*********");
        given(book.getPublicationTimestamp()).willReturn("2");

        given(anotherBook.getTitle()).willReturn("*********abcd*********");
        given(anotherBook.getSummary()).willReturn("********abcd**********");
        given(anotherBook.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********abc*********");
        given(anotherBook.getPublicationTimestamp()).willReturn("1");

        final var response = bookService.filter("abc", List.of(book, anotherBook));

        assertThat(response.get().getBook())
            .as("It should filter a book by name")
            .isEqualTo(anotherBook);
    }

    @Test
    void it_should_filter_a_book_given_a_book_without_publication_date() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("********abcd**********");
        given(book.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("**************abc*************");
        given(book.getPublicationTimestamp()).willReturn("1");

        given(anotherBook.getTitle()).willReturn("*********abcd*********");
        given(anotherBook.getSummary()).willReturn("********abcd**********");
        given(anotherBook.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("***abc***");


        final var response = bookService.filter("abc", List.of(book, anotherBook));

        assertThat(response.get().getBook())
                .as("It should filter a book withouy publication date")
                .isEqualTo(book);
    }

    @Test
    void it_should_map_date() {
        given(book.getTitle()).willReturn("*********abcd*********");
        given(book.getSummary()).willReturn("********abcd**********");
        given(book.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********abc*********");

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response.get().getTimestamp())
            .as("It should map date")
            .isEqualTo(DATE_TIME_FORMATTER.format(Instant.now()));
    }

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }
}
