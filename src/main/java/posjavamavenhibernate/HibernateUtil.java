package posjavamavenhibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

	private static EntityManagerFactory factory = null;
	
	static {
		init();
	}
	
	private static void init() {

		try {
			if (factory == null) {
				factory = Persistence.createEntityManagerFactory("pos-java-maven-hibernate");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); /*provê a parte de persistencia*/
	}

	public static Object getPrimaryKey(Object entity) { // retorna chave primaria
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
		
		
	}
}
