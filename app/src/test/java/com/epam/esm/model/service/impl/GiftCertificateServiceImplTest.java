package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.util.QueryCustomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    @Mock
    private GiftCertificateDao giftCertificateDao;
    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao);
    }

    @Test
    void add_ValidGiftCertificateGiven_ShouldReturnGeneratedId() throws DaoException {
        LocalDateTime dateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(new BigDecimal(123))
                .duration(1)
                .createDate(dateTime)
                .lastUpdateDate(dateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        when(giftCertificateDao.add(any(GiftCertificate.class))).thenReturn(new GiftCertificate());
        try {
            GiftCertificate actual = giftCertificateService.add(giftCertificate);
            GiftCertificate expected = new GiftCertificate();
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
    void add_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.add(any(GiftCertificate.class))).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        LocalDateTime dateTime = LocalDateTime.of(1971, 1, 1, 1, 1);
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .price(new BigDecimal(123))
                .duration(1)
                .createDate(dateTime)
                .lastUpdateDate(dateTime.minusDays(1))
                .tags(new ArrayList<>())
                .build();
        assertThrows(ServiceException.class, () -> giftCertificateService.add(giftCertificate));
    }

    @Test
    void findById_InvalidIdGiven_ShouldThrowInvalidArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findById(-1));
    }

    @Test
    void findById_ValidIdGivenObjectExists_ShouldReturnOptionalWithObject() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        try {
            Optional<GiftCertificate> actual = giftCertificateService.findById(1);
            Optional<GiftCertificate> expected = Optional.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findById_ValidIdGivenObjectNotExist_ShouldReturnEmptyOptional() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        try {
            Optional<GiftCertificate> actual = giftCertificateService.findById(1);
            Optional<GiftCertificate> expected = Optional.empty();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findById_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.findById(anyLong())).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> giftCertificateService.findById(1));
    }

    @Test
    void findAll_Nothing_ShouldReturnList() throws DaoException {
        when(giftCertificateDao.findAll()).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateService.findAll();
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.findAll()).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> giftCertificateService.findAll());
    }

    @Test
    void findAll_ValidQueryCustomizerGiven_ShouldReturnList() throws DaoException {
        when(giftCertificateDao.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateService.findAll(new QueryCustomizer());
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findAll(null));
    }

    @Test
    void findAll_ValidQueryCustomizerGivenDaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.findAll(any(QueryCustomizer.class))).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> giftCertificateService.findAll(new QueryCustomizer()));
    }

    @Test
    void update_ValidIdAndGiftCertificateGiven_Success() throws DaoException {
        long id = 1;
        GiftCertificate giftCertificate = new GiftCertificate();
        try {
            giftCertificateService.update(id, giftCertificate);
        } catch (ServiceException e) {
            fail(e);
        }
        verify(giftCertificateDao, times(1)).update(id, giftCertificate);
    }

    @Test
    void update_InvalidIdAndNullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.update(-1, null));
    }

    @Test
    void update_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            doThrow(new DaoException())
                    .when(giftCertificateDao)
                    .update(anyLong(), any(GiftCertificate.class));
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> giftCertificateService.update(1, new GiftCertificate()));
    }

    @Test
    void delete_ValidIdGiven_Success() throws DaoException {
        long id = 1;
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException e) {
            fail(e);
        }
        verify(giftCertificateDao, times(1)).delete(id);
    }

    @Test
    void delete_InvalidIdGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.delete(-1));
    }

    @Test
    void delete_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        doThrow(new DaoException()).when(giftCertificateDao).delete(anyLong());
        assertThrows(ServiceException.class, () -> giftCertificateService.delete(1));
    }

    @Test
    void findByTagName_ValidTagNameGiven_ShouldReturnList() throws DaoException {
        when(giftCertificateDao.findByTagName(anyString())).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateService.findByTagName("name");
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findByTagName_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findByTagName(null));
    }

    @Test
    void findByTagName_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.findByTagName(anyString())).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> giftCertificateService.findByTagName("name"));
    }

    @Test
    void findByTagName_ValidTagNameAndQueryCustomizerGiven_ShouldReturnList() throws DaoException {
        when(giftCertificateDao.findByTagName(anyString(), any(QueryCustomizer.class))).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateService.findByTagName("name", new QueryCustomizer());
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findByTagName_NullsGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findByTagName(null, null));
    }

    @Test
    void findByTagName_ValidQueryCustomizerGivenDaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(giftCertificateDao.findByTagName(anyString(), any(QueryCustomizer.class))).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class,
                () -> giftCertificateService.findByTagName("name", new QueryCustomizer()));
    }
}
