package com.project.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.project.domain.*;

public class Manager {
    private static SessionFactory factory;


    public static Autor addAutor(String autorName){
        Autor newAutor = new Autor(autorName);

        return newAutor;
    }

    public static Persona addPersona(String dni, String nom, String telefon, String email){
        Persona newPersona = new Persona(dni, nom, telefon, email);

        return newPersona;
    }

    public static Llibre addLlibre(String ibsn, String titol, String editorial, Integer anyPublicacio){
        Llibre newLlibre = new Llibre(ibsn, titol, editorial, anyPublicacio);

        return newLlibre;
    }

    public static Biblioteca addBiblioteca(String nom, String ciutat, String adreca, String telefon, String email){
        Biblioteca newBiblioteca = new Biblioteca(nom, ciutat, adreca, telefon, email);

        return newBiblioteca;
    }

    public static Exemplar addExemplar(String codiBarres, Llibre llibre, Biblioteca biblioteca){
        Exemplar newExemplar = new Exemplar(codiBarres, llibre, biblioteca);

        return newExemplar;
    }

    public static void updateAutor(long autorId, String autorNom, Set<Llibre> llibres){
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Autor autor = session.get(Autor.class, autorId);
                if (autor != null) {
                    autor.setNom(autorNom);
                    autor.setLlibres(llibres);
                    session.merge(autor);
                }
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    /**
     * Llista entitats amb filtre opcional.
     */
    public static <T> Collection<?> listCollection(Class<? extends T> clazz, String where) {
        Collection<?> result = null;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                String hql = where.isEmpty() 
                    ? "FROM " + clazz.getName()
                    : "FROM " + clazz.getName() + " WHERE " + where;
                result = session.createQuery(hql, clazz).list();
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
        return result;
    }

    public static <T> Collection<?> listCollection(Class<? extends T> clazz) {
        return listCollection(clazz, "");
    }

    /**
     * Converteix una col·lecció d'entitats a String.
     */
    public static <T> String collectionToString(Class<? extends T> clazz, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        
        StringBuilder txt = new StringBuilder();
        for (Object obj : collection) {
            T cObj = clazz.cast(obj);
            txt.append("\n").append(cObj.toString());
        }
        if (txt.length() > 0) {
            txt.delete(0, 1);  // Eliminem el primer salt de línia
        }
        return txt.toString();
    }

    /**
     * Crea la SessionFactory per defecte
     */
    public static void createSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            
            // Registrem totes les classes que tenen anotacions JPA
            configuration.addAnnotatedClass(Biblioteca.class);
            configuration.addAnnotatedClass(Llibre.class);
            configuration.addAnnotatedClass(Exemplar.class);
            configuration.addAnnotatedClass(Prestec.class);
            configuration.addAnnotatedClass(Persona.class);
            configuration.addAnnotatedClass(Autor.class);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("No s'ha pogut crear la SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Crea la SessionFactory amb un fitxer de propietats específic
     */
    public static void createSessionFactory(String propertiesFileName) {
        try {
            Configuration configuration = new Configuration();
            
            configuration.addAnnotatedClass(Biblioteca.class);
            configuration.addAnnotatedClass(Llibre.class);
            configuration.addAnnotatedClass(Exemplar.class);
            configuration.addAnnotatedClass(Prestec.class);
            configuration.addAnnotatedClass(Persona.class);
            configuration.addAnnotatedClass(Autor.class);

            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new IOException("No s'ha trobat " + propertiesFileName);
                }
                properties.load(input);
            }

            configuration.addProperties(properties);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Error creant la SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Tanca la SessionFactory
     */
    public static void close() {
        if (factory != null) {
            factory.close();
        }
    }
}
