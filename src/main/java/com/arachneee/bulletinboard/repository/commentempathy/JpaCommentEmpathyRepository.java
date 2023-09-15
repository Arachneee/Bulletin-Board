package com.arachneee.bulletinboard.repository.commentempathy;

import com.arachneee.bulletinboard.domain.CommentEmpathy;
import com.arachneee.bulletinboard.repository.CommentEmpathyRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Primary
public class JpaCommentEmpathyRepository implements CommentEmpathyRepository {

    private final EntityManager em;

    @Override
    public void save(CommentEmpathy commentEmpathy) {
        em.persist(commentEmpathy);
    }
}
