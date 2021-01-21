package com.epam.esm.model.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.QueryCustomizer;

import java.util.List;
import java.util.Optional;

/**
 * The interface Gift certificate service provides methods for service which performs operations with
 * {@link GiftCertificate} objects.
 * <p>
 *     Some of the methods may need to provide {@link QueryCustomizer} object for more specific purposes.
 * </p>
 */
public interface GiftCertificateService {
    /**
     * Adds {@link GiftCertificate} object to repository.
     *
     * @param giftCertificate gift certificate to be added
     * @return generated id of added gift certificate
     * @throws ServiceException if error occurs while adding {@link GiftCertificate} to repository
     * @throws IllegalArgumentException if provided object is null or doesn't pass validation
     * (see {@link com.epam.esm.model.validator.ProxyGiftCertificateValidator})
     */
    long add(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Finds {@link GiftCertificate} by id and returns optional.
     *
     * @param id id to search by
     * @return optional object. Empty optional returned if object doesn't found
     * @throws ServiceException if error occurs while finding {@link GiftCertificate} by id
     * @throws IllegalArgumentException if provided id doesn't pass validation
     * (see {@link com.epam.esm.model.validator.EntityValidator}
     */
    Optional<GiftCertificate> findById(long id) throws ServiceException;

    /**
     * Finds all {@link GiftCertificate} and returns list.
     *
     * @return list of found {@link GiftCertificate} objects
     * @throws ServiceException if error occurs while finding all {@link GiftCertificate} objects
     */
    List<GiftCertificate> findAll() throws ServiceException;

    /**
     * Finds all {@link GiftCertificate} and returns list. Order and content of list depends on {@link QueryCustomizer}
     * provided to the method.
     *
     * @param queryCustomizer query customizer which affects result list
     * @return list of found {@link GiftCertificate} objects
     * @throws ServiceException if error occurs while finding {@link GiftCertificate} objects
     * @throws IllegalArgumentException if query customizer is null
     */
    List<GiftCertificate> findAll(QueryCustomizer queryCustomizer) throws ServiceException;

    /**
     * Updates {@link GiftCertificate} object with provided id in the repository with {@link GiftCertificate}
     * object values. If value of provided {@link GiftCertificate} is null, field will not be updated.
     *
     * @param id              id of object to be updated
     * @param giftCertificate gift certificate object with new field values
     * @throws ServiceException if error occurs while updating {@link GiftCertificate} to repository
     * @throws IllegalArgumentException if provided object is null or parameters doesn't pass validation
     * (see {@link com.epam.esm.model.validator.GiftCertificateValidator}
     * and {@link com.epam.esm.model.validator.EntityValidator})
     */
    void update(long id, GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Deletes {@link GiftCertificate} object with provided id from repository.
     *
     * @param id id of object to be deleted
     * @throws ServiceException if error occurs while deleting {@link GiftCertificate} from repository
     * @throws IllegalArgumentException if provided id doesn't pass validation
     * (see {@link com.epam.esm.model.validator.EntityValidator})
     */
    void delete(long id) throws ServiceException;

    /**
     * Finds {@link GiftCertificate} objects by tag name and returns list.
     *
     * @param tagName tag name to search by
     * @return list of found {@link GiftCertificate} objects
     * @throws ServiceException if error occurs while finding {@link GiftCertificate} objects by tag name
     * @throws IllegalArgumentException if provided tag name is null
     */
    List<GiftCertificate> findByTagName(String tagName) throws ServiceException;

    /**
     * Finds {@link GiftCertificate} objects by tag name and returns list. Order and content of list
     * depends on {@link QueryCustomizer} provided to the method.
     *
     * @param tagName         tag name to search by
     * @param queryCustomizer query customizer which affects result list
     * @return list of found {@link GiftCertificate} objects
     * @throws ServiceException if error occurs while finding {@link GiftCertificate} objects by tag name
     * @throws IllegalArgumentException if tag name or query customizer objects are null
     */
    List<GiftCertificate> findByTagName(String tagName, QueryCustomizer queryCustomizer) throws ServiceException;
}
