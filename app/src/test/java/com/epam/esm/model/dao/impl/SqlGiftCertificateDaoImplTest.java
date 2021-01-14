package com.epam.esm.model.dao.impl;

import com.epam.esm.config.SpringTestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = SpringTestConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
class SqlGiftCertificateDaoImplTest {
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("alter table gift_certificate alter column id restart with 1");
        jdbcTemplate.execute("alter table tag alter column id restart with 1");
    }

    @Test
    void add_ValidData_OneElementInDB() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        try {
            giftCertificateDao.add(giftCertificate);
            int expected = 1;
            int actual = giftCertificateDao.findAll().size();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findById_InvalidId_EmptyOptional() {
        try {
            Optional<GiftCertificate> actual = giftCertificateDao.findById(-1);
            Optional<Object> expected = Optional.empty();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findById_OneElementAndIdIsOne_OptionalWithObject() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .tags(new ArrayList<>())
                .build();
        try {
            long generatedId = giftCertificateDao.add(giftCertificate);
            giftCertificate.setId(generatedId);
            Optional<GiftCertificate> actual = giftCertificateDao.findById(generatedId);
            Optional<GiftCertificate> expected = Optional.of(giftCertificate);
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findAll_TwoElementsInDb_TwoElementsList() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        try {
            giftCertificateDao.add(giftCertificate);
            giftCertificateDao.add(giftCertificate);
            List<GiftCertificate> giftCertificates = giftCertificateDao.findAll();
            int expected = 2;
            int actual = giftCertificates.size();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void update_NewObjectParams_RowUpdate() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        try {
            long generatedId = giftCertificateDao.add(giftCertificate);
            giftCertificate.setId(generatedId);
            GiftCertificate expected = GiftCertificate.builder()
                    .id(1L)
                    .name("REDACTEDNAME")
                    .description("REDACTEDNAME")
                    .createDate(localDateTime)
                    .lastUpdateDate(localDateTime.plusDays(1))
                    .duration(1)
                    .price(new BigDecimal(123))
                    .tags(new ArrayList<>())
                    .build();
            giftCertificateDao.update(generatedId, expected);
            GiftCertificate actual = giftCertificateDao.findById(generatedId).get();
            assertEquals(expected, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void delete_ValidId_RowDeletion() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .build();
        try {
            long generatedId = giftCertificateDao.add(giftCertificate);
            int expected = giftCertificateDao.findAll().size();
            giftCertificate.setId(generatedId);
            giftCertificateDao.delete(generatedId);
            int actual = giftCertificateDao.findAll().size();
            assertEquals(expected - 1, actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findByTagId_ValidId_GiftCertificatesList() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        Tag tag1 = Tag.builder()
                .id(1L)
                .name("name")
                .build();
        Tag tag2 = Tag.builder()
                .id(2L)
                .name("name")
                .build();
        Tag tag3 = Tag.builder()
                .id(3L)
                .name("name")
                .build();
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .tags(Arrays.asList(tag1, tag2, tag3))
                .build();
        try {
            long generatedId = giftCertificateDao.add(giftCertificate);
            giftCertificate.setId(generatedId);
            List<GiftCertificate> giftCertificateList = giftCertificateDao.findByTagId(2);
            giftCertificate.setTags(null);
            boolean actual = giftCertificateList.contains(giftCertificate);
            assertTrue(actual);
        } catch (DaoException e) {
            fail(e);
        }
    }

    @Test
    void findByTagName_ValidTagName_GiftCertificatesList() {
        LocalDateTime localDateTime = LocalDateTime.of(12, 1, 1, 1, 1);
        Tag tag1 = Tag.builder()
                .id(1L)
                .name("supername")
                .build();
        Tag tag2 = Tag.builder()
                .id(2L)
                .name("name")
                .build();
        Tag tag3 = Tag.builder()
                .id(3L)
                .name("meganame")
                .build();
        GiftCertificate giftCertificate1 = GiftCertificate.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .tags(Arrays.asList(tag1, tag2, tag3))
                .build();
        GiftCertificate giftCertificate2 = GiftCertificate.builder()
                .id(2L)
                .name("name")
                .description("description")
                .createDate(localDateTime)
                .lastUpdateDate(localDateTime.plusDays(1))
                .duration(1)
                .price(new BigDecimal(123))
                .tags(Arrays.asList(tag1))
                .build();
        try {
            long generatedId1 = giftCertificateDao.add(giftCertificate1);
            long generatedId2 = giftCertificateDao.add(giftCertificate2);
            giftCertificate1.setId(generatedId1);
            giftCertificate2.setId(generatedId2);
            List<GiftCertificate> giftCertificateList = giftCertificateDao.findByTagName("name");
            boolean actual = giftCertificateList.contains(giftCertificate1);
            assertTrue(actual);
        } catch (DaoException e) {
            fail(e);
        }
    }
}
