package com.arachneee.bulletinboard.web.controller;

import java.io.IOException;
import java.util.*;

import com.arachneee.bulletinboard.web.dto.*;
import com.arachneee.bulletinboard.web.form.PostAddForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.PostService;
import com.arachneee.bulletinboard.web.session.SessionConst;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@GetMapping("")
	public String posts(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						PostSearchCondition postSearchCondition,
						HttpServletResponse response,
						HttpServletRequest request,
						Model model) throws IOException {

		Long presentPage = postSearchCondition.getPage();

		if (presentPage < 1L) {
			response.sendError(400, "page 는 1이상입니다.");
			return null;
		}

		List<PostPreDto> postPreDtoList = postService.search(postSearchCondition);

		model.addAttribute("queryString", request.getQueryString());
		model.addAttribute("memberName", member.getName());
		model.addAttribute("postPreDtoList", postPreDtoList);
		model.addAttribute("postSearchCondition", postSearchCondition);
		model.addAttribute("previous", presentPage == 1L);
		model.addAttribute("next", postService.isLastPage(postSearchCondition.getSearchCode(), postSearchCondition.getSearchString(), presentPage));

		return "post/posts";
	}

	@GetMapping("/add")
	public String addPostForm(PostAddForm postAddForm) {
		log.info("Get : /post/add 호출");
		return "post/addPostForm";
	}

	@PostMapping("/add")
	public String savePost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						   @Valid PostAddForm postAddForm,
						   BindingResult bindingResult) {

		log.info("Post : /post/add 호출");

		if (bindingResult.hasErrors()) {
			return "post/addPostForm";
		}

		postService.save(postAddForm.getTitle(), postAddForm.getContent(), member);
		log.info("post save 완료");

		return "redirect:/posts";
	}

	@GetMapping("/{id}")
	public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id,
					   PostSearchCondition postSearchCondition,
					   CommentSearchCondition commentSearchCondition,
					   HttpServletResponse response,
					   Model model) throws IOException {

		log.info("commentSearchCondition = {}, {}", commentSearchCondition.getCommentSortCode(), commentSearchCondition.getCommentPage());

		Integer commentPage = commentSearchCondition.getCommentPage();

		if (commentPage < 1) {
			response.sendError(400, "page 는 1이상입니다.");
			return null;
		}

		PostViewDto postViewDto = postService.viewAndFindPostViewDto(id, member.getId(), commentSearchCondition);

		log.info("PostViewDto = {} ", postViewDto);

		model.addAttribute("postSearchCondition", postSearchCondition);
		model.addAttribute("postViewDto", postViewDto);
		model.addAttribute("show", member.getName().equals(postViewDto.getName()));
		model.addAttribute("commentContent", "");
		model.addAttribute("memberName", member.getName());
		model.addAttribute("commentSearchCondition", commentSearchCondition);
		model.addAttribute("previous", commentPage == 1);
		model.addAttribute("next", postService.isLastCommentPage(id, commentPage));

		return "post/post";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						   @PathVariable Long id,
						   Model model) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}

		model.addAttribute("postEditDto", postService.findPostEditDto(id));
		log.info("Get : post edit 호출");

		return "post/editPostForm";
	}

	@PostMapping("/{id}/edit")
	public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id,
					   @Valid PostEditDto postEditDto,
					   PostSearchCondition postSearchCondition,
					   BindingResult bindingResult) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}

		if (bindingResult.hasErrors()) {
			return "post/editPostForm";
		}

		postService.update(id, postEditDto.getTitle(), postEditDto.getContent());
		return "redirect:/posts/{id}?" + postSearchCondition.toQueryString();
	}

	@GetMapping("/{id}/delete")
	public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						 @PathVariable Long id,
						 PostSearchCondition postSearchCondition,
						 Model model) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}

		postService.delete(id);
		return "redirect:/posts?" + postSearchCondition.toQueryString();
	}

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

	@ModelAttribute("commentSortCodes")
	public List<CommentSortCode> commentSortCodes() {
		List<CommentSortCode> commentSortCodes = new ArrayList<>();
		commentSortCodes.add(new CommentSortCode("NEW", "최신순"));
		commentSortCodes.add(new CommentSortCode("OLD", "등록순"));
		return commentSortCodes;
	}

	@Getter
	@AllArgsConstructor
	static class CommentSortCode {
		private String code;
		private String displayName;
	}

	@Getter
	@AllArgsConstructor
	static class SortCode {
		private String code;
		private String displayName;
	}

	@Getter
	@AllArgsConstructor
	static class SearchCode {

		private String code;
		private String displayName;
	}

}
