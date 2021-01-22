package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.util.QueryCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
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
    private static final String FIND_BY_TAG_ID_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date " +
                    "FROM gift_certificate WHERE id IN " +
                    "(SELECT gift_certificate_id FROM gift_certificate_has_tag WHERE tag_id = ?)";
    private static final String FIND_BY_TAG_NAME_QUERY =
            "SELECT gc.id, gc.name, description, price, duration, create_date, last_update_date " +
                    "FROM gift_certificate gc " +
                    "INNER JOIN gift_certificate_has_tag gcht on gc.id = gcht.gift_certificate_id " +
                    "INNER JOIN tag t on gcht.tag_id = t.id " +
                    "WHERE t.name = ?";
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
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
            ps.setTimestamp(5, giftCertificate.getCreateDate() != null
                    ? Timestamp.valueOf(giftCertificate.getCreateDate())
                    : null);
            ps.setTimestamp(6, giftCertificate.getLastUpdateDate() != null
                    ? Timestamp.valueOf(giftCertificate.getLastUpdateDate())
                    : null);
            return ps;
        }, keyHolder);
        long generatedGiftCertificateId = keyHolder.getKey().longValue();
        giftCertificate.setId(generatedGiftCertificateId);
        addNestedTags(giftCertificate);
        return generatedGiftCertificateId;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) throws DaoException {
        try {
            List<Tag> tags = tagDao.findByGiftCertificateId(id);
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new GiftCertificateRowMapper(), id);
            giftCertificate.setTags(tags);
            return Optional.of(giftCertificate);
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
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(queryCustomizer.prepareQuery(FIND_ALL_QUERY), new GiftCertificateRowMapper());
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId());
            giftCertificate.setTags(tags);
        }
        return giftCertificates;
    }

    @Override
    public void update(long id, GiftCertificate patch) throws DaoException {
        Optional<GiftCertificate> optionalGiftCertificate = findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            if (patch.getName() != null) {
                giftCertificate.setName(patch.getName());
            }
            if (patch.getDescription() != null) {
                giftCertificate.setDescription(patch.getDescription());
            }
            if (patch.getPrice() != null) {
                giftCertificate.setPrice(patch.getPrice());
            }
            if (patch.getDuration() != null) {
                giftCertificate.setDuration(patch.getDuration());
            }
            if (patch.getCreateDate() != null) {
                giftCertificate.setCreateDate(patch.getCreateDate());
            }
            if (patch.getLastUpdateDate() != null) {
                giftCertificate.setLastUpdateDate(patch.getLastUpdateDate());
            }
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
                            : null,
                    giftCertificate.getLastUpdateDate() != null
                            ? Timestamp.valueOf(giftCertificate.getLastUpdateDate())
                            : null,
                    id);
        } else {
            throw new DaoException("Gift certificate with such id is not exist");
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        jdbcTemplate.update(DELETE_QUERY, id);
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
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(queryCustomizer.prepareQuery(FIND_BY_TAG_NAME_QUERY), new GiftCertificateRowMapper(), tagName);
        for (GiftCertificate giftCertificate : giftCertificates) {
            List<Tag> tags = tagDao.findByGiftCertificateId(giftCertificate.getId());
            giftCertificate.setTags(tags);
        }
        return giftCertificates;
    }

    private void addNestedTags(GiftCertificate giftCertificate) throws DaoException {
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                try {
                    // Adding tag into database if id is passed and such tag is not exist
                    if (tag.getId() == null) {
                        long generatedTagId = tagDao.add(tag);
                        tag.setId(generatedTagId);
                    } else {
                        if (tagDao.findById(tag.getId()).isEmpty()) {
                            long generatedTagId = tagDao.add(tag);
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
                    .lastUpdateDate(rs.getTimestamp(6) != null
                            ? rs.getTimestamp(7).toLocalDateTime()
                            : null)
                    .build();
        }
    }
}
