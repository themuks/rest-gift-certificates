package com.epam.esm.model.service;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag service provides methods for service which performs operations with
 * {@link Tag} objects.
 */
public interface TagService {
    /**
     * Adds {@link Tag} object to repository.
     *
     * @param tag tag to be added
     * @return added {@link Tag}
     * @throws ServiceException         if error occurs while adding {@link Tag} to repository
     * @throws IllegalArgumentException if provided object is null or doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.TagValidator})
     */
    Tag add(Tag tag) throws ServiceException;

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
     * Finds all {@link Tag} and returns list. Order and content of list depends on lists of field names,
     * types of order and search expressions provided to the method.
     *
     * @param sortField        list of field names to sort
     * @param sortType         list of sort types
     * @param searchField      list of field names to search by
     * @param searchExpression list of search expressions
     * @param offset           count of records to skip
     * @param limit            maximum count of records to return
     * @return list of found {@link Tag} objects
     * @throws ServiceException         if error occurs while finding {@link Tag} objects
     * @throws IllegalArgumentException if query customizer is null
     */
    List<Tag> findAll(List<String> sortField,
                      List<String> sortType,
                      List<String> searchField,
                      List<String> searchExpression,
                      int offset,
                      int limit) throws ServiceException;

    /**
     * Deletes {@link Tag} object with provided id from repository.
     *
     * @param id id of object to be deleted
     * @return deleted {@link Tag}
     * @throws ServiceException         if error occurs while deleting {@link Tag} from repository
     * @throws IllegalArgumentException if provided id doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.EntityValidator})
     */
    Tag delete(long id) throws ServiceException;

    Tag findMostUsedTag() throws ServiceException;
}
