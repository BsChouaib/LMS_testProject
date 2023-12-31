package com.bsChouaib.rami.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.bsChouaib.rami.common.Constants;
import com.bsChouaib.rami.model.Issue;
import com.bsChouaib.rami.model.Member;
import com.bsChouaib.rami.repository.IssueRepository;
import com.bsChouaib.rami.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IssueServiceTest {

	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueService issueService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAll() {
		Issue issue1 = new Issue();
		Issue issue2 = new Issue();
		List<Issue> issues = Arrays.asList(issue1, issue2);

		when(issueRepository.findAll()).thenReturn(issues);

		List<Issue> result = issueService.getAll();

		assertEquals(2, result.size());
		assertSame(issue1, result.get(0));
		assertSame(issue2, result.get(1));
	}

	@Test
	public void testGetAllEmptyList() {
		when(issueRepository.findAll()).thenReturn(Collections.emptyList());

		List<Issue> result = issueService.getAll();

		assertTrue(result.isEmpty());
	}

	@Test
	public void testGet() {
		Long issueId = 1L;
		Issue issue = new Issue();
		issue.setId(issueId);

		when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));

		Issue result = issueService.get(issueId);

		assertNotNull(result);
		assertSame(issue, result);
	}

	@Test
	public void testGetNotFound() {
		Long issueId = 1L;

		when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> issueService.get(issueId));
	}

	@Test
	public void testGetAllUnreturned() {
		Issue issue1 = new Issue();
		Issue issue2 = new Issue();
		List<Issue> unreturnedIssues = Arrays.asList(issue1, issue2);

		when(issueRepository.findByReturned(Constants.BOOK_NOT_RETURNED)).thenReturn(unreturnedIssues);

		List<Issue> result = issueService.getAllUnreturned();

		assertEquals(2, result.size());
		assertSame(issue1, result.get(0));
		assertSame(issue2, result.get(1));
	}

	@Test
	public void testAddNew() {
		Issue issue = new Issue();
		when(issueRepository.save(issue)).thenReturn(issue);

		Issue result = issueService.addNew(issue);

		assertSame(issue, result);
		assertNotNull(issue.getIssueDate());
		assertEquals(Constants.BOOK_NOT_RETURNED, issue.getReturned());
	}

	@Test
	public void testSave() {
		Issue issue = new Issue();
		when(issueRepository.save(issue)).thenReturn(issue);

		Issue result = issueService.save(issue);

		assertSame(issue, result);
	}

	@Test
	public void testGetCountByMember() {
		Member member = new Member();
		when(issueRepository.countByMemberAndReturned(member, Constants.BOOK_NOT_RETURNED)).thenReturn(3L);

		Long result = issueService.getCountByMember(member);

		assertEquals(Optional.of(3L).get(), result);
	}
}
