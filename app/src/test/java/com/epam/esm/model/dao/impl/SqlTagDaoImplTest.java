package com.epam.esm.model.dao.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = SpringTestConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
class SqlTagDaoImplTest {
    @Autowired
    private DataSource dataSource;
    private SqlTagDaoImpl tagDao;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        tagDao = new SqlTagDaoImpl(dataSource);
        tagDao.setGiftCertificateDao(giftCertificateDao);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("alter table gift_certificate alter column id restart with 1");
        jdbcTemplate.execute("alter table tag alter column id restart with 1");
    }

    @Test
    void add_ValidData_OneElementInDB() {
        Tag tag = Tag.builder()
                .name("name")
                .build();
        try {
            tagDao.add(tag);
            int expected = 1;
            int actual = tagDao.findAll().size();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findById_InvalidId_EmptyOptional() {
        try {
            Optional<Tag> actual = tagDao.findById(-1);
            Optional<Object> expected = Optional.empty();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findById_OneElementAndIdIsOne_OptionalWithObject() {
        Tag tag = Tag.builder()
                .name("name")
                .giftCertificates(new ArrayList<>())
                .build();
        try {
            long generatedId = tagDao.add(tag);
            tag.setId(generatedId);
            Optional<Tag> actual = tagDao.findById(generatedId);
            Optional<Tag> expected = Optional.of(tag);
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findAll_TwoElementsInDb_TwoElementsList() {
        Tag tag = Tag.builder()
                .name("name")
                .build();
        try {
            tagDao.add(tag);
            tagDao.add(tag);
            List<Tag> tags = tagDao.findAll();
            int expected = 2;
            int actual = tags.size();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void delete_ValidId_RowDeletion() {
        Tag tag = Tag.builder()
                .name("name")
                .build();
        try {
            long generatedId = tagDao.add(tag);
            int expected = tagDao.findAll().size();
            tag.setId(generatedId);
            tagDao.delete(generatedId);
            int actual = tagDao.findAll().size();
            assertEquals(expected - 1, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findByGiftCertificateId_ValidId_Tag() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate1 = GiftCertificate.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        GiftCertificate giftCertificate2 = GiftCertificate.builder()
                .id(2L)
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        Tag tag = Tag.builder()
                .id(1L)
                .name("name")
                .giftCertificates(List.of(giftCertificate1, giftCertificate2))
                .build();
        try {
            long generatedId = tagDao.add(tag);
            tag.setId(generatedId);
            List<Tag> tags = tagDao.findByGiftCertificateId(2);
            tag.setGiftCertificates(null);
            boolean actual = tags.contains(tag);
            assertTrue(actual);
        } catch (DaoException e) {
            fail(e);
        }
    }
}
