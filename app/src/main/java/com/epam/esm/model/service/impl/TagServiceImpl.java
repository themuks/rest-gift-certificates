package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.validator.EntityValidator;
import com.epam.esm.model.validator.QueryParameterValidator;
import com.epam.esm.model.validator.TagValidator;
import com.epam.esm.util.CriteriaConstructor;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final Validator tagValidator = new TagValidator();

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag add(Tag tag) throws ServiceException {
        if (tag == null) {
            throw new IllegalArgumentException("The supplied [Tag] is " +
                    "required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(tag);
        dataBinder.addValidators(tagValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [Tag] has invalid field '"
                    + bindingResult.getFieldError().getField() + "'");
        }
        try {
            return tagDao.add(tag);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Optional<Tag> findById(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            return tagDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<Tag> findAll(List<String> sortField,
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
            return tagDao.findAll(searchCriteria, sortCriteria, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Tag delete(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            return tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Tag findMostUsedTag() throws ServiceException {
        try {
            return tagDao.findMostUsedTag();
        } catch (DaoException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }
}
