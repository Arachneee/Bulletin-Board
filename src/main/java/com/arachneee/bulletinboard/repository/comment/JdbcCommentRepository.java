package com.arachneee.bulletinboard.repository.comment;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;
import com.arachneee.bulletinboard.web.dto.CommentViewDto;

@Repository
@Primary
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
	public List<CommentViewDto> findByPostId(Long postId) {
		String sql = "select member.name as name, content, create_time from comment join member where post.member_id = member.member_id where post_id = :postId";
		Map<String, Object> param = Map.of("postId", postId);

		return template.query(sql, param, commentViewDtoRowMapper());
	}

	private RowMapper<CommentViewDto> commentViewDtoRowMapper() {
		return (rs, rowNum) -> CommentViewDto.create(rs.getString("content"),
														rs.getString("name"),
														rs.getTimestamp("create_time").toLocalDateTime());
	}
}
