package com.arachneee.bulletinboard.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.arachneee.bulletinboard.domain.SearchCode;
import com.arachneee.bulletinboard.web.form.SearchForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.service.PostService;
import com.arachneee.bulletinboard.web.dto.PostUpdateDto;
import com.arachneee.bulletinboard.web.session.SessionConst;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	private final PostService postService;

	@ModelAttribute("searchCodes")
	public List<SearchCode> searchCodes() {
		List<SearchCode> searchCodes = new ArrayList<>();
		searchCodes.add(new SearchCode("TITLE", "제목"));
		searchCodes.add(new SearchCode("CONTENT", "내용"));
		searchCodes.add(new SearchCode("NAME", "작성자"));
		return searchCodes;
	}

	@GetMapping("")
	public String posts(@ModelAttribute SearchForm searchForm, Model model) {
		List<Post> postList = postService.findAll();
		model.addAttribute("postList", postList);
		return "post/posts";
	}

	@PostMapping("")
	public String search(@ModelAttribute SearchForm searchForm, Model model) {
		log.info("searchForm = {}, {}", searchForm.getSearchCode(), searchForm.getSearchString());
		List<Post> postList = postService.search(searchForm);
		model.addAttribute("postList", postList);
		return "post/posts";
	}

	@GetMapping("/add")
	public String addPost(@ModelAttribute Post post) {
		log.info("Get : /post/add 호출");
		return "post/addPostForm";
	}

	@PostMapping("/add")
	public String savePost(@Valid @ModelAttribute Post post, BindingResult bindingResult) {
		log.info("Post : /post/add 호출");

		if (bindingResult.hasErrors()) {
			return "post/addPostForm";
		}

		postService.save(post);
		log.info("post save 완료");

		return "redirect:/post";
	}

	@GetMapping("/{id}")
	public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						@PathVariable Long id,
						Model model) {

		Post findPost = postService.view(id);
		model.addAttribute("post", findPost);
		log.info("post view={}", id);

		boolean show = true;

		if (postService.isNotRightMember(member, id)) {
			show = false;
		}

		model.addAttribute("show", show);
		return "post/post";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
		@PathVariable Long id,
		Model model) {

		if (postService.isNotRightMember(member, id)) {
			return "redirect:/post/{id}";
		}

		Post findPost = postService.findById(id);
		model.addAttribute("post", findPost);
		return "post/editPostForm";
	}

	@PostMapping("/{id}/edit")
	public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
		@PathVariable Long id, @ModelAttribute PostUpdateDto postUpdateDto) {

		if (postService.isNotRightMember(member, id)) {
			return "redirect:/post/{id}";
		}

		postService.update(id, postUpdateDto);
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