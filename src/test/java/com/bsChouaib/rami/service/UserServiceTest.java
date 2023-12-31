package com.bsChouaib.rami.service;

import java.util.*;

import com.bsChouaib.rami.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bsChouaib.rami.model.User;
import com.bsChouaib.rami.repository.UserRepository;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllUsers() {
		User user1 = new User();
		User user2 = new User();
		List<User> users = Arrays.asList(user1, user2);

		when(userRepository.findAllByOrderByDisplayNameAsc()).thenReturn(users);

		List<User> result = userService.getAllUsers();

		assertEquals(2, result.size());
		assertSame(user1, result.get(0));
		assertSame(user2, result.get(1));
	}

	@Test
	public void testGetAllUsersEmptyList() {
		when(userRepository.findAllByOrderByDisplayNameAsc()).thenReturn(Collections.emptyList());

		List<User> result = userService.getAllUsers();

		assertTrue(result.isEmpty());
	}

	@Test
	public void testGetAllActiveUsers() {
		User user1 = new User();
		User user2 = new User();
		List<User> activeUsers = Arrays.asList(user1, user2);

		when(userRepository.findAllByActiveOrderByDisplayNameAsc(1)).thenReturn(activeUsers);

		List<User> result = userService.getAllActiveUsers();

		assertEquals(2, result.size());
		assertSame(user1, result.get(0));
		assertSame(user2, result.get(1));
	}

	@Test
	public void testGetByUsername() {
		String username = "testuser";
		User user = new User();
		user.setUsername(username);

		when(userRepository.findByUsername(username)).thenReturn(user);

		User result = userService.getByUsername(username);

		assertNotNull(result);
		assertSame(user, result);
	}

	@Test
	public void testGetById() {
		Long userId = 1L;
		User user = new User();
		user.setId(userId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		User result = userService.getById(userId);

		assertNotNull(result);
		assertSame(user, result);
	}

	@Test
	public void testGetByIdNotFound() {
		Long userId = 1L;

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> userService.getById(userId));
	}

	@Test
	public void testAddNew() {
		User user = new User("Test User", "testuser", "password", "ROLE_USER");
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(user);

		User result = userService.addNew(user);

		assertSame(user, result);
		assertNotNull(user.getCreatedDate());
		assertNotNull(user.getLastModifiedDate());
		assertEquals("encodedPassword", user.getPassword());
		assertEquals(Optional.of(1).get(), user.getActive());
	}

	@Test
	public void testUpdate() {
		User user = new User();
		when(userRepository.save(user)).thenReturn(user);

		User result = userService.update(user);

		assertSame(user, result);
		assertNotNull(user.getLastModifiedDate());
	}

}

