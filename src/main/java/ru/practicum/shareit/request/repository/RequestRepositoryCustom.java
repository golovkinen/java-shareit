package ru.practicum.shareit.request.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RequestRepositoryCustom implements IRequestRepositoryCustom {


    private EntityManager entityManager;

    public RequestRepositoryCustom(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Request> getPagedRequests(int from, int size, int userId) {
        return entityManager.createQuery("select r from Request r where r.user.id <> :userId")
                .setParameter("userId", userId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
