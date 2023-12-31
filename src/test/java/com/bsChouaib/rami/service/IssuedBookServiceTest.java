package com.bsChouaib.rami.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.bsChouaib.rami.common.Constants;
import com.bsChouaib.rami.model.Book;
import com.bsChouaib.rami.model.Category;
import com.bsChouaib.rami.model.IssuedBook;
import com.bsChouaib.rami.repository.IssuedBookRepository;
import com.bsChouaib.rami.service.IssuedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IssuedBookServiceTest {

	@Mock
	private IssuedBookRepository issuedBookRepository;

	@InjectMocks
	private IssuedBookService issuedBookService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAll() {
		IssuedBook issuedBook1 = new IssuedBook();
		IssuedBook issuedBook2 = new IssuedBook();
		List<IssuedBook> issuedBooks = Arrays.asList(issuedBook1, issuedBook2);

		when(issuedBookRepository.findAll()).thenReturn(issuedBooks);

		List<IssuedBook> result = issuedBookService.getAll();

		assertEquals(2, result.size());
		assertSame(issuedBook1, result.get(0));
		assertSame(issuedBook2, result.get(1));
	}

	@Test
	public void testGetAllEmptyList() {
		when(issuedBookRepository.findAll()).thenReturn(Collections.emptyList());

		List<IssuedBook> result = issuedBookService.getAll();

		assertTrue(result.isEmpty());
	}

	@Test
	public void testGet() {
		Long issuedBookId = 1L;
		IssuedBook issuedBook = new IssuedBook();
		issuedBook.setId(issuedBookId);

		when(issuedBookRepository.findById(issuedBookId)).thenReturn(Optional.of(issuedBook));

		IssuedBook result = issuedBookService.get(issuedBookId);

		assertNotNull(result);
		assertSame(issuedBook, result);
	}

	@Test
	public void testGetNotFound() {
		Long issuedBookId = 1L;

		when(issuedBookRepository.findById(issuedBookId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> issuedBookService.get(issuedBookId));
	}

	@Test
	public void testGetCountByBook() {
		Book book = new Book(1L, "Title", "Tag", "Author", "Publisher", "ISBN", Constants.BOOK_STATUS_AVAILABLE, new Date(), new Category());

		when(issuedBookRepository.countByBookAndReturned(book, Constants.BOOK_NOT_RETURNED)).thenReturn(2L);

		Long result = issuedBookService.getCountByBook(book);

		assertEquals(Optional.of(2L).get(), result);
	}

	@Test
	public void testSave() {
		IssuedBook issuedBook = new IssuedBook();
		when(issuedBookRepository.save(issuedBook)).thenReturn(issuedBook);

		IssuedBook result = issuedBookService.save(issuedBook);

		assertSame(issuedBook, result);
	}

	@Test
	public void testAddNew() {
		IssuedBook issuedBook = new IssuedBook();
		when(issuedBookRepository.save(issuedBook)).thenReturn(issuedBook);

		IssuedBook result = issuedBookService.addNew(issuedBook);

		assertSame(issuedBook, result);
		assertEquals(Constants.BOOK_NOT_RETURNED, issuedBook.getReturned());
	}
}