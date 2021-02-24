package com.epam.esm.model.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {
    List<Order> findOrdersOfUser(long id, int offset, int limit) throws DaoException;
}
