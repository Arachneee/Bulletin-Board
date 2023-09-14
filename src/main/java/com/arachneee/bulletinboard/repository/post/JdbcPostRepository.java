package com.arachneee.bulletinboard.repository.post;

import com.arachneee.bulletinboard.domain.Comment;
import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;

import com.arachneee.bulletinboard.web.dto.PostSearchCondition;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JdbcPostRepository implements PostRepository {
    private final NamedParameterJdbcTemplate template;

    public JdbcPostRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post (title, content, member_id, create_time, view_count) values (:title, :content, :memberId, :createTime, :viewCount)";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("title", post.getTitle())
            .addValue("content", post.getContent())
            .addValue("memberId", post.getMember().getId())
            .addValue("createTime", post.getCreateTime())
            .addValue("viewCount", post.getViewCount());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);
    }

    @Override
    public Post findById(Long id) {
        return findWithCommentsById(id);
    }

    @Override
    public void update(Long postId, String title, String content) {
        String sql = "update post set title = :title, content = :content where post_id = :post_id";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("title", title)
            .addValue("content", content)
            .addValue("post_id", postId);

        template.update(sql, param);
    }

    @Override
    public void delete(Long postId) {
        String sql = "delete from post where post_id = :postId";

        Map<String, Object> param = Map.of("postId", postId);

        template.update(sql, param);
    }

    @Override
    public List<PostPreDto> search(PostSearchCondition postSearchCondition, Long pageSize) {
        String searchString = postSearchCondition.getSearchString();
        String searchCode = postSearchCondition.getSearchCode();
        String sortCode = postSearchCondition.getSortCode();
        Long page = postSearchCondition.getPage();

        log.info("Repository : searchForm = {}, {}, {}", searchCode, searchString, sortCode);
        Long skipPageSize = (page - 1L) * pageSize;

        String sql = "select post_id, title, create_time, view_count, member.name as name from post join member on post.member_id = member.member_id " +
                     "where " + getSearchSql(searchCode) + " like '%" + searchString + "%' " +
                     "order by " + getSortSql(sortCode) + " " +
                     "limit " + skipPageSize + "," + pageSize;

        return template.query(sql, postPreDtoRowMapper());
    }

    private static String getSortSql(String sortCode) {
        if (sortCode.equals("NEW")) {
            return "create_time desc";
        } else if (sortCode.equals("VIEW")) {
            return "view_count desc";
        } else { // sortCode.equals("OLD")
            return "create_time asc";
        }
    }

    private static String getSearchSql(String searchCode) {
        if (searchCode.equals("CONTENT")) {
            return "content";
        } else if (searchCode.equals("NAME")) {
            return "name";
        } else { // searchCode.equals("TITLE")
            return "title";
        }
    }

    private RowMapper<PostPreDto> postPreDtoRowMapper() {
        return (rs, rowNum) -> new PostPreDto(rs.getLong("post_id"),
                                                    rs.getString("title"),
                                                    rs.getString("name"),
                                                    rs.getTimestamp("create_time").toLocalDateTime(),
                                                    rs.getInt("view_count"));
    }


    @Override
    public void updateViewCount(Long postId, int viewCount) {
        String sql = "update post set view_count = :viewCount where post_id = :postId";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("viewCount", viewCount)
            .addValue("postId", postId);

        template.update(sql, param);
    }

    @Override
    public Long findMemberIdByPostID(Long postId) {
        String sql = "select post.member_id from post join member on post.member_id = member.member_id where post.post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);

        return template.queryForObject(sql, param, Long.class);
    }

    @Override
    public Long countAll(String searchCode, String searchString) {
        String sql = "select count(*) from post " +
                     "where :searchCode like '%:searchString%'";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("searchCode", getSearchSql(searchCode))
            .addValue("searchString", searchString);

        return template.queryForObject(sql, param, Long.class);
    }

    @Override
    public Post findWithCommentsById(Long postId) {
        String sql = "select * from post as p" +
            " join member as m on p.member_id = m.member_id" +
            " join comment as c on p.post_id = c.post_id" +
            " where p.post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);

        return template.queryForObject(sql, param, postMapper());
    }


    private RowMapper<Post> postMapper() {
        return (rs, rowNum) -> {
            Member member = Member.create(rs.getString("loginId"), rs.getString("password"), rs.getString("name"));

            Post post = Post.createRowMap(rs.getString("title"),
                rs.getString("p.content"),
                member,
                rs.getTimestamp("create_time").toLocalDateTime(),
                rs.getInt("view_count"));

            while (rs.next()) {
                Comment comment = new Comment(rs.getLong("post_id"),
                    rs.getString("c.content"),
                    post,
                    member,
                    rs.getTimestamp("c.create_time").toLocalDateTime());
            }

            return post;
        };
    }
}
