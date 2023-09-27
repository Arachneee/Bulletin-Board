package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.CommentService;
import com.arachneee.bulletinboard.web.argumentresolver.Login;
import com.arachneee.bulletinboard.web.form.CommentAddForm;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;
import com.arachneee.bulletinboard.web.search.PostSearchCondition;
import com.arachneee.bulletinboard.web.session.SessionConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public String saveComment(@Login Member member,
                              @PathVariable Long postId,
                              @RequestParam("commentContent") String commentContent,
                              PostSearchCondition postSearchCondition,
                              CommentSearchCondition commentSearchCondition) {

        if (StringUtils.isEmptyOrWhitespace(commentContent)) {
            return "redirect:/posts/{postId}";
        }

        commentService.save(commentContent, postId, member);
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/edit")
    public String editForm(@Login Member member,
                           @PathVariable Long postId,
                           @PathVariable Long commentId,
                           PostSearchCondition postSearchCondition,
                           CommentSearchCondition commentSearchCondition,
                           Model model) {

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        CommentAddForm commentAddForm = new CommentAddForm(commentService.findContentById(commentId));
        model.addAttribute("commentAddForm", commentAddForm);
        return "post/comment/editCommentForm";
    }

    @PostMapping("/{commentId}/edit")
    public String editForm(@Login Member member,
                           @PathVariable Long commentId,
                           @Valid CommentAddForm commentAddForm,
                           PostSearchCondition postSearchCondition,
                           CommentSearchCondition commentSearchCondition,
                           BindingResult bindingResult) {

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        if (bindingResult.hasErrors()) {
            return "post/comment/editCommentForm";
        }

        commentService.update(commentId, commentAddForm.getCommentContent());
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/delete")
    public String delete(@Login Member member,
                         PostSearchCondition postSearchCondition,
                         CommentSearchCondition commentSearchCondition,
                         @PathVariable Long commentId) {

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        commentService.delete(commentId);
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/empathy")
    public String empathy(@Login Member member,
                          PostSearchCondition postSearchCondition,
                          CommentSearchCondition commentSearchCondition,
                          @PathVariable Long commentId) {

        commentService.empathy(commentId, member);
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

}
