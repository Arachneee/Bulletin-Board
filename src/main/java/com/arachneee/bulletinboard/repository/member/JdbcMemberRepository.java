package com.arachneee.bulletinboard.repository.member;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate template;

    public JdbcMemberRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (login_id, password, name) values (?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, member.getLoginId());
            preparedStatement.setString(2, member.getPassword());
            preparedStatement.setString(3, member.getName());
            return preparedStatement;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        member.setId(key);

        return member;
    }

    @Override
    public Member findById(Long id) {
        String sql = "select id, login_id, password, name from member where id = ?";

        return template.queryForObject(sql, memberRowMapper(), id);
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select id, login_id, password, name from member where name = ?";

        try {
            Member member = template.queryForObject(sql, memberRowMapper(), name);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, password, name from member where login_id = ?";

        try {
            Member member = template.queryForObject(sql, memberRowMapper(), loginId);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select id, login_id, password, name from member";
        List<Object> param = new ArrayList<>();
        return template.query(sql, memberRowMapper(), param.toArray());
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setPassword(rs.getString("password"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
