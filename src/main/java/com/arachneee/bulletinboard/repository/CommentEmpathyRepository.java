package com.arachneee.bulletinboard.repository;

import com.arachneee.bulletinboard.domain.CommentEmpathy;
import org.springframework.stereotype.Repository;


public interface CommentEmpathyRepository {
    public void save(CommentEmpathy commentEmpathy);
}
