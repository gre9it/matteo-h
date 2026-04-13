package fr.turgot.dao;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import fr.turgot.dao.model.Etudiant;
import fr.turgot.dao.model.User;
import fr.turgot.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserDAO {

    private static final EntityManagerFactory emf = JPAUtil.getEMF();

    public User authenticate(String username, String plainPassword) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :u", User.class);
            query.setParameter("u", username);
            User user = query.getSingleResult();
            if (BCrypt.checkpw(plainPassword, user.getPassword())) {
                return user;
            }
        } catch (NoResultException e) {
            // utilisateur introuvable
        }
        return null;
    }

    public boolean userExists(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.username = :u", Long.class)
                    .setParameter("u", username)
                    .getSingleResult();
            return count > 0;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean createUser(String username, String plainPassword) {
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                Etudiant e = new Etudiant();
                e.setUsername(username);
                e.setPassword(hash);
                e.setRole(User.Role.ETUDIANT);
                em.persist(e);
                tx.commit();
                return true;
            } catch (Exception ex) {
                if (tx.isActive())
                    tx.rollback();
                ex.printStackTrace();
                return false;
            }
        }
    }

    public List<User> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT u FROM User u ORDER BY u.role, u.username", User.class)
                    .getResultList();
        }
    }

    public User findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(User.class, id);
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean changeRole(int userId, User.Role nouveauRole) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                User user = em.find(User.class, userId);
                if (user == null)
                    return false;

                User.Role ancienRole = user.getRole();
                if (ancienRole == nouveauRole)
                    return true;

                // 1. Supprime l'entrée table fille ancien rôle
                String ancienneTable = tableDeRole(ancienRole);
                if (ancienneTable != null) {
                    em.createNativeQuery("DELETE FROM " + ancienneTable + " WHERE id = :id")
                            .setParameter("id", userId)
                            .executeUpdate();
                }

                // 2. Met à jour le rôle dans user
                em.createNativeQuery("UPDATE user SET role = :role WHERE id = :id")
                        .setParameter("role", nouveauRole.name())
                        .setParameter("id", userId)
                        .executeUpdate();

                // 3. Insère dans la nouvelle table fille
                String nouvelleTable = tableDeRole(nouveauRole);
                if (nouvelleTable != null) {
                    em.createNativeQuery("INSERT INTO " + nouvelleTable + " (id) VALUES (:id)")
                            .setParameter("id", userId)
                            .executeUpdate();
                }

                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive())
                    tx.rollback();
                e.printStackTrace();
                return false;
            }
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public boolean deleteUser(int userId) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                User user = em.find(User.class, userId);
                if (user == null)
                    return false;

                // 1. Supprime les emprunts du user
                em.createNativeQuery("DELETE FROM emprunt WHERE user_id = :id")
                        .setParameter("id", userId)
                        .executeUpdate();

                // 2. Supprime la table fille
                String table = tableDeRole(user.getRole());
                if (table != null) {
                    em.createNativeQuery("DELETE FROM " + table + " WHERE id = :id")
                            .setParameter("id", userId)
                            .executeUpdate();
                }

                // 3. Supprime le user
                em.createNativeQuery("DELETE FROM user WHERE id = :id")
                        .setParameter("id", userId)
                        .executeUpdate();

                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx.isActive())
                    tx.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private String tableDeRole(User.Role role) {
        return switch (role) {
            case ETUDIANT -> "etudiant";
            case PROFESSEUR -> "professeur";
            case BIBLIOTHECAIRE -> "bibliothecaire";
            case ADMIN -> "admin";
        };
    }
}