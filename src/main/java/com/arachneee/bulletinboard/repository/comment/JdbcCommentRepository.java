package com.arachneee.bulletinboard.repository.comment;

import javax.sql.DataSource;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.web.search.CommentSearchCondition;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.repository.CommentRepository;

import java.util.List;
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
	public Comment findById(Long id) {
		String sql = "select * from comment c" +
				" join member m on c.member_id = m.member_id" +
				" join post p on c.post_id = p.post_id" +
				" where c.comment_id = :id";

		Map<String, Object> param = Map.of("id", id);

		return template.queryForObject(sql, param, commentRowMapper());
	}

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
		String sql = "select comment.member_id from comment" +
					" join member on comment.member_id = member.member_id" +
					" where comment.comment_id = :commentId";
		Map<String, Object> param = Map.of("commentId", commentId);

		return template.queryForObject(sql, param, Long.class);
	}

	@Override
	public String findContentById(Long commentId) {
		String sql = "select content from comment" +
				" where comment_id = :commentId";
		Map<String, Object> param = Map.of("commentId", commentId);

		return template.queryForObject(sql, param, String.class);
	}

	@Override
	public List<Comment> findCommentsByPostId(Long postId, CommentSearchCondition commentSearchCondition, Integer commentPageSize) {
		String sql = "select * from comment c" +
				" join member m on c.member_id = m.member_id" +
				" join post p on c.post_id = p.post_id" +
				" where p.post_id = :postId" +
				" order by c.create_time " + getSortCode(commentSearchCondition);

		Map<String, Object> param = Map.of("postId", postId);

		return template.query(sql, param, commentRowMapper());
	}

	private static String getSortCode(CommentSearchCondition commentSearchCondition) {
		return commentSearchCondition.getCommentSortCode().equals("NEW") ? "desc" : "asc";
	}

	private RowMapper<Comment> commentRowMapper() {
		return (rs, rowNum) -> {
			Member member = Member.create(rs.getString("m.loginId"), rs.getString("m.password"), rs.getString("m.name"));

			Post post = Post.createRowMap(rs.getString("p.title"),
					rs.getString("p.content"),
					member,
					rs.getTimestamp("p.create_time").toLocalDateTime(),
					rs.getInt("p.view_count"));

			return new Comment(rs.getLong("post_id"),
						rs.getString("c.content"),
						post,
						member,
						rs.getTimestamp("c.create_time").toLocalDateTime());
		};
	}

	@Override
	public Long countByPostId(Long postId) {
		String sql = "select count(*) from comment where post_id = :postId";

		Map<String, Object> param = Map.of("postId", postId);

		return template.queryForObject(sql, param, Long.class);
	}
}
