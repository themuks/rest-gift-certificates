package com.epam.esm.model.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.dao.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * The interface User dao extended from {@code Dao<T>} interface.
 * Provides additional methods to work with {@code User} objects from data source.
 *
 * @see User
 * @see Dao
 */
public interface UserDao extends Dao<User> {
    /**
     * Find orders of user.
     *
     * @param id     id to search user by
     * @param offset count of records to skip
     * @param limit  maximum count of records to return
     * @return list of found {@link Order} objects
     * @throws DaoException if error occurs while finding {@link User} objects
     */
    List<Order> findOrdersOfUser(long id, int offset, int limit) throws DaoException;

    /**
     * Find user by email.
     *
     * @param email email to search user by
     * @return found optional of {@link User} object
     * @throws DaoException if error occurs while finding {@link User} object
     */
    Optional<User> findByEmail(String email) throws DaoException;
}
