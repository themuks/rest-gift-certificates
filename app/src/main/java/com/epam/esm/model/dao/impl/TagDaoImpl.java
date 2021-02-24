package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setClazz(Tag.class);
    }
}
