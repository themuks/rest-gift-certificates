package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
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
    void add_ValidTagGiven_ShouldReturnEntityWithGeneratedId() throws DaoException {
        Tag expected = Tag.builder().id(1L).name("name").build();
        when(tagDao.add(any(Tag.class))).thenReturn(expected);
        try {
            Tag actual = tagService.add(expected);
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
    void add_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(tagDao.add(any(Tag.class))).thenThrow(new DaoException());
        Tag tag = Tag.builder().name("name").build();
        assertThrows(ServiceException.class, () -> tagService.add(tag));
    }

    @Test
    void findById_InvalidIdGiven_ShouldThrowInvalidArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(0));
    }

    @Test
    void findById_ValidIdGivenObjectExists_ShouldReturnOptionalWithObject() throws DaoException {
        Tag tag = Tag.builder()
                .id(1L)
                .name("name")
                .build();
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(tag));
        Optional<Tag> optionalTag = Optional.empty();
        try {
            optionalTag = tagService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        if (optionalTag.isPresent()) {
            Tag actual = optionalTag.get();
            assertEquals(tag, actual);
        } else {
            fail();
        }
    }

    @Test
    void findById_ValidIdGivenObjectNotExist_ShouldReturnEmptyOptional() throws DaoException {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        Optional<Tag> optionalTag = Optional.empty();
        try {
            optionalTag = tagService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        assertTrue(optionalTag.isEmpty());
    }

    @Test
    void findById_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(tagDao.findById(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> tagService.findById(1));
    }

    @Test
    void findAll_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(tagDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenThrow(new DaoException());
        assertThrows(ServiceException.class,
                () -> tagService.findAll(List.of(), List.of(), List.of(), List.of(), 1, 1));
    }

    @Test
    void findAll_ValidParametersGiven_ShouldReturnList() throws DaoException {
        List<Tag> expected = List.of(new Tag());
        when(tagDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<Tag> actual = tagService.findAll(new ArrayList<>(),
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
                () -> tagService.findAll(null, null, null, null, -1, -1));
    }

    @Test
    void delete_ValidIdGiven_Success() throws DaoException {
        when(tagDao.delete(anyLong())).thenReturn(new Tag());
        try {
            Tag actual = tagService.delete(1L);
            Tag expected = new Tag();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void delete_InvalidIdGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.delete(0));
    }

    @Test
    void delete_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(tagDao.delete(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> tagService.delete(1L));
    }

    @Test
    void findMostUsedTag_Nothing_MostUsedTagReturned() throws DaoException {
        Tag expected = Tag.builder().name("tag name").build();
        when(tagDao.findMostUsedTag()).thenReturn(expected);
        try {
            Tag actual = tagService.findMostUsedTag();
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findMostUsedTag_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(tagDao.findMostUsedTag()).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> tagService.findMostUsedTag());
    }
}
