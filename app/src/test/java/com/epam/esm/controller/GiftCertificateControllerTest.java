package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.util.QueryCustomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {
    @Mock
    private GiftCertificateService giftCertificateService;
    private GiftCertificateController giftCertificateController;

    @BeforeEach
    void setUp() {
        this.giftCertificateController = new GiftCertificateController(giftCertificateService);
    }

    @Test
    void findAll_NullParametersGiven_ShouldReturnList() throws ServiceException {
        when(giftCertificateService.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateController.findAll(null, null, null, null, null);
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.findAll(any(QueryCustomizer.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> giftCertificateController.findAll(null, null, null, null, null));
    }

    @Test
    void findAll_ParametersGiven_ShouldReturnList() throws ServiceException {
        when(giftCertificateService.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateController
                    .findAll(null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_ParametersGivenServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.findAll(any(QueryCustomizer.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class,
                () -> giftCertificateController.findAll(null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void findById_IdGivenObjectExists_ShouldReturnObject() throws ServiceException {
        when(giftCertificateService.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        try {
            GiftCertificate actual = giftCertificateController.findById(1);
            GiftCertificate expected = new GiftCertificate();
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findById_IdGivenObjectNotExist_ShouldThrowEntityNotFoundException() throws ServiceException {
        when(giftCertificateService.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> giftCertificateController.findById(1));
    }

    @Test
    void findById_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.findById(anyLong())).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> giftCertificateController.findById(1));
    }

    @Test
    void add_GiftCertificateGiven_Success() throws ServiceException {
        when(giftCertificateService.add(any(GiftCertificate.class))).thenReturn(new GiftCertificate());
        try {
            ResponseEntity<GiftCertificate> responseEntity = giftCertificateController.add(new GiftCertificate());
            GiftCertificate actual = responseEntity.getBody();
            GiftCertificate expected = new GiftCertificate();
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void add_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.add(any(GiftCertificate.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> giftCertificateController.add(new GiftCertificate()));
    }

    @Test
    void update_IdAndGiftCertificateGiven_Success() throws ServiceException {
        long id = 1;
        GiftCertificate giftCertificate = new GiftCertificate();
        try {
            giftCertificateController.update(id, giftCertificate);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
        verify(giftCertificateService, times(1)).update(id, giftCertificate);
    }

    @Test
    void update_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            doThrow(new ServiceException())
                    .when(giftCertificateService)
                    .update(anyLong(), any(GiftCertificate.class));
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class,
                () -> giftCertificateController.update(1, new GiftCertificate()));
    }

    @Test
    void delete_IdGiven_Success() throws ServiceException {
        long id = 1;
        try {
            giftCertificateController.delete(id);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
        verify(giftCertificateService, times(1)).delete(id);
    }

    @Test
    void delete_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() throws ServiceException {
        doThrow(new ServiceException()).when(giftCertificateService).delete(anyLong());
        assertThrows(ServerInternalErrorException.class, () -> giftCertificateController.delete(1));
    }

    @Test
    void findAll_TagNameGiven_ShouldReturnList() throws ServiceException {
        when(giftCertificateService.findByTagName(anyString(), any(QueryCustomizer.class))).thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateController.findAll("name", null, null, null, null);
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_TagNameGivenServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.findByTagName(anyString(), any(QueryCustomizer.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class,
                () -> giftCertificateController.findAll("name", null, null, null, null));
    }

    @Test
    void findAll_TagNameAndSortAndSearchParametersGiven_ShouldReturnList() throws ServiceException {
        when(giftCertificateService.findByTagName(anyString(), any(QueryCustomizer.class)))
                .thenReturn(List.of(new GiftCertificate()));
        try {
            List<GiftCertificate> actual = giftCertificateController
                    .findAll("name", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            List<GiftCertificate> expected = List.of(new GiftCertificate());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_QueryCustomizerGivenServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(giftCertificateService.findByTagName(anyString(), any(QueryCustomizer.class)))
                    .thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> giftCertificateController
                .findAll("name", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }
}
