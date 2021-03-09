package com.epam.esm.model.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.validator.*;
import com.epam.esm.util.CriteriaConstructor;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final Validator giftCertificateValidator = new GiftCertificateValidator(new TagValidator());
    private final Validator proxyGiftCertificateValidator = new ProxyGiftCertificateValidator(new TagValidator());

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate add(GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificate == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(proxyGiftCertificateValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        List<Tag> tags = giftCertificate.getTags();
        if (tags != null) {
            List<Tag> newTags = new ArrayList<>();
            for (Tag tag : tags) {
                try {
                    Long id = tag.getId();
                    if (id != null) {
                        Optional<Tag> tagOptional = tagDao.findById(id);
                        if (tagOptional.isEmpty()) {
                            tag.setId(null);
                            newTags.add(tagDao.add(tag));
                        } else {
                            newTags.add(tagOptional.get());
                        }
                    } else {
                        newTags.add(tag);
                    }
                } catch (DaoException e) {
                    throw new ServiceException(e.getLocalizedMessage(), e);
                }
            }
            giftCertificate.setTags(newTags);
        }
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
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
    public List<GiftCertificate> findAll(List<String> sortField,
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
            return giftCertificateDao.findAll(searchCriteria, sortCriteria, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public GiftCertificate update(long id, GiftCertificate patch) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        if (patch == null) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] is required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(patch);
        dataBinder.addValidators(giftCertificateValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [GiftCertificate] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        Optional<GiftCertificate> optionalGiftCertificate = findById(id);
        if (optionalGiftCertificate.isPresent()) {
            GiftCertificate giftCertificate = optionalGiftCertificate.get();
            Optional.ofNullable(patch.getName()).ifPresent(giftCertificate::setName);
            Optional.ofNullable(patch.getDescription()).ifPresent(giftCertificate::setDescription);
            Optional.ofNullable(patch.getPrice()).ifPresent(giftCertificate::setPrice);
            Optional.ofNullable(patch.getDurationInDays()).ifPresent(giftCertificate::setDurationInDays);
            Optional.ofNullable(patch.getCreateDate()).ifPresent(giftCertificate::setCreateDate);
            Optional.ofNullable(patch.getLastUpdateDate()).ifPresentOrElse(giftCertificate::setLastUpdateDate,
                    () -> giftCertificate.setLastUpdateDate(LocalDateTime.now()));
            Optional.ofNullable(patch.getTags()).ifPresent(giftCertificate::setTags);
            try {
                return giftCertificateDao.update(giftCertificate);
            } catch (DaoException e) {
                throw new ServiceException(e.getLocalizedMessage(), e);
            }
        } else {
            throw new ServiceException("Gift certificate with such id = (" + id + ") is not exist");
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
    public List<GiftCertificate> findByTagName(List<String> tagName,
                                               List<String> sortField,
                                               List<String> sortType,
                                               List<String> searchField,
                                               List<String> searchExpression,
                                               int offset,
                                               int limit) throws ServiceException {
        if (tagName == null) {
            throw new IllegalArgumentException("The supplied tag names is required and must not be null");
        }
        if (!QueryParameterValidator.isOffsetValid(offset) || !QueryParameterValidator.isLimitValid(limit)) {
            throw new IllegalArgumentException("Query parameters such as offset or/and limit are incorrect");
        }
        List<SearchUnit> searchCriteria = CriteriaConstructor.convertListsToSearchCriteria(searchField, searchExpression);
        List<SortUnit> sortCriteria = CriteriaConstructor.convertListsToSortCriteria(sortField, sortType);
        try {
            return giftCertificateDao.findByTagName(tagName, searchCriteria, sortCriteria, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }
}
