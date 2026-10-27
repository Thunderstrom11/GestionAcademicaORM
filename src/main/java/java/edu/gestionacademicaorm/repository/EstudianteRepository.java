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

    public boolean existeCorreo(String correo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long total = session.createQuery(
                            "SELECT COUNT(e) FROM Estudiante e WHERE LOWER(e.correo) = LOWER(:correo)",
                            Long.class)
                    .setParameter("correo", correo)
                    .getSingleResult();

            return total != null && total > 0;
        }
    }

    public List<Estudiante> listarPorCarrera(String carrera) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Estudiante e WHERE e.carrera = :carrera ORDER BY e.apellidos",
                            Estudiante.class)
                    .setParameter("carrera", carrera)
                    .getResultList();
        }
    }
}

