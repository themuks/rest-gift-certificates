package com.epam.esm.model.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.dao.AbstractDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.dao.exception.EntityWithIdNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    private static final String FIND_USER_WITH_HIGHEST_ORDER_COST = """
                SELECT id,
                       (SELECT sum(o.cost)
                        FROM user AS u
                                 JOIN `order` o on u.id = o.user_id
                        WHERE u.id = user.id) as total
                FROM user
                ORDER BY total DESC
                LIMIT 1
            """;
    private static final String USER_ID = "user_id";
    private static final int ZERO = 0;
    private static final String FIND_MOST_USED_TAG_BY_USER_ID_QUERY = """
            SELECT distinct tag.id, count(*) as magnitude
            FROM tag
                     INNER JOIN gift_certificate_has_tag gcht on tag.id = gcht.tag_id
                     INNER JOIN gift_certificate gc on gcht.gift_certificate_id = gc.id
                     INNER JOIN `order` o on gc.id = o.gift_certificate_id
            WHERE user_id = :user_id
            GROUP BY tag.id
            ORDER BY magnitude DESC
            LIMIT 1
            """;

    public TagDaoImpl() {
        setClazz(Tag.class);
    }

    @Override
    public Tag findMostUsedTag() throws DaoException {
        Object[] result = (Object[]) entityManager.createNativeQuery(FIND_USER_WITH_HIGHEST_ORDER_COST)
                .getSingleResult();
        long userId = ((BigInteger) result[ZERO]).longValue();
        result = (Object[]) entityManager.createNativeQuery(FIND_MOST_USED_TAG_BY_USER_ID_QUERY)
                .setParameter(USER_ID, userId).getSingleResult();
        long mostUsedTagId = ((BigInteger) result[ZERO]).longValue();
        Optional<Tag> tagOptional = findById(mostUsedTagId);
        return tagOptional.orElseThrow(() -> new EntityWithIdNotFoundException(mostUsedTagId,
                "message.exception.dao.not_found"));
    }
}
