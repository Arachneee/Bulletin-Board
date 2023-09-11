package com.arachneee.bulletinboard.web.controller;

import java.io.IOException;
import java.util.*;

import com.arachneee.bulletinboard.domain.SearchCode;
import com.arachneee.bulletinboard.domain.SortCode;
import com.arachneee.bulletinboard.service.CommentService;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.CommentAddForm;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostSearchForm;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.PostService;
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
	private final CommentService commentService;

	@GetMapping("")
	public String posts(PostSearchForm postSearchForm,
						HttpServletResponse response,
						Model model) throws IOException {

		Long presentPage = postSearchForm.getPage();

		if (presentPage < 1L) {
			response.sendError(400, "page 는 1이상입니다.");
			return null;
		}

		List<PostPreDto> postPreDtoList = postService.search(postSearchForm.getSearchCode(), postSearchForm.getSearchString(), postSearchForm.getSortCode(), presentPage);

		model.addAttribute("postPreDtoList", postPreDtoList);
		model.addAttribute("postSearchForm", postSearchForm);
		model.addAttribute("previous", presentPage == 1L);
		model.addAttribute("next", postService.isLastPage(postSearchForm.getSearchCode(), postSearchForm.getSearchString(), presentPage));

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

		return "redirect:/post";
	}

	@GetMapping("/{id}")
	public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id,
					   PostSearchForm postSearchForm,
					   Model model) {

		PostViewDto postViewDto = postService.viewAndFindPostViewDto(id);
		List<CommentViewDto> comments = commentService.findByPostId(id);

		model.addAttribute("postViewDto", postViewDto);
		model.addAttribute("comments", comments);
		model.addAttribute("show", postService.isNotRightMember(member.getId(), id));
		model.addAttribute("commentContent", "");

		return "post/post";
	}

	@PostMapping("/{id}")
	public String saveComment(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
							  @PathVariable Long id,
							  @RequestParam("commentContent") String commentContent) {

		if (StringUtils.isEmptyOrWhitespace(commentContent)) {
			return "redirect:/post/" + id;
		}

		commentService.save(commentContent, id, member.getId());
		log.info("댓글 저장 완료 ={}", commentContent);
		return "redirect:/post/" + id;
	}

	@GetMapping("/{id}/edit")
	public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						   @PathVariable Long id,
		Model model) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/post/{id}";
		}

		model.addAttribute("postViewDto", postService.findPostViewDto(id));
		return "post/editPostForm";
	}

	@PostMapping("/{id}/edit")
	public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
					   @PathVariable Long id,
					   @Valid PostViewDto postViewDto,
					   BindingResult bindingResult) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/post/{id}";
		}

		if (bindingResult.hasErrors()) {
			return "post/editPostForm";
		}

		postService.update(id, postViewDto.getTitle(), postViewDto.getContent());
		return "redirect:/post/{id}";
	}

	@GetMapping("/{id}/delete")
	public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
						 @PathVariable Long id,
						 Model model) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/post/{id}";
		}

		postService.delete(id);
		return "redirect:/post";
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
}
