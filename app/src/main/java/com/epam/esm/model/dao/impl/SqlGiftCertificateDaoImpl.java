package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.util.QueryCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SqlGiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String INSERT_QUERY =
            "INSERT INTO gift_certificate " +
                    "(name, description, price, durationInDays, create_date, last_update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, description, price, durationInDays, create_date, last_update_date " +
                    "FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT id, name, description, price, durationInDays, create_date, last_update_date FROM gift_certificate";
    private static final String INSERT_INTO_REF_TABLE_QUERY =
            "INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String UPDATE_GIFT_CERTIFICATE_QUERY =
            "UPDATE gift_certificate SET id = ?, name = ?, description = ?, " +
                    "price = ?, durationInDays = ?, create_date = ?, last_update_date = ? WHERE id = ?";
    private static final String FIND_BY_TAG_ID_QUERY =
            "SELECT id, name, description, price, durationInDays, create_date, last_update_date " +
                    "FROM gift_certificate WHERE id IN " +
                    "(SELECT gift_certificate_id FROM gift_certificate_has_tag WHERE tag_id = ?)";
    private static final String FIND_BY_TAG_NAME_QUERY =
            "SELECT gc.id, gc.name, description, price, durationInDays, create_date, last_update_date " +
                    "FROM gift_certificate gc " +
                    "INNER JOIN gift_certificate_has_tag gcht on gc.id = gcht.gift_certificate_id " +
                    "INNER JOIN tag t on gcht.tag_id = t.id " +
                    "WHERE t.name = ?";
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String WHERE = "WHERE";
    private static final String TEMP_VALUE = "TEMP_VALUE";
    private static final String AND_WITH_BRACKET = "AND (";
    private static final String ORDER_BY = "ORDER BY";
    private static final String ORDER_BY_WITH_BRACKET = ") ORDER BY";
    private static final String RIGHT_BRACKET = ")";
    private static final String LEFT_BRACKET = "(";
    private final JdbcTemplate jdbcTemplate;
    private TagDao tagDao;

    public SqlGiftCertificateDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate add(GiftCertificate giftCertificate) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration() != null
                    ? giftCertificate.getDuration()
                    : 0);
            ps.setTimestamp(5, giftCertificate.getCreateDate() != null
                    ? Timestamp.valueOf(giftCertificate.getCreateDate())
                    : Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, giftCertificate.getLastUpdateDate() != null
                    ? Timestamp.valueOf(giftCertificate.getLastUpdateDate())
                    : Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new DaoException("Generated tag id is null");
        }
        long generatedGiftCertificateId = keyHolder.getKey().longValue();
        giftCertificate.setId(generatedGiftCertificateId);
        addNestedTags(giftCertificate);
        return findById(generatedGiftCertificateId)
                .orElseThrow(() -> new DaoException("Error while adding gift certificate"));
    }

    @Override
    public Optional<GiftCertificate> findById(long id) throws DaoException {
        try {
            List<Tag> tags = tagDao.findByGiftCertificateId(id);
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new GiftCertificateRowMapper(), id);
            if (giftCertificate != null) {
                giftCertificate.setTags(tags);
            }
            return Optional.ofNullable(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws DaoException {
        return findAll(new QueryCustomizer());
    }

    @Override
    public List<GiftCertificate> findAll(QueryCustomizer queryCustomizer) throws DaoException {
        try {
            List<GiftCertificate> giftCertificates = jdbcTemplate.query(queryCustomizer.prepareQuery(FIND_ALL_QUERY),
                    new GiftCertificateRowMapper());
            for (GiftCertificate giftCertificate : giftCertificates) {
                List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId());
                giftCertificate.setTags(tags);
            }
            return giftCertificates;
        } catch (BadSqlGrammarException e) {
            throw new DaoException("Invalid query", e);
        }
    }

    @Override
    public GiftCertificate update(long id, GiftCertificate patch) throws DaoException {
        Optional<GiftCertificate> optionalGiftCertificate = findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            Optional.ofNullable(patch.getName()).ifPresent(giftCertificate::setName);
            Optional.ofNullable(patch.getDescription()).ifPresent(giftCertificate::setDescription);
            Optional.ofNullable(patch.getPrice()).ifPresent(giftCertificate::setPrice);
            Optional.ofNullable(patch.getDuration()).ifPresent(giftCertificate::setDuration);
            Optional.ofNullable(patch.getCreateDate()).ifPresent(giftCertificate::setCreateDate);
            Optional.ofNullable(patch.getLastUpdateDate()).ifPresentOrElse(giftCertificate::setLastUpdateDate,
                    () -> giftCertificate.setLastUpdateDate(LocalDateTime.now()));
            if (patch.getTags() != null) {
                patch.setId(id);
                addNestedTags(patch);
            }
            jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE_QUERY,
                    id,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getDuration(),
                    giftCertificate.getCreateDate() != null
                            ? Timestamp.valueOf(giftCertificate.getCreateDate())
                            : Timestamp.valueOf(LocalDateTime.now()),
                    giftCertificate.getLastUpdateDate() != null
                            ? Timestamp.valueOf(giftCertificate.getLastUpdateDate())
                            : Timestamp.valueOf(LocalDateTime.now()),
                    id);
        } else {
            throw new DaoException("Gift certificate with such id is not exist");
        }
        return findById(id)
                .orElseThrow(() -> new DaoException("Error while updating gift certificate"));
    }

    @Override
    public GiftCertificate delete(long id) throws DaoException {
        GiftCertificate giftCertificate = findById(id).orElseThrow(
                () -> new DaoException("Error while deleting gift certificate. Object with such id is not exist"));
        jdbcTemplate.update(DELETE_QUERY, id);
        return giftCertificate;
    }

    public List<GiftCertificate> findByTagId(long id) {
        return jdbcTemplate.query(FIND_BY_TAG_ID_QUERY, new GiftCertificateRowMapper(), id);
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) throws DaoException {
        return findByTagName(tagName, new QueryCustomizer());
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName, QueryCustomizer queryCustomizer) throws DaoException {
        try {
            List<GiftCertificate> giftCertificates = jdbcTemplate.query(
                    constructCorrectSql(FIND_BY_TAG_NAME_QUERY, queryCustomizer), new GiftCertificateRowMapper(), tagName);
            for (GiftCertificate giftCertificate : giftCertificates) {
                List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId());
                giftCertificate.setTags(tags);
            }
            return giftCertificates;
        } catch (BadSqlGrammarException e) {
            throw new DaoException("Invalid query", e);
        }
    }

    private void addNestedTags(GiftCertificate giftCertificate) throws DaoException {
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                try {
                    // Adding tag into database if id is passed and such tag is not exist
                    if (tag.getId() == null) {
                        long generatedTagId = tagDao.add(tag).getId();
                        tag.setId(generatedTagId);
                    } else {
                        if (tagDao.findById(tag.getId()).isEmpty()) {
                            long generatedTagId = tagDao.add(tag).getId();
                            tag.setId(generatedTagId);
                        }
                    }
                    jdbcTemplate.update(INSERT_INTO_REF_TABLE_QUERY, giftCertificate.getId(), tag.getId());
                } catch (DaoException e) {
                    throw new DaoException("Error while adding tag", e);
                }
            }
        }
    }

    private String constructCorrectSql(String sql, QueryCustomizer queryCustomizer) {
        sql = sql.replaceAll(WHERE, TEMP_VALUE);
        sql = queryCustomizer.prepareQuery(sql);
        sql = sql.replaceFirst(WHERE, AND_WITH_BRACKET);
        if (sql.contains(LEFT_BRACKET)) {
            if (sql.contains(ORDER_BY)) {
                sql = sql.replaceFirst(ORDER_BY, ORDER_BY_WITH_BRACKET);
            } else {
                sql += RIGHT_BRACKET;
            }
        }
        sql = sql.replaceAll(TEMP_VALUE, WHERE);
        return sql;
    }

    private static class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return GiftCertificate.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .description(rs.getString(3))
                    .price(rs.getBigDecimal(4))
                    .duration(rs.getInt(5))
                    .createDate(rs.getTimestamp(6) != null
                            ? rs.getTimestamp(6).toLocalDateTime()
                            : null)
                    .lastUpdateDate(rs.getTimestamp(7) != null
                            ? rs.getTimestamp(7).toLocalDateTime()
                            : null)
                    .build();
        }
    }
}
