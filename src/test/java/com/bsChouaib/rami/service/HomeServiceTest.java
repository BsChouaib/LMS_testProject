package com.bsChouaib.rami.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import com.bsChouaib.rami.service.BookService;
import com.bsChouaib.rami.service.CategoryService;
import com.bsChouaib.rami.service.HomeService;
import com.bsChouaib.rami.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HomeServiceTest {

	@Mock
	private MemberService memberService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private BookService bookService;

	@InjectMocks
	private HomeService homeService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetTopTilesMap() {
		when(memberService.getTotalCount()).thenReturn(10L);
		when(memberService.getStudentsCount()).thenReturn(5L);
		when(memberService.getParentsCount()).thenReturn(5L);
		when(categoryService.getTotalCount()).thenReturn(8L);
		when(bookService.getTotalCount()).thenReturn(20L);
		when(bookService.getTotalIssuedBooks()).thenReturn(3L);

		Map<String, Long> result = homeService.getTopTilesMap();

		assertNotNull(result);
		assertEquals(Optional.of(10L).get(), result.get("totalMembers"));
		assertEquals(Optional.of(5L).get(), result.get("totalStudents"));
		assertEquals(Optional.of(5L).get(), result.get("totalParents"));
		assertEquals(Optional.of(8L).get(), result.get("totalCategories"));
		assertEquals(Optional.of(20L).get(), result.get("totalBooks"));
		assertEquals(Optional.of(3L).get(), result.get("totalIssuedBooks"));
	}

	// Add more test cases to cover various scenarios

	@Test
	public void testGetTopTilesMapWithZeroCounts() {
		when(memberService.getTotalCount()).thenReturn(0L);
		when(memberService.getStudentsCount()).thenReturn(0L);
		when(memberService.getParentsCount()).thenReturn(0L);
		when(categoryService.getTotalCount()).thenReturn(0L);
		when(bookService.getTotalCount()).thenReturn(0L);
		when(bookService.getTotalIssuedBooks()).thenReturn(0L);

		Map<String, Long> result = homeService.getTopTilesMap();

		assertNotNull(result);
		assertEquals(Optional.of(0L).get(), result.get("totalMembers"));
		assertEquals(Optional.of(0L).get(), result.get("totalStudents"));
		assertEquals(Optional.of(0L).get(), result.get("totalParents"));
		assertEquals(Optional.of(0L).get(), result.get("totalCategories"));
		assertEquals(Optional.of(0L).get(), result.get("totalBooks"));
		assertEquals(Optional.of(0L).get(), result.get("totalIssuedBooks"));
	}

}
