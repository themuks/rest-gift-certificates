package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private static final String FIND_ORDERS_OF_USER_QUERY = """
                select o 
                from Order o 
                where o.user.id = :id
            """;
    private static final String ID = "id";

    public UserDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setClazz(User.class);
    }

    @Override
    public List<Order> findOrdersOfUser(long id, int offset, int limit) {
        return entityManager.createQuery(FIND_ORDERS_OF_USER_QUERY, Order.class)
                .setParameter(ID, id)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
