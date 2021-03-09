package com.epam.esm.model.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;

/**
 * The interface Tag dao extended from {@code Dao<T>} interface.
 * Provides additional methods to work with {@code Tag} objects from data source.
 *
 * @see Tag
 * @see Dao
 */
public interface TagDao extends Dao<Tag> {
    /**
     * Find most used tag around users with highest cost of all orders.
     *
     * @return found tag
     * @throws DaoException if error occurs while finding most used {@code Tag} object
     */
    Tag findMostUsedTag() throws DaoException;
}
