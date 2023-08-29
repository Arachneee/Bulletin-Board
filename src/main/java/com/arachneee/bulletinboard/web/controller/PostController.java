package com.arachneee.bulletinboard.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	private final PostService postService;

	@GetMapping("")
	public String postMain(Model model) {
		List<Post> postList = postService.findAll();
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
	public String post(@PathVariable long id, Model model) {
		Post findPost = postService.findById(id);
		model.addAttribute("post", findPost);
		log.info("post view={}", id);
		return "post/post";
	}

}
