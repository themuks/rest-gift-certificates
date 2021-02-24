package com.epam.esm.model.service;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Finds {@link com.epam.esm.entity.User} by id and returns optional.
     *
     * @param id id to search by
     * @return optional object. Empty optional returned if object doesn't found
     * @throws ServiceException         if error occurs while finding {@link User} by id
     * @throws IllegalArgumentException if provided id doesn't pass validation
     *                                  (see {@link com.epam.esm.model.validator.EntityValidator}
     */
    Optional<User> findById(long id) throws ServiceException;

    /**
     * Finds all {@link User} and returns list. Order and content of list depends on lists of
     * field names, types of order and search expressions provided to the method.
     *
     * @param sortField        list of field names to sort
     * @param sortType         list of sort types
     * @param searchField      list of field names to search by
     * @param searchExpression list of search expressions
     * @param offset           count of records to skip
     * @param limit            maximum count of records to return
     * @return list of found {@link User} objects
     * @throws ServiceException         if error occurs while finding {@link User} objects
     * @throws IllegalArgumentException if query customizer is null
     */
    List<User> findAll(List<String> sortField,
                       List<String> sortType,
                       List<String> searchField,
                       List<String> searchExpression,
                       int offset,
                       int limit) throws ServiceException;

    /**
     * Find orders of user.
     *
     * @param id id to search user by
     * @param offset
     * @param limit
     * @return list of found {@link Order} objects
     * @throws ServiceException         if error occurs while finding {@link User} objects
     * @throws IllegalArgumentException if query customizer is null
     */
    List<Order> findOrdersOfUser(long id, int offset, int limit) throws ServiceException;
}
