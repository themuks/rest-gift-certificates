package com.epam.esm.model.dao;

import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public abstract class AbstractDao<T> implements Dao<T> {
    private static final String FROM = "from ";
    private static final String PERCENT = "%";
    protected final EntityManager entityManager;
    private Class<T> clazz;

    public AbstractDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected static <T> List<Order> extractOrderList(List<SortUnit> sortCriteria,
                                                      CriteriaBuilder criteriaBuilder,
                                                      Root<T> root) {
        List<Order> orderList = new ArrayList<>();
        for (SortUnit sortCriterion : sortCriteria) {
            Path<Object> objectPath = root.get(sortCriterion.getSortField());
            if (sortCriterion.isAscending()) {
                orderList.add(criteriaBuilder.asc(objectPath));
            } else {
                orderList.add(criteriaBuilder.desc(objectPath));
            }
        }
        return orderList;
    }

    protected static <T> List<Predicate> extractPredicates(List<SearchUnit> searchCriteria,
                                                           CriteriaBuilder criteriaBuilder,
                                                           Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchUnit searchCriterion : searchCriteria) {
            predicates.add(criteriaBuilder.like(root.get(searchCriterion.getSearchFieldName()),
                    PERCENT + searchCriterion.getSearchExpression() + PERCENT));
        }
        return predicates;
    }

    public void setClazz(Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    @Override
    public T add(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<T> findAll(int offset, int limit) {
        return entityManager.createQuery(FROM + clazz.getName(), clazz)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<T> findAll(List<SearchUnit> searchCriteria, List<SortUnit> sortCriteria, int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        List<Predicate> predicates = extractPredicates(searchCriteria, criteriaBuilder, root);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<Order> orderList = extractOrderList(sortCriteria, criteriaBuilder, root);
        criteriaQuery.orderBy(orderList);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public T delete(long id) throws DaoException {
        Optional<T> optionalEntity = findById(id);
        T entity = optionalEntity.orElseThrow(() ->
                new DaoException("message.exception.dao.not_found"));
        delete(entity);
        return entity;
    }

    private void delete(T entity) {
        entityManager.remove(entity);
    }
}
