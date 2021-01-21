package com.epam.esm.model.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.util.QueryCustomizer;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag service provides methods for service which performs operations with
 * {@link Tag} objects.
 * <p>
 * Some of the methods may need to provide {@link QueryCustomizer} object for more specific purposes.
 * </p>
 */
public interface TagService {
    /**
     * Adds {@link Tag} object to repository.
     *
     * @param tag tag to be added
     * @return generated id of added tag
     * @throws ServiceException         if error occurs while adding {@link Tag} to repository
     * @throws IllegalArgumentException if provided object is null or doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.ProxyGiftCertificateValidator})
     */
    long add(Tag tag) throws ServiceException;

    /**
     * Finds {@link Tag} by id and returns optional.
     *
     * @param id id to search by
     * @return optional object. Empty optional returned if object doesn't found
     * @throws ServiceException         if error occurs while finding {@link Tag} by id
     * @throws IllegalArgumentException if provided id doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.EntityValidator}
     */
    Optional<Tag> findById(long id) throws ServiceException;

    /**
     * Finds all {@link Tag} and returns list.
     *
     * @return list of found {@link Tag} objects
     * @throws ServiceException if error occurs while finding all {@link Tag} objects
     */
    List<Tag> findAll() throws ServiceException;

    /**
     * Finds all {@link Tag} and returns list. Order and content of list depends on {@link QueryCustomizer}
     * provided to the method.
     *
     * @param queryCustomizer query customizer which affects result list
     * @return list of found {@link Tag} objects
     * @throws ServiceException         if error occurs while finding {@link Tag} objects
     * @throws IllegalArgumentException if query customizer is null
     */
    List<Tag> findAll(QueryCustomizer queryCustomizer) throws ServiceException;

    /**
     * Deletes {@link Tag} object with provided id from repository.
     *
     * @param id id of object to be deleted
     * @throws ServiceException         if error occurs while deleting {@link Tag} from repository
     * @throws IllegalArgumentException if provided id doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.EntityValidator})
     */
    void delete(long id) throws ServiceException;
}
