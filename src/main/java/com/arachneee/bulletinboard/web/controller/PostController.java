package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.service.CommentService;
import com.arachneee.bulletinboard.service.PostService;
import com.arachneee.bulletinboard.web.argumentresolver.Login;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostEditForm;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;
import com.arachneee.bulletinboard.web.search.PostSearchCondition;
import com.arachneee.bulletinboard.web.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final CommentService commentService;

	@GetMapping("")
	public String posts(@Login Member member,
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
		model.addAttribute("next", postService.isLastPage(postSearchCondition, presentPage));
		model.addAttribute("postSortCodes", PostSortCode.values());
		model.addAttribute("postSearchCodes", PostSearchCode.values());

		return "post/posts";
	}

	@GetMapping("/add")
	public String addPostForm(PostAddForm postAddForm) {
		return "post/addPostForm";
	}

	@PostMapping("/add")
	public String savePost(@Login Member member,
						   @Valid PostAddForm postAddForm,
						   BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "post/addPostForm";
		}

		postService.save(postAddForm.getTitle(), postAddForm.getContent(), member);
		return "redirect:/posts";
	}

	@GetMapping("/{id}")
	public String post(@Login Member member,
					   @PathVariable Long id,
					   PostSearchCondition postSearchCondition,
					   CommentSearchCondition commentSearchCondition,
					   HttpServletResponse response,
					   Model model) throws IOException {

		Integer commentPage = commentSearchCondition.getCommentPage();

		if (commentPage < 1) {
			response.sendError(400, "page 는 1이상입니다.");
			return null;
		}

		postService.view(id);

		PostViewDto postViewDto = postService.findPostViewDto(id, member.getId(), commentSearchCondition);

		Optional<CommentViewDto> bestComment = Optional.empty();

		if (commentPage == 1) {
			Optional<Comment> comment = commentService.getBestComment(id);

			if (comment.isPresent()) {
				bestComment = Optional.of(CommentViewDto.from(comment.get(), member.getId()));
			}
		}

		model.addAttribute("bestComment", bestComment);
		model.addAttribute("postSearchCondition", postSearchCondition);
		model.addAttribute("postViewDto", postViewDto);
		model.addAttribute("show", member.isSameName(postViewDto.getName()));
		model.addAttribute("commentContent", "");
		model.addAttribute("memberName", member.getName());
		model.addAttribute("commentSearchCondition", commentSearchCondition);
		model.addAttribute("previous", commentPage == 1);
		model.addAttribute("next", postService.isLastCommentPage(id, commentPage));
		model.addAttribute("commentSortCodes", CommentSortCode.values());

		return "post/post";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@Login Member member,
						   @PathVariable Long id,
						   Model model) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}
		Post post = postService.findPost(id);

		model.addAttribute("postEditForm", new PostEditForm(post));

		return "post/editPostForm";
	}

	@PostMapping("/{id}/edit")
	public String edit(@Login Member member,
					   @PathVariable Long id,
					   PostSearchCondition postSearchCondition,
					   @Valid PostEditForm postEditForm,
					   BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "post/editPostForm";
		}

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}

		postService.update(id, postEditForm.getTitle(), postEditForm.getContent());
		return "redirect:/posts/{id}?" + postSearchCondition.toQueryString();
	}

	@GetMapping("/{id}/delete")
	public String delete(@Login Member member,
						 @PathVariable Long id,
						 PostSearchCondition postSearchCondition) {

		if (postService.isNotRightMember(member.getId(), id)) {
			return "redirect:/posts/{id}";
		}

		postService.delete(id);
		return "redirect:/posts?" + postSearchCondition.toQueryString();
	}


	public enum CommentSortCode {
		NEW("최신순"), OLD("등록순");

		private final String label;

		CommentSortCode(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public enum PostSearchCode {
		TITLE("제목"), CONTENT("내용"), NAME("작성자");

		private final String label;

		PostSearchCode(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public enum PostSortCode {
		NEW("최신순"), OLD("오래된순"), VIEW("조회순");

		private final String label;

		PostSortCode(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}
}
