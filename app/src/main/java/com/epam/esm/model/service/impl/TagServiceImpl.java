package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.validator.EntityValidator;
import com.epam.esm.model.validator.TagValidator;
import com.epam.esm.util.QueryCustomizer;
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
    public long add(Tag tag) throws ServiceException {
        if (tag == null) {
            throw new IllegalArgumentException("The supplied [Tag] is " +
                    "required and must not be null");
        }
        DataBinder dataBinder = new DataBinder(tag);
        dataBinder.addValidators(tagValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("The supplied [Tag] has invalid fields");
        }
        try {
            return tagDao.add(tag);
        } catch (DaoException e) {
            throw new ServiceException("Error while adding tag", e);
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
            throw new ServiceException("Error while finding gift certificate by id", e);
        }
    }

    @Override
    public List<Tag> findAll() throws ServiceException {
        try {
            return tagDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while finding all gift certificates", e);
        }
    }

    @Override
    public List<Tag> findAll(QueryCustomizer queryCustomizer) throws ServiceException {
        if (queryCustomizer == null) {
            throw new IllegalArgumentException("The supplied [QueryCustomizer] is " +
                    "required and must not be null");
        }
        try {
            return tagDao.findAll(queryCustomizer);
        } catch (DaoException e) {
            throw new ServiceException("Error while finding all gift certificates", e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        if (!EntityValidator.isIdValid(id)) {
            throw new IllegalArgumentException("Id must be positive");
        }
        try {
            tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting gift certificate", e);
        }
    }
}
