package com.project.dao;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.project.domain.*;

public class Manager {
    private static SessionFactory factory;


    public static Autor addAutor(String autorName){
        Session session = factory.openSession();
        Transaction tx = null;
        Autor result = null;
        try {
            tx = session.beginTransaction();
            result = new Autor(autorName);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static Persona addPersona(String dni, String nom, String telefon, String email){
        Session session = factory.openSession();
        Transaction tx = null;
        Persona result = null;
        try {
            tx = session.beginTransaction();
            result = new Persona(nom, telefon, dni, email);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static Llibre addLlibre(String isbn, String titol, String editorial, Integer anyPublicacio){
        Session session = factory.openSession();
        Transaction tx = null;
        Llibre result = null;
        try {
            tx = session.beginTransaction();
            result = new Llibre(isbn, titol, editorial, anyPublicacio);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static Biblioteca addBiblioteca(String nom, String ciutat, String adreca, String telefon, String email){
        Session session = factory.openSession();
        Transaction tx = null;
        Biblioteca result = null;
        try {
            tx = session.beginTransaction();
            result = new Biblioteca(nom, ciutat, adreca, telefon, email);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static Exemplar addExemplar(String codiBarres, Llibre llibre, Biblioteca biblioteca){
        Session session = factory.openSession();
        Transaction tx = null;
        Exemplar result = null;
        try {
            tx = session.beginTransaction();
            result = new Exemplar(codiBarres, llibre, biblioteca);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static Prestec addPrestec(Exemplar exemplar, Persona persona, LocalDate dataPrimera, LocalDate dataSegona){
        Session session = factory.openSession();
        Transaction tx = null;
        Prestec result = null;
        try {
            tx = session.beginTransaction();
            result = new Prestec(exemplar, persona, dataPrimera, dataSegona);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }

    public static void updateAutor(long autorId, String nom, Set<Llibre> llibres){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Autor obj = (Autor) session.get(Autor.class, autorId); 

            if (obj == null) {
                throw new RuntimeException("Cart not found with id: " + autorId);
            }

            obj.setNom(nom);
            obj.setLlibres(llibres);;

            session.merge(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }

    public static void registrarRetornPrestec(long prestecId, LocalDate dataEntrega){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Prestec obj = (Prestec) session.get(Prestec.class, prestecId); 

            if (obj == null) {
                throw new RuntimeException("Cart not found with id: " + prestecId);
            }

            obj.setActiu(false);
            obj.setDataRetornReal(dataEntrega);

            session.merge(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }
    public static List<Llibre> findLlibresAmbAutors() {
        List<Llibre> result = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "SELECT DISTINCT l FROM Llibre l JOIN FETCH l.autors";
            Query<Llibre> query = session.createQuery(hql, Llibre.class);
            result = query.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    
    public static List<Object[]> findLlibresEnPrestec() {
        List<Object[]> result = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "SELECT p.exemplar.llibre.titol, p.persona.nom, p.dataPrestec, p.dataRetornPrevista " +
                         "FROM Prestec p WHERE p.actiu = true";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            result = query.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
    
    public static List<Object[]> findLlibresAmbBiblioteques() {
        List<Object[]> result = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "SELECT e.llibre.titol, e.biblioteca.nom FROM Exemplar e";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            result = query.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    } 
    
    public static String formatMultipleResult(List<Object[]> result) {
        StringBuilder sb = new StringBuilder();
        for (Object[] row : result) {
            for (Object column : row) {
                sb.append(column != null ? column.toString() : "null").append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
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
