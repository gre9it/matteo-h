package fr.turgot.dao;

import fr.turgot.dao.model.Ressource;
import fr.turgot.dao.model.Livre;
import fr.turgot.dao.model.Document;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class RessourceDAO {

    public void save(Ressource ressource) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ressource);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la sauvegarde de la ressource : " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void update(Ressource ressource) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ressource);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la mise à jour : " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Ressource r = em.find(Ressource.class, id);
            if (r != null) em.remove(r);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Optional<Ressource> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Ressource r = em.find(Ressource.class, id);
            return Optional.ofNullable(r);
        } finally {
            em.close();
        }
    }

    public List<Ressource> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Ressource r ORDER BY r.title", Ressource.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livre> findAllLivres() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Livre l ORDER BY l.title", Livre.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Document> findAllDocuments() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Document d ORDER BY d.title", Document.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
