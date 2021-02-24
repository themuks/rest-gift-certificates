package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.DaoException;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
@Transactional
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {
    private static final String TAGS = "tags";
    private static final String NAME = "name";

    public GiftCertificateDaoImpl(EntityManager entityManager) {
        super(entityManager);
        setClazz(GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> findByTagName(List<String> tagNames,
                                               List<SearchUnit> searchCriteria,
                                               List<SortUnit> sortCriteria,
                                               int offset,
                                               int limit) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        ListJoin<GiftCertificate, Tag> tagsJoin = root.joinList(TAGS);
        criteriaQuery.select(root);
        List<Predicate> predicates = extractPredicates(searchCriteria, criteriaBuilder, root);
        List<Order> orderList = extractOrderList(sortCriteria, criteriaBuilder, root);
        criteriaQuery.where(criteriaBuilder.and(
                tagsJoin.get(NAME).in(tagNames),
                criteriaBuilder.and(predicates.toArray(new Predicate[0]))
        ));
        criteriaQuery.groupBy(root);
        criteriaQuery.having(
                criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(tagsJoin.get(NAME)),
                        tagNames.stream().distinct().toArray().length
                )
        );
        criteriaQuery.orderBy(orderList);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
