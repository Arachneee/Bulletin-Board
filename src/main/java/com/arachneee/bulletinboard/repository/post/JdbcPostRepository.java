package com.arachneee.bulletinboard.repository.post;

import com.arachneee.bulletinboard.domain.Post;
import com.arachneee.bulletinboard.repository.PostRepository;
import com.arachneee.bulletinboard.web.dto.PostPreDto;
import com.arachneee.bulletinboard.web.dto.PostViewDto;
import com.arachneee.bulletinboard.web.form.PostAddForm;
import com.arachneee.bulletinboard.web.form.SearchForm;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class JdbcPostRepository implements PostRepository {
    private final JdbcTemplate template;

    public JdbcPostRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Post save(Post post) {
        String sql = "insert into post (title, content, member_id, create_time, view_count) values (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setLong(3, post.getMember().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(post.getCreateTime()));
            preparedStatement.setInt(5, post.getViewCount());

            return preparedStatement;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        post.setId(key);

        return post;
    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public void update(Long id, PostAddForm postAddForm) {
        String sql = "update post set title=?, content=? where id=?";

        template.update(sql,
            postAddForm.getTitle(),
            postAddForm.getTitle(),
            id);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from post where id=?";
        template.update(sql, id);
    }

    @Override
    public List<PostPreDto> search(SearchForm searchForm) {
        String searchCode = searchForm.getSearchCode();
        String searchString = searchForm.getSearchString();
        String sortCode = searchForm.getSortCode();

        String sql = "select post.id, title, content, create_time, view_count, member.id as name from post join member on post.member_id = member.id ";

        if (searchCode.equals("CONTENT")) {
            sql += "where content like ";
        } else if (searchCode.equals("NAME")) {
            sql += "where name like ";
        } else { // searchCode.equals("TITLE")
            sql += "where title like ";
        }

        sql += "'%" + searchString + "%' ";

        if (sortCode.equals("NEW")) {
            sql += "order by create_time desc";
        } else if (sortCode.equals("VIEW")) {
            sql += "order by view_count desc";
        } else { // sortCode.equals("OLD")
            sql += "order by create_time asc";
        }

        List<Object> param = new ArrayList<>();
        return template.query(sql, postPreDtoRowMapper(), param.toArray());
    }

    private RowMapper<PostPreDto> postPreDtoRowMapper() {
        return (rs, rowNum) -> {
            PostPreDto postPreDto = new PostPreDto();
            postPreDto.setId(rs.getLong("post.id"));
            postPreDto.setTitle(rs.getString("title"));
            postPreDto.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            postPreDto.setViewCount(rs.getInt("view_count"));
            postPreDto.setName(rs.getString("name"));

            return postPreDto;
        };
    }

    @Override
    public PostViewDto findViewDtoById(Long id) {
        String sql = "select post.id, title, content, create_time, view_count, member.id as name from post join member on post.member_id = member.id where post.id = ?";
        return template.queryForObject(sql, postViewDtoRowMapper(), id);
    }

    private RowMapper<PostViewDto> postViewDtoRowMapper() {
        return (rs, rowNum) -> {
            PostViewDto postViewDto = new PostViewDto();
            postViewDto.setId(rs.getLong("post.id"));
            postViewDto.setTitle(rs.getString("title"));
            postViewDto.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            postViewDto.setViewCount(rs.getInt("view_count"));
            postViewDto.setName(rs.getString("name"));
            postViewDto.setContent(rs.getString("content"));

            return postViewDto;
        };
    }

    @Override
    public void updateViewCount(Long id, int viewCount) {
        String sql = "update post set view_count=? where id=?";
        template.update(sql, viewCount,id);
    }

    @Override
    public Long findMemberIdByPostID(Long id) {
        String sql = "select member.id from post join member on post.member_id = member.id where post.id = ?";
        return template.queryForObject(sql, Long.class, id);
    }
}
