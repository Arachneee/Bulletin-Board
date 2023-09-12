package com.arachneee.bulletinboard.repository.comment;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;

@Repository
public class JdbcCommentRepository implements CommentRepository {

	private final NamedParameterJdbcTemplate template;

	public JdbcCommentRepository(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void save(Comment comment) {
		String sql = "insert into comment (post_id, member_id, content, create_time) values (:postId, :memberId, :content, :createTime)";

		SqlParameterSource param = new MapSqlParameterSource()
			.addValue("postId", comment.getPost().getId())
			.addValue("memberId", comment.getMember().getId())
			.addValue("content", comment.getContent())
			.addValue("createTime", comment.getCreateTime());

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		template.update(sql, param, keyHolder);
	}
}
