package java.edu.gestionacademicaorm.config;

import java.edu.gestionacademicaorm.model.Estudiante;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public final class HibernateUtil {

    // fabrica unica para toda la aplicacion. se crea al cargar la clase.
    private static final SessionFactory FACTORY = crear();

    // Constructor privado para los métodos estáticos.
    private HibernateUtil() {
    }

    private static SessionFactory crear() {
        // Lee hibernate.properties desde src/main/resources (classpath).
        try (var in = HibernateUtil.class.getClassLoader()
                .getResourceAsStream("hibernate.properties")) {

            if (in == null) {
                throw new IllegalStateException("Falta configuración");
            }

            Properties p = new Properties();
            p.load(in);

            // Carga las propiedades, registra la entidad y construye la fabrica.
            // Al construirla, Hibernate conecta y con hbm2ddl=update crea/actualiza la tabla.
            return new Configuration()
                    .addProperties(p)
                    .addAnnotatedClass(Estudiante.class)
                    .buildSessionFactory();

        } catch (Exception e) {
            // Si la inicializacion falla, la aplicacion no puede continuar.
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return FACTORY;
    }
}
