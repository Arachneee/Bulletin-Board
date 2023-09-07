package com.arachneee.bulletinboard.repository.post;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.PostSearchForm;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    public Post save(Post post) {
        String sql = "insert into post (title, content, member_id, create_time, view_count) values (:title, :content, :memberId, :createTime, :viewCount)";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("title", post.getTitle())
            .addValue("content", post.getContent())
            .addValue("memberId", post.getMember().getId())
            .addValue("createTime", post.getCreateTime())
            .addValue("viewCount", post.getViewCount());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        post.setId(key);

        return post;
    }

    @Override
    public void update(Long id, PostAddForm postAddForm) {
        String sql = "update post set title = :title, content = :content where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("title", postAddForm.getTitle())
            .addValue("content", postAddForm.getContent())
            .addValue("id", id);

        template.update(sql, param);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from post where id = :id";

        Map<String, Object> param = Map.of("id", id);

        template.update(sql, param);
    }

    @Override
    public List<PostPreDto> search(PostSearchForm postSearchForm) {
        String searchCode = postSearchForm.getSearchCode();
        String searchString = postSearchForm.getSearchString();
        String sortCode = postSearchForm.getSortCode();

        log.info("Repository : searchForm = {}, {}, {}", searchCode, searchString, sortCode);

        String sql = "select post.id, title, content, create_time, view_count, member.name as name from post join member on post.member_id = member.id " +
                     "where " + getSearchSql(searchCode) + " like '%" + searchString + "%' " +
                     "order by " + getSortSql(sortCode);

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
        return BeanPropertyRowMapper.newInstance(PostPreDto.class);
    }

    @Override
    public PostViewDto findViewDtoById(Long id) {
        String sql = "select post.id, title, content, create_time, view_count, member.id as name from post join member on post.member_id = member.id where post.id = :id";
        Map<String, Object> param = Map.of("id", id);

        return template.queryForObject(sql, param, postViewDtoRowMapper());
    }

    private RowMapper<PostViewDto> postViewDtoRowMapper() {
        return BeanPropertyRowMapper.newInstance(PostViewDto.class);
    }

    @Override
    public void updateViewCount(Long id, int viewCount) {
        String sql = "update post set view_count = :viewCount where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("viewCount", viewCount)
            .addValue("id", id);

        template.update(sql, param);
    }

    @Override
    public Long findMemberIdByPostID(Long id) {
        String sql = "select member.id from post join member on post.member_id = member.id where post.id = :id";
        Map<String, Object> param = Map.of("id", id);

        return template.queryForObject(sql, param, Long.class);
    }
}
