package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private GiftCertificateDao giftCertificateDao;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao, orderDao, giftCertificateDao, passwordEncoder);
    }

    @Test
    void findById_InvalidIdGiven_ShouldThrowInvalidArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.findById(0));
    }

    @Test
    void findById_ValidIdGivenObjectExists_ShouldReturnOptionalWithObject() throws DaoException {
        User user = User.builder()
                .id(1L)
                .name("name")
                .build();
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = userService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        if (optionalUser.isPresent()) {
            User actual = optionalUser.get();
            assertEquals(user, actual);
        } else {
            fail();
        }
    }

    @Test
    void findById_ValidIdGivenObjectNotExist_ShouldReturnEmptyOptional() throws DaoException {
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = userService.findById(1L);
        } catch (ServiceException e) {
            fail(e);
        }
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void findById_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(userDao.findById(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> userService.findById(1));
    }

    @Test
    void findAll_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(userDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenThrow(new DaoException());
        assertThrows(ServiceException.class,
                () -> userService.findAll(List.of(), List.of(), List.of(), List.of(), 1, 1));
    }

    @Test
    void findAll_ValidParametersGiven_ShouldReturnList() throws DaoException {
        List<User> expected = List.of(new User());
        when(userDao.findAll(anyList(), anyList(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<User> actual = userService.findAll(new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    0,
                    501);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findAll_InvalidParametersGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.findAll(null, null, null, null, -1, -1));
    }

    @Test
    void findOrdersOfUser_ValidParametersGiven_OrdersOfUserReturned() throws DaoException {
        List<Order> expected = List.of(new Order());
        when(userDao.findOrdersOfUser(anyLong(), anyInt(), anyInt())).thenReturn(expected);
        try {
            List<Order> actual = userService.findOrdersOfUser(1L, 0, 501);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void findOrdersOfUser_InvalidParametersGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.findOrdersOfUser(0, -1, -1));
    }

    @Test
    void findOrdersOfUser_DaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        when(userDao.findOrdersOfUser(anyLong(), anyInt(), anyInt())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> userService.findOrdersOfUser(1L, 0, 501));
    }

    @Test
    void makeOrderOnUser_ValidParametersGiven_OrderReturned() throws DaoException {
        Order expected = new Order();
        when(userDao.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        when(orderDao.add(any(Order.class))).thenReturn(expected);
        try {
            Order actual = userService.makeOrderOnGiftCertificate(1L, 1L);
            assertEquals(expected, actual);
        } catch (ServiceException e) {
            fail(e);
        }
    }

    @Test
    void makeOrderOnUser_InvalidParametersGiven_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.makeOrderOnGiftCertificate(0, 0));
    }

    @Test
    void makeOrderOnUser_UserDaoThrownDaoException_ShouldThrowServiceException() throws DaoException {
        when(userDao.findById(anyLong())).thenThrow(new DaoException());
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        assertThrows(ServiceException.class, () -> userService.makeOrderOnGiftCertificate(1L, 1L));
    }

    @Test
    void makeOrderOnUser_GiftCertificateDaoThrownDaoException_ShouldThrowServiceException() throws DaoException {
        when(giftCertificateDao.findById(anyLong())).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> userService.makeOrderOnGiftCertificate(1L, 1L));
    }

    @Test
    void makeOrderOnUser_OrderDaoThrownDaoException_ShouldThrowServiceException() throws DaoException {
        when(userDao.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(new GiftCertificate()));
        when(orderDao.add(any(Order.class))).thenThrow(new DaoException());
        assertThrows(ServiceException.class, () -> userService.makeOrderOnGiftCertificate(1L, 1L));
    }
}
