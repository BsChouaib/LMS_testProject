package com.bsChouaib.rami.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.bsChouaib.rami.common.Constants;
import com.bsChouaib.rami.model.Member;
import com.bsChouaib.rami.repository.MemberRepository;
import com.bsChouaib.rami.service.IssueService;
import com.bsChouaib.rami.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private IssueService issueService;

	@InjectMocks
	private MemberService memberService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetTotalCount() {
		when(memberRepository.count()).thenReturn(5L);

		Long totalCount = memberService.getTotalCount();

		assertEquals(Optional.of(5L).get(), totalCount);
	}

	@Test
	public void testGetParentsCount() {
		when(memberRepository.countByType(Constants.MEMBER_PARENT)).thenReturn(3L);

		Long parentsCount = memberService.getParentsCount();

		assertEquals(Optional.of(3L).get(), parentsCount);
	}

	@Test
	public void testGetStudentsCount() {
		when(memberRepository.countByType(Constants.MEMBER_STUDENT)).thenReturn(2L);

		Long studentsCount = memberService.getStudentsCount();

		assertEquals(Optional.of(2L).get(), studentsCount);
	}

	@Test
	public void testGetAll() {
		Member member1 = new Member();
		Member member2 = new Member();
		List<Member> members = Arrays.asList(member1, member2);

		when(memberRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc()).thenReturn(members);

		List<Member> result = memberService.getAll();

		assertEquals(2, result.size());
		assertSame(member1, result.get(0));
		assertSame(member2, result.get(1));
	}

	@Test
	public void testGetAllEmptyList() {
		when(memberRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc()).thenReturn(Collections.emptyList());

		List<Member> result = memberService.getAll();

		assertTrue(result.isEmpty());
	}

	@Test
	public void testGet() {
		Long memberId = 1L;
		Member member = new Member();
		member.setId(memberId);

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

		Member result = memberService.get(memberId);

		assertNotNull(result);
		assertSame(member, result);
	}

	@Test
	public void testGetNotFound() {
		Long memberId = 1L;

		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> memberService.get(memberId));
	}

	@Test
	public void testAddNew() {
		Member member = new Member();
		when(memberRepository.save(member)).thenReturn(member);

		Member result = memberService.addNew(member);

		assertSame(member, result);
		assertNotNull(member.getJoiningDate());
	}

	@Test
	public void testSave() {
		Member member = new Member();
		when(memberRepository.save(member)).thenReturn(member);

		Member result = memberService.save(member);

		assertSame(member, result);
	}

	@Test
	public void testDeleteMember() {
		Member member = new Member();

		assertDoesNotThrow(() -> memberService.delete(member));
		verify(memberRepository, times(1)).delete(member);
	}

	@Test
	public void testDeleteById() {
		Long memberId = 1L;

		assertDoesNotThrow(() -> memberService.delete(memberId));
		verify(memberRepository, times(1)).deleteById(memberId);
	}

	@Test
	public void testHasUsageWithIssueCountGreaterThanZero() {
		Member member = new Member();
		when(issueService.getCountByMember(member)).thenReturn(1L);

		boolean hasUsage = memberService.hasUsage(member);

		assertTrue(hasUsage);
	}

	@Test
	public void testHasUsageWithIssueCountZero() {
		Member member = new Member();
		when(issueService.getCountByMember(member)).thenReturn(0L);

		boolean hasUsage = memberService.hasUsage(member);

		assertFalse(hasUsage);
	}

	// Add more test cases for edge cases, exceptions, etc.
}

