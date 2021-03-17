package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.exception.EntityWithIdNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private static final String FIND_ORDERS_OF_USER_QUERY = """
            select o 
            from Order o 
            where o.user.id = :id
            """;
    private static final String ID = "id";

    public UserDaoImpl() {
        setClazz(User.class);
    }

    @Override
    public List<Order> findOrdersOfUser(long id, int offset, int limit) throws DaoException {
        Optional<User> optionalUser = findById(id);
        optionalUser.orElseThrow(() -> new EntityWithIdNotFoundException(id, "message.exception.dao.not_found"));
        return entityManager.createQuery(FIND_ORDERS_OF_USER_QUERY, Order.class)
                .setParameter(ID, id)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
