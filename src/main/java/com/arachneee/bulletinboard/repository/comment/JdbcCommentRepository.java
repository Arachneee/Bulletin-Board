package com.arachneee.bulletinboard.repository.comment;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;

import java.util.Map;

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

	@Override
	public void update(Long id, String content) {
		String sql = "update comment set content = :content where comment_id = :id";

		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", id)
				.addValue("content", content);

		template.update(sql, param);
	}

	@Override
	public void delete(Long id) {
		String sql = "delete from comment where comment_id = :id";

		Map<String, Object> param = Map.of("id", id);

		template.update(sql, param);
	}

	@Override
	public Long findMemberIdByCommentId(Long commentId) {
		String sql = "select comment.member_id from comment join member on comment.member_id = member.member_id where comment.comment_id = :commentId";
		Map<String, Object> param = Map.of("commentId", commentId);

		return template.queryForObject(sql, param, Long.class);
	}

	@Override
	public String findContentById(Long commentId) {
		String sql = "select content from comment where comment_id = :commentId";
		Map<String, Object> param = Map.of("commentId", commentId);

		return template.queryForObject(sql, param, String.class);
	}
}
