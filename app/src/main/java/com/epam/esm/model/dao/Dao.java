package com.epam.esm.model.dao;

import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;

import java.util.List;
import java.util.Optional;

/**
 * The interface Base dao provides methods for basic CRUD operations.
 *
 * @param <T> the type parameter
 */
public interface Dao<T> {
    /**
     * Adds provided object to the data source.
     *
     * @param entity generic type object
     * @return added object
     * @throws DaoException if error occurs while adding object to the data source
     */
    T add(T entity) throws DaoException;

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
     * @param offset count of records to skip
     * @param limit  maximum count of records to return
     * @return list of all objects from data source
     * @throws DaoException the dao exception
     */
    List<T> findAll(int offset, int limit) throws DaoException;

    /**
     * Finds all generic type objects in the repository. Result depends on search and sort criteria
     * provided to the method.
     *
     * @param searchCriteria describes data to search by
     * @param sortCriteria   describes how to sort fetched data
     * @param offset         count of records to skip
     * @param limit          maximum count of records to return
     * @return list of objects from data source, order and content of which controlled by {@code QueryCustomizer}
     * @throws DaoException if error occurs while finding objects in the data source
     */
    List<T> findAll(List<SearchUnit> searchCriteria, List<SortUnit> sortCriteria, int offset, int limit) throws DaoException;

    /**
     * Updates object in the data source with provided id to new values from provided generic type object.
     * If provided object field value is null this field will not be updated.
     *
     * @param entity object with values to be updated
     * @return added object
     * @throws DaoException if error occurs while updating object in the data source
     */
    T update(T entity) throws DaoException;

    /**
     * Deletes from data source the object with provided id.
     *
     * @param id id of entity in the data source that would be deleted
     * @return deleted object
     * @throws DaoException if error occurs while deleting object by id from the data source
     */
    T delete(long id) throws DaoException;
}
