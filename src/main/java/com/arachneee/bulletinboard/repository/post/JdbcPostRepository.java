package com.arachneee.bulletinboard.repository.post;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;

import org.springframework.context.annotation.Primary;
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
@Primary
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
    public List<PostPreDto> search(String searchCode, String searchString, String sortCode, Long page, Long pageSize) {
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

    // 바꿔야됨
    private RowMapper<PostPreDto> postPreDtoRowMapper() {
        return (rs, rowNum) -> PostPreDto.create(rs.getLong("post_id"),
                                                    rs.getString("title"),
                                                    rs.getString("name"),
                                                    rs.getTimestamp("create_time").toLocalDateTime(),
                                                    rs.getInt("view_count"));
    }

    @Override
    public PostViewDto findViewDtoById(Long postId) {
        String sql = "select post_id, title, content, create_time, view_count, member.name as name from post join member on post.member_id = member.member_id where post.post_id = :postId";
        Map<String, Object> param = Map.of("postId", postId);

        return template.queryForObject(sql, param, postViewDtoRowMapper());
    }

    //바꿔야됨
    private RowMapper<PostViewDto> postViewDtoRowMapper() {
        return (rs, rowNum) -> {
            PostViewDto postViewDto = new PostViewDto();

            postViewDto.setId(rs.getLong("post_id"));
            postViewDto.setTitle(rs.getString("title"));
            postViewDto.setContent(rs.getString("content"));
            postViewDto.setName(rs.getString("name"));
            postViewDto.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            postViewDto.setViewCount(rs.getInt("view_count"));

            return postViewDto;
        };
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
                     "where " + getSearchSql(searchCode) + " like '%" + searchString + "%'";

        Map<String, Object> param = new HashMap<>();

        return template.queryForObject(sql, param, Long.class);
    }
}
