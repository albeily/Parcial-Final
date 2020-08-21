package edu.pucmm.survey.service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBMService<T> {

    private static EntityManagerFactory entityManagerFactory;
    private Class<T> classEntity;

    public DBMService(Class<T> classEntity) {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("Survey");
        }
        this.classEntity = classEntity;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public Object getId(T entity) {
        Object id = null;

        if (entity != null) {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        id = field.get(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return id;
    }

    public boolean create(T entity) throws IllegalArgumentException, EntityExistsException, PersistenceException {
        boolean ok = false;
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            ok = true;
        } finally {
            entityManager.close();
        }

        return ok;
    }

    public boolean edit(T entity) throws PersistenceException {
        boolean ok = false;
        EntityManager entityManager = getEntityManager();

        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            ok = true;
        } finally {
            entityManager.close();
        }

        return ok;
    }

    public boolean delete(Object entityId) throws PersistenceException {
        boolean ok = false;
        EntityManager entityManager = getEntityManager();
        T entity;

        entityManager.getTransaction().begin();
        try {
            entity = entityManager.find(this.classEntity, entityId);
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
            ok = true;
        } finally {
            entityManager.close();
        }

        return ok;
    }

    public T find(Object id) throws PersistenceException {
        EntityManager entityManager = getEntityManager();
        T entity = null;

        try {
            entity = entityManager.find(this.classEntity, id);
        } finally {
            entityManager.close();
        }

        return entity;
    }

    public List<T> findAll() throws PersistenceException {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery<T> criteriaQuery;
        List<T> entities = null;

        try {
            criteriaQuery = entityManager.getCriteriaBuilder().createQuery(this.classEntity);
            criteriaQuery.select(criteriaQuery.from(this.classEntity));
            entities = entityManager.createQuery(criteriaQuery).getResultList();
        } finally {
            entityManager.close();
        }

        return entities;
    }

    public List<T> castList(Collection<?> collection) {
        List<T> list = new ArrayList<T>(collection.size());

        for (Object entity : collection) {
            list.add(this.classEntity.cast(entity));
        }

        return list;
    }
}
