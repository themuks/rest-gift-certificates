package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.validator.EntityValidator;
import com.epam.esm.model.validator.QueryParameterValidator;
import com.epam.esm.util.CriteriaConstructor;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;

    public UserServiceImpl(UserDao userDao, OrderDao orderDao, GiftCertificateDao giftCertificateDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public Optional<User> findById(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<User> findAll(List<String> sortField,
                              List<String> sortType,
                              List<String> searchField,
                              List<String> searchExpression,
                              int offset,
                              int limit) throws ServiceException {
        if (!QueryParameterValidator.isOffsetValid(offset) || !QueryParameterValidator.isLimitValid(limit)) {
            throw new IllegalArgumentException("Query parameters such as offset or/and limit are incorrect");
        }
        List<SearchUnit> searchCriteria = CriteriaConstructor.convertListsToSearchCriteria(searchField, searchExpression);
        List<SortUnit> sortCriteria = CriteriaConstructor.convertListsToSortCriteria(sortField, sortType);
        try {
            return userDao.findAll(searchCriteria, sortCriteria, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<Order> findOrdersOfUser(long id, int offset, int limit) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        if (!QueryParameterValidator.isOffsetValid(offset) || !QueryParameterValidator.isLimitValid(limit)) {
            throw new IllegalArgumentException("Query parameters such as offset or/and limit are incorrect");
        }
        try {
            return userDao.findOrdersOfUser(id, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Order makeOrderOnGiftCertificate(long userId, long giftCertificateId) throws ServiceException {
        if (!EntityValidator.isIdValid(userId) || !EntityValidator.isIdValid(giftCertificateId)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        Optional<GiftCertificate> optionalGiftCertificate;
        try {
            optionalGiftCertificate = giftCertificateDao.findById(giftCertificateId);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
        GiftCertificate giftCertificate = optionalGiftCertificate.orElseThrow(() ->
                new ServiceException("Gift certificate with id = (" + giftCertificateId + ") doesn't exist"));
        Optional<User> optionalUser;
        try {
            optionalUser = userDao.findById(userId);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
        User user = optionalUser.orElseThrow(() ->
                new ServiceException("User with id = (" + giftCertificateId + ") doesn't exist"));
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .user(user)
                .giftCertificate(giftCertificate)
                .cost(giftCertificate.getPrice())
                .build();
        try {
            return orderDao.add(order);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }
}
