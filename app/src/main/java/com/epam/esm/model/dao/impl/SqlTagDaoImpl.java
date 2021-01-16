package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.QueryCustomizer;
import com.epam.esm.model.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static final String INSERT_INTO_REF_TABLE_QUERY =
            "INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
    private static final String FIND_BY_GIFT_CERTIFICATE_ID_QUERY =
            "SELECT id, name FROM tag WHERE id IN " +
                    "(SELECT tag_id FROM gift_certificate_has_tag WHERE gift_certificate_id = ?)";
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;
    private GiftCertificateDao giftCertificateDao;

    public SqlTagDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setGiftCertificateDao(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public long add(Tag tag) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        long generatedTagId = keyHolder.getKey().longValue();
        if (tag.getGiftCertificates() != null) {
            for (GiftCertificate giftCertificate : tag.getGiftCertificates()) {
                try {
                    if (giftCertificate.getId() == null) {
                        long generatedGiftCertificateId = giftCertificateDao.add(giftCertificate);
                        giftCertificate.setId(generatedGiftCertificateId);
                    } else {
                        if (giftCertificateDao.findById(giftCertificate.getId()).isEmpty()) {
                            long generatedGiftCertificateId = giftCertificateDao.add(giftCertificate);
                            giftCertificate.setId(generatedGiftCertificateId);
                        }
                    }
                    jdbcTemplate.update(INSERT_INTO_REF_TABLE_QUERY, giftCertificate.getId(), generatedTagId);
                } catch (DaoException e) {
                    throw new DaoException("Error while adding gift certificate", e);
                }
            }
        }
        return generatedTagId;
    }

    @Override
    public Optional<Tag> findById(long id) throws DaoException {
        try {
            List<GiftCertificate> giftCertificates = giftCertificateDao.findByTagId(id);
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new TagRowMapper(), id);
            tag.setGiftCertificates(giftCertificates);
            return Optional.of(tag);
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
        List<Tag> tags = jdbcTemplate.query(queryCustomizer.prepareQuery(FIND_ALL_QUERY), new TagRowMapper());
        for (Tag tag : tags) {
            List<GiftCertificate> giftCertificates = giftCertificateDao.findByTagId(tag.getId());
            tag.setGiftCertificates(giftCertificates);
        }
        return tags;
    }

    @Override
    public void update(long id, Tag tag) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) throws DaoException {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Tag> findByGiftCertificateId(long id) throws DaoException {
        return jdbcTemplate.query(FIND_BY_GIFT_CERTIFICATE_ID_QUERY, new TagRowMapper(), id);
    }

    private class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Tag.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build();
        }
    }
}
