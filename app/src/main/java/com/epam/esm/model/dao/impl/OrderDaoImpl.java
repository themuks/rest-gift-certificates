package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.OrderDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {
    public OrderDaoImpl() {
        setClazz(Order.class);
    }
}
