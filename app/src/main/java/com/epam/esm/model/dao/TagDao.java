package com.epam.esm.model.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDao extends BaseDao<Tag> {
    List<Tag> findByGiftCertificateId(long id, boolean isNested) throws DaoException;
}
