package com.epam.esm.model.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends BaseDao<GiftCertificate> {
    List<GiftCertificate> findByTagId(long id) throws DaoException;

    List<GiftCertificate> findByTagName(String tagName) throws DaoException;

    List<GiftCertificate> findByTagName(String tagName, QueryCustomizer queryCustomizer) throws DaoException;
}
