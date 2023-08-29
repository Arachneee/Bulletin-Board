package com.arachneee.bulletinboard.web.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arachneee.bulletinboard.domain.post.Post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	@GetMapping
	public String postMain() {  // TODOLIST : 정렬 기준을 담을 수 있다.
		return "post/main";
	}

	@GetMapping("/add")
	public String addPost(@ModelAttribute Post post) {
		return "post/addPostForm";
	}

	@PostMapping("/add")
	public String savePost(@ModelAttribute Post post) {
		return "redirect:/post";
	}
}
