package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "prestecs")
public class Prestec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prestec_id")
    private long prestecId;
    
    @ManyToOne
    @JoinColumn(name = "exemplar_id")
    private Exemplar exemplar;
    
    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
    
    @Column(nullable = false, length = 50)
    private LocalDate dataPrestec;
    
    @Column(nullable = false, length = 50)
    private LocalDate dataRetornPrevista;

    @Column(length = 50)
    private LocalDate dataRetornReal;
    
    @Column(length = 50)
    private boolean actiu;

    public Prestec(){
        
    }

    public Prestec(Exemplar exemplar, Persona persona, LocalDate dataPrestec, LocalDate dataRetornPrevista) {
        this.exemplar = exemplar;
        this.persona = persona;
        this.dataPrestec = dataPrestec;
        this.dataRetornPrevista = dataRetornPrevista;
    }

    public Prestec(long prestecId, Exemplar exemplar, Persona persona, LocalDate dataPrestec, LocalDate dataRetornPrevista,
            LocalDate dataRetornReal, boolean actiu) {
        this.prestecId = prestecId;
        this.exemplar = exemplar;
        this.persona = persona;
        this.dataPrestec = dataPrestec;
        this.dataRetornPrevista = dataRetornPrevista;
        this.dataRetornReal = dataRetornReal;
        this.actiu = actiu;
    }

    public long getPrestecId() {
        return prestecId;
    }

    public void setPrestecId(long prestecId) {
        this.prestecId = prestecId;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDate getDataPrestec() {
        return dataPrestec;
    }

    public void setDataPrestec(LocalDate dataPrestec) {
        this.dataPrestec = dataPrestec;
    }

    public LocalDate getDataRetornPrevista() {
        return dataRetornPrevista;
    }

    public void setDataRetornPrevista(LocalDate dataRetornPrevista) {
        this.dataRetornPrevista = dataRetornPrevista;
    }

    public LocalDate getDataRetornReal() {
        return dataRetornReal;
    }

    public void setDataRetornReal(LocalDate dataRetornReal) {
        this.dataRetornReal = dataRetornReal;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public boolean estaRetardat(){
        boolean retardat = false;
        if (dataRetornPrevista.isBefore(LocalDate.now())){
            retardat = true;
        }
        return retardat;
    }

    public int getDiesRetard(){
        int dies = 0;
        dies = (int) ChronoUnit.DAYS.between(dataRetornPrevista, LocalDate.now());
        return dies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Prestec[id=%d", prestecId));
        
        if (exemplar != null) {
            sb.append(String.format(", exemplar='%s'", exemplar.getCodiBarres()));
        }
        
        if (persona != null) {
            sb.append(String.format(", persona='%s'", persona.getNom()));
        }
        
        sb.append(String.format(", dataPrestec='%s'", dataPrestec));
        sb.append(String.format(", dataRetornPrevista='%s'", dataRetornPrevista));
        
        if (dataRetornReal != null) {
            sb.append(String.format(", dataRetornReal='%s'", dataRetornReal));
        }
        
        sb.append(String.format(", actiu=%s", actiu));
        
        if (estaRetardat()) {
            sb.append(String.format(", diesRetard=%d", getDiesRetard()));
        }
        
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestec prestec = (Prestec) o;
        return prestecId == prestec.prestecId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(prestecId);
    }
}