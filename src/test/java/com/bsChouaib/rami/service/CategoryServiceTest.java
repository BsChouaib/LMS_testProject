package com.bsChouaib.rami.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.bsChouaib.rami.model.Book;
import com.bsChouaib.rami.model.Category;
import com.bsChouaib.rami.repository.CategoryRepository;
import com.bsChouaib.rami.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTotalCount() {
        when(categoryRepository.count()).thenReturn(5L);

        Long totalCount = categoryService.getTotalCount();

        Assertions.assertEquals(Optional.of(5L).get(), totalCount);
    }

    @Test
    public void testGetAllBySort() {
        Category category1 = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        Category category2 = new Category(2L, "Category 2",null,null,new Date(),Arrays.asList(new Book()));
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(categories);

        List<Category> result = categoryService.getAllBySort();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Category 1", result.get(0).getName());
        Assertions.assertEquals("Category 2", result.get(1).getName());
    }

    @Test
    public void testGetAllBySortEmptyList() {
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAllBySort();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        Category category1 = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        Category category2 = new Category(2L, "Category 2",null,null,new Date(),Arrays.asList(new Book()));
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Category 1", result.get(0).getName());
        Assertions.assertEquals("Category 2", result.get(1).getName());
    }

    @Test
    public void testGetAllEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGet() {
        Long categoryId = 1L;
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.get(categoryId);

        assertNotNull(result);
        assertEquals("Category 1", result.getName());
    }

    @Test
    public void testGetNotFound() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.get(categoryId));
    }

    @Test
    public void testAddNew() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        when(categoryRepository.save(category)).thenReturn(new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book())));

        Category result = categoryService.addNew(category);

        assertNotNull(result.getId());
        assertEquals("Category 1", result.getName());
    }

    @Test
    public void testSave() {
        Category category =new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertEquals("Category 1", result.getName());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));

        assertDoesNotThrow(() -> categoryService.delete(category));
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void testDeleteById() {
        Long categoryId = 1L;

        assertDoesNotThrow(() -> categoryService.delete(categoryId));
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testHasUsageWithBooks() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));
        category.setBooks(Collections.singletonList(new Book(1L, "Book 1","tag","auth","pub","123",1,new Date(), category)));

        assertTrue(categoryService.hasUsage(category));
    }

    @Test
    public void testHasUsageWithoutBooks() {
        Category category = new Category(1L, "Category 1", null,null,new Date(),Arrays.asList(new Book()));

        assertTrue(categoryService.hasUsage(category));
    }
}
