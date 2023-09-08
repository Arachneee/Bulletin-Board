package com.arachneee.bulletinboard.repository.member;

import com.arachneee.bulletinboard.domain.Member;
import com.arachneee.bulletinboard.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
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
    public void save(Member member) {
        String sql = "insert into member (login_id, password, name) values (:loginId, :password, :name)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("loginId", member.getLoginId())
                .addValue("password", member.getPassword())
                .addValue("name", member.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder);
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

            log.info("findMember data loginId = {}, password = {} by input = {}", member.getLoginId(), member.getPassword(), loginId);
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
        return (rs, rowNum) -> {
            return Member.create(rs.getString("login_id"), rs.getString("password"), rs.getString("name"));
        };
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
