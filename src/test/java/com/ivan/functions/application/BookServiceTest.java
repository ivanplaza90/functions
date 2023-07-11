package com.ivan.functions.application;

import com.ivan.functions.domain.Author;
import com.ivan.functions.domain.Book;
import com.ivan.functions.domain.BookDate;
import com.ivan.functions.domain.BookMapper;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private Book book;

    @Mock
    private Book anotherBook;

    @Mock
    private BookDate bookDate;

    @Mock
    private Author author;

    @Mock
    private BookMapper bookMapper;

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
        given(bookMapper.toBookDate(book)).willReturn(bookDate);

        final var response = bookService.filter("abc", List.of(book));

        assertThat(response)
            .as("It should filter a book by name")
            .isNotEmpty();
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
        given(book.getPublicationTimestamp()).willReturn("1");

        given(anotherBook.getTitle()).willReturn("*********abcd*********");
        given(anotherBook.getSummary()).willReturn("********abcd**********");
        given(anotherBook.getAuthor()).willReturn(author);
        given(author.getBio()).willReturn("*********abc*********");
        given(anotherBook.getPublicationTimestamp()).willReturn("2");

        given(bookMapper.toBookDate(book)).willReturn(bookDate);

        final var response = bookService.filter("abc", List.of(book, anotherBook));

        assertThat(response.get())
            .as("It should filter a book by name")
            .isEqualTo(bookDate);

        then(bookMapper).should(times(0)).toBookDate(anotherBook);
    }

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookMapper);
    }
}
