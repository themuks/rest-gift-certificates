package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class SqlGiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String INSERT_QUERY =
            "INSERT INTO gift_certificate " +
                    "(name, description, price, duration, create_date, last_update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date " +
                    "FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String INSERT_INTO_REF_TABLE_QUERY =
            "INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String UPDATE_GIFT_CERTIFICATE_QUERY =
            "UPDATE gift_certificate SET id = ?, name = ?, description = ?, " +
                    "price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";
    private static final String DELETE_REF_DATA_QUERY =
            "DELETE FROM tag WHERE id IN " +
                    "(SELECT t.id FROM tag t " +
                    "INNER JOIN gift_certificate_has_tag gcht ON t.id = gcht.tag_id " +
                    "INNER JOIN gift_certificate gc on gcht.gift_certificate_id = gc.id WHERE gc.id = ?)";
    private static final String DELETE_GIFT_CERTIFICATES_QUERY =
            "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_TAG_ID_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date " +
                    "FROM gift_certificate WHERE id IN " +
                    "(SELECT gift_certificate_id FROM gift_certificate_has_tag WHERE tag_id = ?)";
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
    public long add(GiftCertificate giftCertificate) throws DaoException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
            ps.setTimestamp(6, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);
        long generatedGiftCertificateId = keyHolder.getKey().longValue();
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                try {
                    if (tagDao.findById(tag.getId()).isEmpty()) {
                        long generatedTagId = tagDao.add(tag);
                        jdbcTemplate.update(INSERT_INTO_REF_TABLE_QUERY, generatedGiftCertificateId, generatedTagId);
                    }
                } catch (DaoException e) {
                    throw new DaoException("Error while adding tag", e);
                }
            }
        }
        return generatedGiftCertificateId;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) throws DaoException {
        try {
            List<Tag> tags = tagDao.findByGiftCertificateId(id, false);
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new GiftCertificateRowMapper(), id);
            giftCertificate.setTags(tags);
            return Optional.of(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws DaoException {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_ALL_QUERY,  new GiftCertificateRowMapper());
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId(), false);
            giftCertificate.setTags(tags);
        }
        return giftCertificates;
    }

    @Override
    public void update(long id, GiftCertificate giftCertificate) throws DaoException {
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE_QUERY,
                id,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                Timestamp.valueOf(giftCertificate.getCreateDate()),
                Timestamp.valueOf(giftCertificate.getLastUpdateDate()),
                id);
    }

    @Override
    public void delete(long id) throws DaoException {
        jdbcTemplate.update(DELETE_REF_DATA_QUERY, id);
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATES_QUERY, id);
    }

    @Override
    public List<GiftCertificate> findByTagId(long id, boolean isNested) throws DaoException {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_BY_TAG_ID_QUERY, new GiftCertificateRowMapper(), id);
        if (isNested) {
            return giftCertificates;
        }
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId(), true);
            giftCertificate.setTags(tags);
        }
        return giftCertificates;
    }

    private class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return GiftCertificate.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .description(rs.getString(3))
                    .price(rs.getBigDecimal(4))
                    .duration(rs.getInt(5))
                    .createDate(rs.getTimestamp(6).toLocalDateTime())
                    .lastUpdateDate(rs.getTimestamp(7).toLocalDateTime())
                    .build();
        }
    }
}
