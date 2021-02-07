package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.validator.EntityValidator;
import com.epam.esm.model.validator.GiftCertificateValidator;
import com.epam.esm.model.validator.ProxyGiftCertificateValidator;
import com.epam.esm.model.validator.TagValidator;
import com.epam.esm.util.QueryCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final Validator giftCertificateValidator = new GiftCertificateValidator(new TagValidator());
    private final Validator proxyGiftCertificateValidator = new ProxyGiftCertificateValidator(new TagValidator());

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public GiftCertificate add(GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificate == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is " +
                    "required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(proxyGiftCertificateValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        try {
            return giftCertificateDao.add(giftCertificate);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
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
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws ServiceException {
        try {
            return giftCertificateDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
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
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public GiftCertificate update(long id, GiftCertificate giftCertificate) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        if (giftCertificate == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is " +
                    "required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(giftCertificateValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        try {
            return giftCertificateDao.update(id, giftCertificate);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public GiftCertificate delete(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            return giftCertificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<GiftCertificate> findByTagName(String tagName) throws ServiceException {
        if (tagName == null) {
            throw new IllegalArgumentException("The supplied [String] is " +
                    "required and must not be null");
        }
        try {
            return giftCertificateDao.findByTagName(tagName);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
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
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }
}
