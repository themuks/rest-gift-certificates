package com.epam.esm.model.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.QuerySorter;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    long add(GiftCertificate giftCertificate) throws ServiceException;

    Optional<GiftCertificate> findById(long id) throws ServiceException;

    List<GiftCertificate> findAll() throws ServiceException;

    List<GiftCertificate> findAll(QuerySorter querySorter) throws ServiceException;

    void update(long id, GiftCertificate giftCertificate) throws ServiceException;

    void delete(long id) throws ServiceException;

    List<GiftCertificate> findByTagName(String tagName) throws ServiceException;

    List<GiftCertificate> findByTagName(String tagName, QuerySorter querySorter) throws ServiceException;
}
