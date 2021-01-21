package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.util.QueryCustomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    @Mock
    private TagService tagService;
    private TagController tagController;

    @BeforeEach
    void setUp() {
        this.tagController = new TagController(tagService);
    }

    @Test
    void findAll_NullParametersGiven_ShouldReturnList() throws ServiceException {
        when(tagService.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new Tag()));
        try {
            List<Tag> actual = tagController.findAll(null);
            List<Tag> expected = List.of(new Tag());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(tagService.findAll(any(QueryCustomizer.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> tagController.findAll(null));
    }

    @Test
    void findAll_ParametersGiven_ShouldReturnList() throws ServiceException {
        when(tagService.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new Tag()));
        try {
            List<Tag> actual = tagController
                    .findAll(new MultiValueMapAdapter<>(new HashMap<>()));
            List<Tag> expected = List.of(new Tag());
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findAll_ParametersGivenServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(tagService.findAll(any(QueryCustomizer.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class,
                () -> tagController.findAll(new MultiValueMapAdapter<>(new HashMap<>())));
    }

    @Test
    void findById_IdGivenObjectExists_ShouldReturnObject() throws ServiceException {
        when(tagService.findById(anyLong())).thenReturn(Optional.of(new Tag()));
        try {
            Tag actual = tagController.findById(1);
            Tag expected = new Tag();
            assertEquals(expected, actual);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
    }

    @Test
    void findById_IdGivenObjectNotExist_ShouldThrowEntityNotFoundException() throws ServiceException {
        when(tagService.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tagController.findById(1));
    }

    @Test
    void findById_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(tagService.findById(anyLong())).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> tagController.findById(1));
    }

    @Test
    void add_TagGiven_Success() throws ServiceException {
        when(tagService.add(any(Tag.class))).thenReturn(1L);
        try {
            tagController.add(new Tag());
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
        verify(tagService, times(1)).add(new Tag());
    }

    @Test
    void add_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() {
        try {
            when(tagService.add(any(Tag.class))).thenThrow(new ServiceException());
        } catch (ServiceException ignored) {
        }
        assertThrows(ServerInternalErrorException.class, () -> tagController.add(new Tag()));
    }

    @Test
    void delete_IdGiven_Success() throws ServiceException {
        long id = 1;
        try {
            tagController.delete(id);
        } catch (ServerInternalErrorException e) {
            fail(e);
        }
        verify(tagService, times(1)).delete(id);
    }

    @Test
    void delete_ServiceExceptionThrown_ShouldThrowServerInternalErrorException() throws ServiceException {
        doThrow(new ServiceException()).when(tagService).delete(anyLong());
        assertThrows(ServerInternalErrorException.class, () -> tagController.delete(1));
    }
}
