package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.QueryCustomizer;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public long add(GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificate == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is " +
                    "required and must not be null");
        }
        try {
            return giftCertificateDao.add(giftCertificate);
        } catch (DaoException e) {
            throw new ServiceException("Error while adding gift certificate", e);
        }
    }

    @Override
    public Optional<GiftCertificate> findById(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            return giftCertificateDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while finding gift certificate by id", e);
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws ServiceException {
        return findAll(new QueryCustomizer());
    }

    @Override
    public List<GiftCertificate> findAll(QueryCustomizer queryCustomizer) throws ServiceException {
        if (queryCustomizer == null) {
            throw new IllegalArgumentException("The supplied [QueryCustomizer] is " +
                    "required and must not be null");
        }
        try {
            return giftCertificateDao.findAll(queryCustomizer);
        } catch (DaoException e) {
            throw new ServiceException("Error while finding all gift certificates", e);
        }
    }

    @Override
    public void update(long id, GiftCertificate giftCertificate) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        if (giftCertificate == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is " +
                    "required and must not be null");
        }
        try {
            giftCertificateDao.update(id, giftCertificate);
        } catch (DaoException e) {
            throw new ServiceException("Error while updating gift certificate", e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            giftCertificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting gift certificate", e);
        }
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) throws ServiceException {
        if (tagName == null) {
            throw new IllegalArgumentException("The supplied [String] is " +
                    "required and must not be null");
        }
        return findByTagName(tagName, new QueryCustomizer());
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName, QueryCustomizer queryCustomizer) throws ServiceException {
        if (tagName == null) {
            throw new IllegalArgumentException("The supplied [String] is " +
                    "required and must not be null");
        }
        if (queryCustomizer == null) {
            throw new IllegalArgumentException("The supplied [QueryCustomizer] is " +
                    "required and must not be null");
        }
        try {
            return giftCertificateDao.findByTagName(tagName, queryCustomizer);
        } catch (DaoException e) {
            throw new ServiceException("Error while finding gift certificate by tag name", e);
        }
    }
}
