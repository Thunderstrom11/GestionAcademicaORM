package java.edu.gestionacademicaorm.config;

import java.edu.gestionacademicaorm.model.Estudiante;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            cargarCredenciales(p);

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

    private static void cargarCredenciales(Properties p) throws IOException {
        // .env vive en la raiz del proyecto, al mismo nivel que pom.xml.
        Path env = Path.of(".env");
        if (!Files.exists(env)) {
            throw new IllegalStateException("Falta .env con las credenciales de PostgreSQL");
        }

        Properties credenciales = new Properties();
        try (var lineas = Files.newBufferedReader(env)) {
            String linea;
            while ((linea = lineas.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#") || linea.startsWith("//")) {
                    continue;
                }
                int separador = linea.indexOf('=');
                if (separador > 0) {
                    credenciales.setProperty(
                            linea.substring(0, separador).trim(),
                            linea.substring(separador + 1).trim());
                }
            }
        }

        p.setProperty("hibernate.connection.driver_class", requerido(credenciales, "DB_DRIVER"));
        p.setProperty("hibernate.connection.url", requerido(credenciales, "DB_URL"));
        p.setProperty("hibernate.connection.username", requerido(credenciales, "DB_USER"));
        p.setProperty("hibernate.connection.password", requerido(credenciales, "DB_PASSWORD"));
    }

    private static String requerido(Properties p, String clave) {
        String valor = p.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta " + clave + " en .env");
        }
        return valor;
    }

    public static SessionFactory getSessionFactory() {
        return FACTORY;
    }
}
