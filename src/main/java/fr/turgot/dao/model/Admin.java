package fr.turgot.dao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

    public Admin() {
        this.setRole(Role.ADMIN);
    }
}