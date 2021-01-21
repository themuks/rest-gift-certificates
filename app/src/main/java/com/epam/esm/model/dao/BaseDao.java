package com.epam.esm.model.dao;

import com.epam.esm.util.QueryCustomizer;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    long add(T t) throws DaoException;

    Optional<T> findById(long id) throws DaoException;

    List<T> findAll() throws DaoException;

    List<T> findAll(QueryCustomizer queryCustomizer) throws DaoException;

    void update(long id, T t) throws DaoException;

    void delete(long id) throws DaoException;
}
