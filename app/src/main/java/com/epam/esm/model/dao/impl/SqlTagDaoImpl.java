package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.util.QueryCustomizer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SqlTagDaoImpl implements TagDao {
    private static final String INSERT_QUERY = "INSERT INTO tag (name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
    private static final String FIND_BY_GIFT_CERTIFICATE_ID_QUERY =
            "SELECT id, name FROM tag WHERE id IN " +
                    "(SELECT tag_id FROM gift_certificate_has_tag WHERE gift_certificate_id = ?)";
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    public SqlTagDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Tag add(Tag tag) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Generated tag id is null");
        }
        long generatedTagId = keyHolder.getKey().longValue();
        return findById(generatedTagId)
                .orElseThrow(() -> new DaoException("Error while adding tag"));
    }

    @Override
    public Optional<Tag> findById(long id) throws DaoException {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new TagRowMapper(), id);
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() throws DaoException {
        return findAll(new QueryCustomizer());
    }

    @Override
    public List<Tag> findAll(QueryCustomizer queryCustomizer) throws DaoException {
        try {
            return jdbcTemplate.query(queryCustomizer.prepareQuery(FIND_ALL_QUERY), new TagRowMapper());
        } catch (BadSqlGrammarException e) {
            throw new DaoException("Invalid query", e);
        }
    }

    @Override
    public Tag update(long id, Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tag delete(long id) throws DaoException {
        Tag tag = findById(id).orElseThrow(
                () -> new DaoException("Error while deleting tag. Object with such id is not exist"));
        jdbcTemplate.update(DELETE_QUERY, id);
        return tag;
    }

    @Override
    public List<Tag> findByGiftCertificateId(long id) {
        return jdbcTemplate.query(FIND_BY_GIFT_CERTIFICATE_ID_QUERY, new TagRowMapper(), id);
    }

    private static class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Tag.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build();
        }
    }
}
