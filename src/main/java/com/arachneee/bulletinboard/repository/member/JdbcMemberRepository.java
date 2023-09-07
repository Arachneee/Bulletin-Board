package com.arachneee.bulletinboard.repository.member;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Member findById(Long id) {
        String sql = "select id, login_id, password, name from member where id = :id";
        Map<String, Object> param = Map.of("id", id);

        return template.queryForObject(sql, param, memberRowMapper());
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, password, name from member where login_id = :loginId";
        Map<String, Object> param = Map.of("loginId", loginId);

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
        Map<String, Object> param = Map.of("loginId", loginId);

        return template.queryForObject(sql, param, Long.class);
    }

    @Override
    public Long countName(String name) {
        String sql = "select count(name) from member where name=:name";
        Map<String, Object> param = Map.of("name", name);

        return template.queryForObject(sql, param, Long.class);
    }
}
