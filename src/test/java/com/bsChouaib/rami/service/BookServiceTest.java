package com.bsChouaib.rami.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.bsChouaib.rami.common.Constants;
import com.bsChouaib.rami.model.Book;
import com.bsChouaib.rami.model.Category;
import com.bsChouaib.rami.repository.BookRepository;
import com.bsChouaib.rami.service.BookService;
import com.bsChouaib.rami.service.IssuedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private IssuedBookService issuedBookService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTotalCount() {
        when(bookRepository.count()).thenReturn(5L);

        Long totalCount = bookService.getTotalCount();

        assertEquals(Optional.of(5L).get(), totalCount);
    }

    @Test
    public void testGetTotalIssuedBooks() {
        when(bookRepository.countByStatus(Constants.BOOK_STATUS_ISSUED)).thenReturn(3L);

        Long totalIssuedBooks = bookService.getTotalIssuedBooks();

        assertEquals(Optional.of(3L).get(), totalIssuedBooks);
    }

    @Test
    public void testGetAll() {
        Book book1 = new Book(1L, "Title 1", "Tag1", "Author1", "Publisher1", "ISBN1", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        Book book2 = new Book(2L, "Title 2", "Tag2", "Author2", "Publisher2", "ISBN2", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAll();

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
    }

    @Test
    public void testGetAllEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = bookService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGet() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.get(bookId);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testGetNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookService.get(bookId));
    }

    @Test
    public void testGetByTag() {
        String tag = "Tag";
        Book book = new Book(1L, "Title", tag, "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());

        when(bookRepository.findByTag(tag)).thenReturn(book);

        Book result = bookService.getByTag(tag);

        assertNotNull(result);
        assertEquals(tag, result.getTag());
    }

    @Test
    public void testGetByTagNotFound() {
        String tag = "NonexistentTag";

        when(bookRepository.findByTag(tag)).thenReturn(null);

        assertNull(bookService.getByTag(tag));
    }

    @Test
    public void testGetByIds() {
        List<Long> bookIds = Arrays.asList(1L, 2L, 3L);
        Book book1 = new Book(1L, "Title 1", "Tag1", "Author1", "Publisher1", "ISBN1", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        Book book2 = new Book(2L, "Title 2", "Tag2", "Author2", "Publisher2", "ISBN2", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        Book book3 = new Book(3L, "Title 3", "Tag3", "Author3", "Publisher3", "ISBN3", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());

        when(bookRepository.findAllById(bookIds)).thenReturn(Arrays.asList(book1, book2, book3));

        List<Book> result = bookService.get(bookIds);

        assertEquals(3, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
        assertEquals("Title 3", result.get(2).getTitle());
    }

    @Test
    public void testGetByIdsEmptyList() {
        List<Long> bookIds = Collections.emptyList();

        List<Book> result = bookService.get(bookIds);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByCategory() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        Book book1 = new Book(1L, "Title 1", "Tag1", "Author1", "Publisher1", "ISBN1", Constants.BOOK_STATUS_AVAILABLE, new Date(), category);
        Book book2 = new Book(2L, "Title 2", "Tag2", "Author2", "Publisher2", "ISBN2", Constants.BOOK_STATUS_AVAILABLE, new Date(), category);

        when(bookRepository.findByCategory(category)).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookService.getByCategory(category);

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
    }

    @Test
    public void testGetByCategoryEmptyList() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));

        when(bookRepository.findByCategory(category)).thenReturn(Collections.emptyList());

        List<Book> result = bookService.getByCategory(category);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAvailableByCategory() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        Book book1 = new Book(1L, "Title 1", "Tag1", "Author1", "Publisher1", "ISBN1", Constants.BOOK_STATUS_AVAILABLE, new Date(), category);
        Book book2 = new Book(2L, "Title 2", "Tag2", "Author2", "Publisher2", "ISBN2", Constants.BOOK_STATUS_ISSUED, new Date(), category);

        when(bookRepository.findByCategoryAndStatus(category, Constants.BOOK_STATUS_AVAILABLE)).thenReturn(Collections.singletonList(book1));

        List<Book> result = bookService.geAvailabletByCategory(category);

        assertEquals(1, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
    }

    @Test
    public void testAddNew() {
        Book book = new Book(null, "New Title", "New Tag", "New Author", "New Publisher", "New ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        when(bookRepository.save(book)).thenReturn(new Book(1L, "New Title", "New Tag", "New Author", "New Publisher", "New ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category()));

        Book result = bookService.addNew(book);

        assertNotNull(result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals(Constants.BOOK_STATUS_AVAILABLE, result.getStatus());
    }

    @Test
    public void testSave() {
        Book book = new Book(1L, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book(1L, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());

        assertDoesNotThrow(() -> bookService.delete(book));
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteById() {
        Long bookId = 1L;

        assertDoesNotThrow(() -> bookService.delete(bookId));
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testHasUsageWithIssuedBooks() {
        Book book = new Book(1L, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_ISSUED, new Date(), new Category());
        when(issuedBookService.getCountByBook(book)).thenReturn(1L);

        boolean hasUsage = bookService.hasUsage(book);

        assertTrue(hasUsage);
    }

    @Test
    public void testHasUsageWithoutIssuedBooks() {
        Book book = new Book(1L, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());
        when(issuedBookService.getCountByBook(book)).thenReturn(0L);

        boolean hasUsage = bookService.hasUsage(book);

        assertFalse(hasUsage);
    }
}
