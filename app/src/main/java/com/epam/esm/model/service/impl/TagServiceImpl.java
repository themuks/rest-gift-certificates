package com.epam.esm.model.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public long add(Tag tag) throws ServiceException {
        try {
            return tagDao.add(tag);
        } catch (DaoException e) {
            throw new ServiceException("Error while adding tag", e);
        }
    }

    @Override
    public Optional<Tag> findById(long id) throws ServiceException {
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
    public void delete(long id) throws ServiceException {
        try {
            tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting gift certificate", e);
        }
    }
}
