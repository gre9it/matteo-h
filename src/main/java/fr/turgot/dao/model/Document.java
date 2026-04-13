package fr.turgot.dao.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "document")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Document")
public class Document extends Ressource {

    private String type;

    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
}