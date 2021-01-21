package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.util.QueryCustomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagDao tagDao;
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagDao);
    }

    @Test
    void add_ValidTagGiven_ShouldReturnGeneratedId() throws DaoException {
        Tag tag = Tag.builder()
                .name("name")
                .build();
        when(tagDao.add(any(Tag.class))).thenReturn(1L);
        try {
            long actual = tagService.add(tag);
            long expected = 1L;
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void add_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.add(null));
    }

    @Test
    void add_InvalidTagGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.add(new Tag()));
    }

    @Test
    void add_DaoExceptionThrown_ShouldThrowServiceException() {
        Tag tag = Tag.builder()
                .name("name")
                .build();
        try {
            when(tagDao.add(any(Tag.class))).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> tagService.add(tag));
    }

    @Test
    void findById_InvalidIdGiven_ShouldThrowInvalidArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(-1));
    }

    @Test
    void findById_ValidIdGivenObjectExists_ShouldReturnOptionalWithObject() throws DaoException {
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(new Tag()));
        try {
            Optional<Tag> actual = tagService.findById(1);
            Optional<Tag> expected = Optional.of(new Tag());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findById_ValidIdGivenObjectNotExist_ShouldReturnEmptyOptional() throws DaoException {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        try {
            Optional<Tag> actual = tagService.findById(1);
            Optional<Tag> expected = Optional.empty();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findById_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(tagDao.findById(anyLong())).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> tagService.findById(1));
    }

    @Test
    void findAll_Nothing_ShouldReturnList() throws DaoException {
        when(tagDao.findAll()).thenReturn(List.of(new Tag()));
        try {
            List<Tag> actual = tagService.findAll();
            List<Tag> expected = List.of(new Tag());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_DaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(tagDao.findAll()).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> tagService.findAll());
    }

    @Test
    void findAll_ValidQueryCustomizerGiven_ShouldReturnList() throws DaoException {
        when(tagDao.findAll(any(QueryCustomizer.class))).thenReturn(List.of(new Tag()));
        try {
            List<Tag> actual = tagService.findAll(new QueryCustomizer());
            List<Tag> expected = List.of(new Tag());
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_NullGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findAll(null));
    }

    @Test
    void findAll_ValidQueryCustomizerGivenDaoExceptionThrown_ShouldThrowServiceException() {
        try {
            when(tagDao.findAll(any(QueryCustomizer.class))).thenThrow(new DaoException());
        } catch (DaoException ignored) {
        }
        assertThrows(ServiceException.class, () -> tagService.findAll(new QueryCustomizer()));
    }

    @Test
    void delete_ValidIdGiven_Success() throws DaoException {
        long id = 1;
        try {
            tagService.delete(id);
        } catch (ServiceException e) {
            fail(e);
        }
        verify(tagDao, times(1)).delete(id);
    }

    @Test
    void delete_InvalidIdGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.delete(-1));
    }

    @Test
    void delete_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        doThrow(new DaoException()).when(tagDao).delete(anyLong());
        assertThrows(ServiceException.class, () -> tagService.delete(1));
    }
}
