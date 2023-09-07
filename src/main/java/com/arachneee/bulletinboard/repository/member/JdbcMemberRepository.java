package com.arachneee.bulletinboard.repository.member;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate template;

    public JdbcMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (login_id, password, name) values (:loginId, :password, :name)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);

        // template.update(con -> {
        //     PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
        //     preparedStatement.setString(1, member.getLoginId());
        //     preparedStatement.setString(2, member.getPassword());
        //     preparedStatement.setString(3, member.getName());
        //     return preparedStatement;
        // }, keyHolder);

        Long key = keyHolder.getKey().longValue();
        member.setId(key);

        return member;
    }

    @Override
    public Member findById(Long id) {
        String sql = "select id, login_id, password, name from member where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("id", id);

        return template.queryForObject(sql, param, memberRowMapper());
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, password, name from member where login_id = :loginId";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("loginId", loginId);

        try {
            Member member = template.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select id, login_id, password, name from member";
        return template.query(sql, memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

    @Override
    public Long countLoginId(String loginId) {
        String sql = "select count(id) from member where login_id= :loginId";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("loginId", loginId);

        return template.queryForObject(sql, param, Long.class);
    }

    @Override
    public Long countName(String name) {
        String sql = "select count(name) from member where name=:name";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("name", name);

        return template.queryForObject(sql, param, Long.class);
    }
}
