package java.edu.gestionacademicaorm.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.edu.gestionacademicaorm.config.HibernateUtil;
import java.edu.gestionacademicaorm.model.Estudiante;

import java.util.List;

public class EstudianteRepository {

    public void guardar(Estudiante estudiante) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(estudiante);

            tx.commit();

        } catch (RuntimeException e) {

            if (tx != null && tx.isActive()) tx.rollback();

            throw e;

        }

    }

    public List<Estudiante> listar() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Estudiante", Estudiante.class)

                    .getResultList();

        }

    }

}