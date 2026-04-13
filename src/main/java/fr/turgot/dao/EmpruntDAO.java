package fr.turgot.dao;

import java.time.LocalDate;
import java.util.List;

import fr.turgot.dao.model.Emprunt;
import fr.turgot.dao.model.Ressource;
import fr.turgot.dao.model.User;
import fr.turgot.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class EmpruntDAO {

    private static final EntityManagerFactory emf = JPAUtil.getEMF();

    public List<Emprunt> findByUser(int userId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                "SELECT e FROM Emprunt e WHERE e.user.id = :uid ORDER BY e.dateEmprunt DESC",
                Emprunt.class)
                .setParameter("uid", userId)
                .getResultList();
        }
    }

    public long countEnCours(int userId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                "SELECT COUNT(e) FROM Emprunt e WHERE e.user.id = :uid AND e.dateRetourEffective IS NULL",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean create(User user, Ressource ressource, int dureeJours) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Emprunt emprunt = new Emprunt(user, ressource, dureeJours);
                em.persist(emprunt);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
                return false;
            }
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean retourner(int empruntId, int userId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Emprunt emprunt = em.find(Emprunt.class, empruntId);
                if (emprunt == null || emprunt.getUser().getId() != userId) return false;

                emprunt.setDateRetourEffective(LocalDate.now());

                // Remet un exemplaire disponible
                Ressource r = em.find(Ressource.class, emprunt.getRessource().getId());
                if (r != null) r.giveBack();

                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
                return false;
            }
        }
    }

    public List<Emprunt> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                "SELECT e FROM Emprunt e ORDER BY e.dateEmprunt DESC",
                Emprunt.class)
                .getResultList();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean forcerRetour(int empruntId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Emprunt emprunt = em.find(Emprunt.class, empruntId);
                if (emprunt == null || !emprunt.isOngoing()) return false;
            
                emprunt.setDateRetourEffective(java.time.LocalDate.now());
            
                // Remet un exemplaire disponible
                Ressource r = em.find(
                    fr.turgot.dao.model.Ressource.class,
                    emprunt.getRessource().getId());
                if (r != null) r.giveBack();
                
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
                return false;
            }
        }
    }
}