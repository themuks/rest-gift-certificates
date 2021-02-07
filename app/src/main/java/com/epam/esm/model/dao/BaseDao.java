package com.epam.esm.model.dao;

import com.epam.esm.util.QueryCustomizer;

import java.util.List;
import java.util.Optional;

/**
 * The interface Base dao provides methods for basic CRUD operations.
 *
 * @param <T> the type parameter
 */
public interface BaseDao<T> {
    /**
     * Adds provided object to the data source.
     *
     * @param t generic type object
     * @return added object
     * @throws DaoException if error occurs while adding object to the data source
     */
    T add(T t) throws DaoException;

    /**
     * Finds object in the data source by provided id.
     *
     * @param id by which the search will be performed
     * @return generic type optional. Optional will be empty if object wasn't found
     * @throws DaoException if error occurs while finding object by id in the data source
     */
    Optional<T> findById(long id) throws DaoException;

    /**
     * Finds all generic type objects in the repository.
     *
     * @return list of all objects from data source
     * @throws DaoException the dao exception
     */
    List<T> findAll() throws DaoException;

    /**
     * Finds all generic type objects in the repository. Result depends on {@code QueryCustomizer} object settings
     * provided to the method. It controls which object has to be included in list by changing sql query
     * (for more details check {@link QueryCustomizer} class).
     *
     * @param queryCustomizer the query customizer
     * @return list of objects from data source, order and content of which controlled by {@code QueryCustomizer}
     * @throws DaoException if error occurs while finding objects in the data source
     */
    List<T> findAll(QueryCustomizer queryCustomizer) throws DaoException;

    /**
     * Updates object in the data source with provided id to new values from provided generic type object.
     * If provided object field value is null this field will not be updated.
     *
     * @param id id of entity in the data source
     * @param t  object with values to be updated
     * @return added object
     * @throws DaoException if error occurs while updating object in the data source
     */
    T update(long id, T t) throws DaoException;

    /**
     * Deletes from data source the object with provided id.
     *
     * @param id id of entity in the data source that would be deleted
     * @return deleted object
     * @throws DaoException if error occurs while deleting object by id from the data source
     */
    T delete(long id) throws DaoException;
}
