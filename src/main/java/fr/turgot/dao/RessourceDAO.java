package fr.turgot.dao;

import java.time.LocalDate;
import java.util.List;

import fr.turgot.dao.model.Document;
import fr.turgot.dao.model.Livre;
import fr.turgot.dao.model.Ressource;
import fr.turgot.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class RessourceDAO {

    private static final EntityManagerFactory emf = JPAUtil.getEMF();

    public List<Ressource> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT r FROM Ressource r", Ressource.class)
                    .getResultList();
        }
    }

    public Ressource findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Ressource.class, id);
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean borrow(int ressourceId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Ressource r = em.find(Ressource.class, ressourceId);
                if (r == null || r.getAvailableCopies() == 0) return false;
                r.borrow();
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
    public boolean giveBack(int ressourceId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Ressource r = em.find(Ressource.class, ressourceId);
                if (r == null) return false;
                r.giveBack();
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean createLivre(Livre livre) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                em.persist(livre);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    public boolean createDocument(Document document) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                em.persist(document);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Ressource r = em.find(Ressource.class, id);
                if (r != null) em.remove(r);
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
    public boolean update(int id, String type, String title, String description,
            String auteur, LocalDate date, int availableCopies,
            String genre, String edition, String typeDoc) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Ressource r = em.find(Ressource.class, id);
                if (r == null) return false;
                r.setTitle(title);
                r.setDescription(description);
                r.setAuteur(auteur);
                r.setDateParution(date);
                r.setAvailableCopies(availableCopies);
                if (r instanceof Livre && "livre".equals(type)) {
                    ((Livre) r).setGenre(genre);
                    ((Livre) r).setEdition(edition);
                } else if (r instanceof Document && "document".equals(type)) {
                    ((Document) r).setType(typeDoc);
                }
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