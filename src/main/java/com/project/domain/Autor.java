package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autors")
public class Autor implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private long autorId;
    
    @Column(nullable = false, length = 50)
    private String nom;
    
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "autor_llibre",
        joinColumns = @JoinColumn(name = "autor_id"),
        inverseJoinColumns = @JoinColumn(name = "llibre_id")
    )
    private Set<Llibre> llibres = new HashSet<>();
    
    public Autor(){

    }

    public Autor(String nom) {
        this.nom = nom;
    }

    public Autor(long autorId, String nom, Set<Llibre> llibres) {
        this.autorId = autorId;
        this.nom = nom;
        this.llibres = llibres;
    }

    public long getAutorId() {
        return autorId;
    }

    public void setAutorId(long autorId) {
        this.autorId = autorId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Llibre> getLlibres() {
        return llibres;
    }

    public void setLlibres(Set<Llibre> llibres) {
        this.llibres = llibres;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Autor[id=%d, nom='%s'", autorId, nom));
        
        if (!llibres.isEmpty()) {
            sb.append(", llibres={");
            boolean first = true;
            for (Llibre ll : llibres) {
                if (!first) sb.append(", ");
                sb.append(String.format("'%s'", ll.getTitol()));
                first = false;
            }
            sb.append("}");
        }
        
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return autorId == autor.autorId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(autorId);
    }
}