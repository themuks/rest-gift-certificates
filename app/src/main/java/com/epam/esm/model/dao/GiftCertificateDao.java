package com.epam.esm.model.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.QueryCustomizer;

import java.util.List;

/**
 * The interface Gift certificate dao extended from {@code BaseDao<T>} interface.
 * Provides additional methods to work with {@code GiftCertificate} objects from data source.
 *
 * @see GiftCertificate
 * @see com.epam.esm.model.dao.BaseDao
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificate> {
    /**
     * Finds {@code GiftCertificate} objects by tag id and returns list.
     *
     * @param id tag id to search by
     * @return list of found {@code GiftCertificate} objects
     * @throws DaoException if error occurs while finding {@code GiftCertificate} objects
     *                      by tag id
     */
    List<GiftCertificate> findByTagId(long id) throws DaoException;

    /**
     * Finds {@code GiftCertificate} objects by tag name and returns list.
     *
     * @param tagName tag name to search by
     * @return list of found {@code GiftCertificate} objects
     * @throws DaoException if error occurs while finding {@code GiftCertificate} objects
     *                      by tag name
     */
    List<GiftCertificate> findByTagName(String tagName) throws DaoException;

    /**
     * Finds {@code GiftCertificate} objects by tag name and returns list. Order and content of list depends
     * on {@code QueryCustomizer} object provided to method (for more information see {@link QueryCustomizer}).
     *
     * @param tagName         tag name to search by
     * @param queryCustomizer query customizer which affects result list
     * @return list of found {@code GiftCertificate} objects
     * @throws DaoException if error occurs while finding {@code GiftCertificate} objects
     *                      by tag name
     */
    List<GiftCertificate> findByTagName(String tagName, QueryCustomizer queryCustomizer) throws DaoException;
}
