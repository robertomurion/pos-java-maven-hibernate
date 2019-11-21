package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavenhibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}
	
	public E updateMerge(E entidade) {   //salva ou atualiza
		EntityManager entityManager = HibernateUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		
		return entidadeSalva;
	}
	
	
	@SuppressWarnings("unchecked")
	public E pesquisar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e =  (E) entityManager.find(entidade.getClass(), id);
		return e;
	}
	
	public E pesquisar(Long id, Class<E> entidade){
		E e = (E) entityManager.find(entidade, id);
		return e;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<E >lista(Class<E> entidade){
		EntityManager entityManager = HibernateUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		List<E> lista=  entityManager.createQuery(" SELECT a FROM  "  + entidade.getName() + " a ORDER BY a.id ASC").getResultList();
		transaction.commit();
		return lista;
	}
	public void deletar( E  entidade){
		Object id =HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.createNativeQuery("delete from  " + entidade.getClass().getSimpleName().toLowerCase() + " where id = "  + id).executeUpdate();
		transaction.commit();
		}
	
	public void deletarPorId( E  entidade, Long id){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.createNativeQuery("delete from  " + entidade.getClass().getSimpleName().toLowerCase() + " where id = "  + id).executeUpdate();
		transaction.commit();
		}
}

