package com.arachneee.bulletinboard.web.controller;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.service.CommentService;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;
import com.arachneee.bulletinboard.web.search.PostSearchCondition;
import com.arachneee.bulletinboard.web.form.CommentAddForm;
import com.arachneee.bulletinboard.web.session.SessionConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public String saveComment(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                              @PathVariable Long postId,
                              @RequestParam("commentContent") String commentContent,
                              PostSearchCondition postSearchCondition,
                              CommentSearchCondition commentSearchCondition,
                              Model model) {

        if (StringUtils.isEmptyOrWhitespace(commentContent)) {
            return "redirect:/posts/{postId}";
        }

        commentService.save(commentContent, postId, member);
        log.info("댓글 저장 완료 ={}", commentContent);
        log.info("postSearchCondition = {}", postSearchCondition);
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/edit")
    public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                           @PathVariable Long postId,
                           @PathVariable Long commentId,
                           PostSearchCondition postSearchCondition,
                           CommentSearchCondition commentSearchCondition,
                           Model model) {

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        log.info("get edit postSearchCondition={}", postSearchCondition.toQueryString());
        CommentAddForm commentAddForm = new CommentAddForm(commentService.findContentById(commentId));
        model.addAttribute("commentAddForm", commentAddForm);
        log.info("@GetMapping(\"/{commentId}/edit\") 완료 = {}", commentAddForm.getCommentContent());
        return "post/comment/editCommentForm";
    }

    @PostMapping("/{commentId}/edit")
    public String editForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                           @PathVariable Long postId,
                           @PathVariable Long commentId,
                           @Valid CommentAddForm commentAddForm,
                           PostSearchCondition postSearchCondition,
                           CommentSearchCondition commentSearchCondition,
                           BindingResult bindingResult) {

        log.info("post edit postSearchCondition={}", postSearchCondition.toQueryString());

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        if (bindingResult.hasErrors()) {
            return "post/comment/editCommentForm";
        }

        log.info("contents update start");
        commentService.update(commentId, commentAddForm.getCommentContent());
        log.info("contents update finish");
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/delete")
    public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                         @PathVariable Long postId,
                         PostSearchCondition postSearchCondition,
                         CommentSearchCondition commentSearchCondition,
                         @PathVariable Long commentId) {

        if (commentService.isNotRightMember(member.getId(), commentId)) {
            return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
        }

        log.info("contents delete start");
        commentService.delete(commentId);
        log.info("contents delete finish");
        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

    @GetMapping("/{commentId}/empathy")
    public String empathy(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                          @PathVariable Long postId,
                          PostSearchCondition postSearchCondition,
                          CommentSearchCondition commentSearchCondition,
                          @PathVariable Long commentId) {

        commentService.empathy(commentId, member);

        return "redirect:/posts/{postId}?" + postSearchCondition.toQueryString() + "&" + commentSearchCondition.toQueryString();
    }

}
