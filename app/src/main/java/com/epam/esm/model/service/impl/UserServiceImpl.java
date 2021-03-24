package com.epam.esm.model.service.impl;

import com.epam.esm.entity.*;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import com.epam.esm.model.validator.EntityValidator;
import com.epam.esm.model.validator.QueryParameterValidator;
import com.epam.esm.model.validator.UserValidator;
import com.epam.esm.util.CriteriaConstructor;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final Validator userValidator = new UserValidator();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, OrderDao orderDao, GiftCertificateDao giftCertificateDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.passwordEncoder = passwordEncoder;
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
        User user;
        try {
            user = userDao.findById(userId).orElseThrow(() ->
                    new ServiceException("User with id = (" + userId + ") doesn't exist"));
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateDao.findById(giftCertificateId).orElseThrow(() ->
                    new ServiceException("Gift certificate with id = (" + giftCertificateId + ") doesn't exist"));
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
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

    @Override
    public Optional<User> findByEmail(String email) throws ServiceException {
        if (email == null) {
            throw new IllegalArgumentException("Email is null");
        }
        try {
            return userDao.findByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean register(String email, String password, String name, String surname) throws ServiceException {
        if (email == null || password == null || name == null || surname == null) {
            throw new IllegalArgumentException("Email, password, name and surname are required!");
        }
        User user = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .surname(surname)
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(userValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [User] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        try {
            Optional<User> optionalUser = userDao.findByEmail(email.toLowerCase());
            if (optionalUser.isPresent()) {
                return false;
            }
        } catch (DaoException e) {
            throw new ServiceException("Error while finding user by email", e);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userDao.add(user);
        } catch (DaoException e) {
            throw new ServiceException("Error while adding new user", e);
        }
        return true;
    }
}
