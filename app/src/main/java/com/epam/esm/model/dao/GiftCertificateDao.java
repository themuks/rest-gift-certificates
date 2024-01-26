package com.epam.esm.model.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;

import java.util.List;

/**
 * The interface Gift certificate dao extended from {@code Dao<T>} interface.
 * Provides additional methods to work with {@code GiftCertificate} objects from data source.
 *
 * @see GiftCertificate
 * @see Dao
 */
public interface GiftCertificateDao extends Dao<GiftCertificate> {
    /**
     * Finds {@code GiftCertificate} objects by tag name and returns list.
     *
     * @param tagName        tag name to search by
     * @param searchCriteria describes data to search by
     * @param sortCriteria   describes how to sort fetched data
     * @param offset         count of records to skip
     * @param limit          maximum count of records to return
     * @return list of found {@code GiftCertificate} objects
     * @throws DaoException if error occurs while finding {@code GiftCertificate} objects
     *                      by tag name
     */
    List<GiftCertificate> findByTagName(List<String> tagName,
                                        List<SearchUnit> searchCriteria,
                                        List<SortUnit> sortCriteria,
                                        int offset,
                                        int limit) throws DaoException;
}
