package com.epam.esm.model.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.QueryCustomizer;

import java.util.List;
import java.util.Optional;

public interface TagService {
    long add(Tag tag) throws ServiceException;

    Optional<Tag> findById(long id) throws ServiceException;

    List<Tag> findAll() throws ServiceException;

    List<Tag> findAll(QueryCustomizer queryCustomizer) throws ServiceException;

    void delete(long id) throws ServiceException;
}
