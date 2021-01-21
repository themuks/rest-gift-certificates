package com.epam.esm.model.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * The interface Tag dao extended from {@code BaseDao<T>} interface.
 * Provides additional methods to work with {@code Tag} objects from data source.
 *
 * @see Tag
 * @see com.epam.esm.model.dao.BaseDao
 */
public interface TagDao extends BaseDao<Tag> {
    /**
     * Finds {@code Tag} objects by gift certificate id and returns list.
     *
     * @param id gift certificate id to search by
     * @return list of found {@code Tag} objects
     * @throws DaoException is thrown when error occurs while finding {@code Tag} objects by gift certificate id
     */
    List<Tag> findByGiftCertificateId(long id) throws DaoException;
}
