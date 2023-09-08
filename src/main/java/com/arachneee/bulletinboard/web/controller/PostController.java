package com.arachneee.bulletinboard.web.controller;

import java.util.*;

import com.arachneee.bulletinboard.domain.SearchCode;
import com.arachneee.bulletinboard.domain.SortCode;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostSearchForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.PostService;
import com.arachneee.bulletinboard.web.session.SessionConst;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	private final PostService postService;

	@ModelAttribute("sortCodes")
	public List<SortCode> sortCodes() {
		List<SortCode> sortCodes = new ArrayList<>();
		sortCodes.add(new SortCode("NEW", "최신순"));
		sortCodes.add(new SortCode("OLD", "오래된순"));
		sortCodes.add(new SortCode("VIEW", "조회순"));
		return sortCodes;
	}

	@ModelAttribute("searchCodes")
	public List<SearchCode> searchCodes() {
		List<SearchCode> searchCodes = new ArrayList<>();
		searchCodes.add(new SearchCode("TITLE", "제목"));
		searchCodes.add(new SearchCode("CONTENT", "내용"));
		searchCodes.add(new SearchCode("NAME", "작성자"));
		return searchCodes;
	}

	@GetMapping("")
	public String posts(@CookieValue(name = "searchCode", defaultValue = "TITLE") String searchCode,
						@CookieValue(name = "searchString", defaultValue = "") String searchString,
						@CookieValue(name = "sortCode", defaultValue = "NEW") String sortCode,
						Model model) {

		PostSearchForm postSearchForm = new PostSearchForm(searchCode, searchString, sortCode);
		model.addAttribute("postSearchForm", postSearchForm);

		List<PostPreDto> postPreDtoList = postService.search(searchCode, searchString, sortCode);
		model.addAttribute("postPreDtoList", postPreDtoList);

		return "post/posts";
	}

	@PostMapping("")
	public String search(@ModelAttribute PostSearchForm postSearchForm, Model model, HttpServletResponse response) {
		List<PostPreDto> postPreDtoList = postService.search(postSearchForm.getSearchCode(), postSearchForm.getSearchString(), postSearchForm.getSortCode());

		model.addAttribute("postPreDtoList", postPreDtoList);
		model.addAttribute("postSearchForm", postSearchForm);

		addSearchFormCookies(postSearchForm, response);

		return "post/posts";
	}

	private static void addSearchFormCookies(PostSearchForm postSearchForm, HttpServletResponse response) {
		addObjectCookie("searchCode", postSearchForm.getSearchCode(), response);
		addObjectCookie("searchString", postSearchForm.getSearchString(), response);
		addObjectCookie("sortCode", postSearchForm.getSortCode(), response);
	}

	private static void addObjectCookie(String searchCode, String postSearchCookieName, HttpServletResponse response) {
		Cookie searchCodeCookie = new Cookie(searchCode, postSearchCookieName);
		searchCodeCookie.setPath("/post");
		response.addCookie(searchCodeCookie);
	}

	@GetMapping("/add")
	public String addPostForm(@ModelAttribute PostAddForm postAddForm) {
		log.info("Get : /post/add 호출");
		return "post/addPostForm";
	}

	@PostMapping("/add")
	public String savePost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						   @Valid @ModelAttribute PostAddForm postAddForm, BindingResult bindingResult) {

		log.info("Post : /post/add 호출");

		if (bindingResult.hasErrors()) {
			return "post/addPostForm";
		}

		postService.save(postAddForm.getTitle(), postAddForm.getContent(), member);
		log.info("post save 완료");

		return "redirect:/post";
	}

	@GetMapping("/{id}")
	public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id,
						Model model) {

		PostViewDto postViewDto = postService.viewAndFindPostViewDto(id);

		model.addAttribute("post", postViewDto);
		log.info("post view={}", id);

		model.addAttribute("show", postService.isNotRightMember(member, id));
		return "post/post";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						   @PathVariable Long id,
		Model model) {

		if (postService.isNotRightMember(member, id)) {
			return "redirect:/post/{id}";
		}

		model.addAttribute("post", postService.findPostViewDto(id));
		return "post/editPostForm";
	}

	@PostMapping("/{id}/edit")
	public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id, @ModelAttribute PostAddForm postAddForm) {

		if (postService.isNotRightMember(member, id)) {
			return "redirect:/post/{id}";
		}

		postService.update(id, postAddForm.getTitle(), postAddForm.getContent());
		return "redirect:/post/{id}";
	}

	@GetMapping("/{id}/delete")
	public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						 @PathVariable Long id,
						   Model model) {

		if (postService.isNotRightMember(member, id)) {
			return "redirect:/post/{id}";
		}

		postService.delete(id);
		return "redirect:/post";
	}
}
