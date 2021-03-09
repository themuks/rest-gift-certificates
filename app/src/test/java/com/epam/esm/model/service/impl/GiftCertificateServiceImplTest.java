package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private TagDao tagDao;
    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao);
    }

    @Test
    void add_ValidGiftCertificateGiven_ShouldReturnEntityWithGeneratedId() throws DaoException {
        GiftCertificate expected = GiftCertificate.builder().id(1L).name("name").build();
        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(expected);
        try {
            GiftCertificate actual = giftCertificateService.add(expected);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void add_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.add(null));
    }

    @Test
    void add_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.add(any(GiftCertificate.class))).thenThrow(new DaoException());
        LocalDateTime dateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(new BigDecimal(123))
                .durationInDays(1)
                .createDate(dateTime)
                .lastUpdateDate(dateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        assertThrows(ServiceException.class, () -> giftCertificateService.add(giftCertificate));
    }

    @Test
    void findById_InvalidIdGiven_ShouldThrowInvalidArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findById(0));
    }

    @Test
    void findById_ValidIdGivenObjectExists_ShouldReturnOptionalWithObject() throws DaoException {
        LocalDateTime dateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L)
                .name("name")
                .description("description")
                .price(new BigDecimal(123))
                .durationInDays(1)
                .createDate(dateTime)
                .lastUpdateDate(dateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> optionalGiftCertificate = Optional.empty();
        try {
            optionalGiftCertificate = giftCertificateService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate actual = optionalGiftCertificate.get();
            assertEquals(giftCertificate, actual);
        } else {
            fail();
        }
    }

    @Test
    void findById_ValidIdGivenObjectNotExist_ShouldReturnEmptyOptional() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        Optional<GiftCertificate> optionalGiftCertificate = Optional.empty();
        try {
            optionalGiftCertificate = giftCertificateService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        assertTrue(optionalGiftCertificate.isEmpty());
    }

    @Test
    void findById_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> giftCertificateService.findById(1));
    }

    @Test
    void findAll_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenThrow(new DaoException());
        assertThrows(ServiceException.class,
                () -> giftCertificateService.findAll(List.of(), List.of(), List.of(), List.of(), 1, 1));
    }

    @Test
    void findAll_ValidParametersGiven_ShouldReturnList() throws DaoException {
        List<GiftCertificate> expected = List.of(new GiftCertificate());
        when(giftCertificateDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<GiftCertificate> actual = giftCertificateService.findAll(new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    0,
                    501);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_InvalidParametersGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findAll(null, null, null, null, -1, -1));
    }

    @Test
    void update_ValidIdAndGiftCertificateGiven_Success() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        when(giftCertificateDao.update(any(GiftCertificate.class))).thenReturn(new GiftCertificate());
        try {
            GiftCertificate actual = giftCertificateService.update(1L, new GiftCertificate());
            GiftCertificate expected = new GiftCertificate();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void update_InvalidIdAndNullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.update(0, null));
    }

    @Test
    void update_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        when(giftCertificateDao.update(any(GiftCertificate.class))).thenThrow(new DaoException());
        LocalDateTime dateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(new BigDecimal(123))
                .durationInDays(1)
                .createDate(dateTime)
                .lastUpdateDate(dateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        assertThrows(ServiceException.class, () -> giftCertificateService.update(1L, giftCertificate));
    }

    @Test
    void delete_ValidIdGiven_Success() throws DaoException {
        when(giftCertificateDao.delete(anyLong())).thenReturn(new GiftCertificate());
        try {
            GiftCertificate actual = giftCertificateService.delete(1L);
            GiftCertificate expected = new GiftCertificate();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void delete_InvalidIdGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.delete(0));
    }

    @Test
    void delete_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.delete(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> giftCertificateService.delete(1L));
    }

    @Test
    void findByTagName_ValidTagNameGiven_ShouldReturnList() throws DaoException {
        List<GiftCertificate> expected = List.of(new GiftCertificate());
        when(giftCertificateDao.findByTagName(anyList(), anyList(), anyList(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<GiftCertificate> actual =
                    giftCertificateService.findByTagName(List.of("tag name"), null, null, null, null, 0, 501);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findByTagName_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findByTagName(null, null, null, null, null, 1, 1));
    }

    @Test
    void findByTagName_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.findByTagName(anyList(), anyList(), anyList(), anyInt(), anyInt()))
                .thenThrow(new DaoException());
        assertThrows(ServiceException.class,
                () -> giftCertificateService.findByTagName(List.of("tag name"), null, null, null, null, 1, 1));
    }

    @Test
    void findByTagName_ValidParametersGiven_ShouldReturnList() throws DaoException {
        List<GiftCertificate> expected = List.of(new GiftCertificate());
        when(giftCertificateDao.findByTagName(anyList(), anyList(), anyList(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<GiftCertificate> actual = giftCertificateService.findByTagName(List.of("tag name"),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    0,
                    501);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findByTagName_InvalidParametersGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findByTagName(List.of(""), null, null, null, null, -1, -1));
    }
}
