package com.arachneee.bulletinboard.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class CommentEmpathy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_empathy_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected CommentEmpathy() {
    }

    public static CommentEmpathy create(Comment comment, Member member) {
        CommentEmpathy commentEmpathy = new CommentEmpathy();
        commentEmpathy.setComment(comment);
        commentEmpathy.setMember(member);

        return commentEmpathy;
    }


    private void setComment(Comment comment) {
        this.comment = comment;
        comment.addEmpathy(this);
    }

    private void setMember(Member member) {
        this.member = member;
    }

    public Long getMemberId() {
        return member.getId();
    }
}
