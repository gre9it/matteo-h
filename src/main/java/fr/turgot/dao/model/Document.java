package fr.turgot.dao.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documents")
@PrimaryKeyJoinColumn(name = "ressource_id")
@DiscriminatorValue("Document")
public class Document extends Ressource {

    @Enumerated(EnumType.STRING)
    @Column(name = "type_document", length = 20)
    private TypeDocument typeDocument;

    public Document() {}

    public Document(String title, String description, LocalDate dateParution, String auteur,
                    int availableCopies, TypeDocument typeDocument) {
        super(title, description, dateParution, auteur, availableCopies);
        this.typeDocument = typeDocument;
    }

    @Override
    public String getType() {
        return typeDocument != null ? typeDocument.name() : "Document";
    }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }
}
