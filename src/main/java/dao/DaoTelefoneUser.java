package dao;

import java.util.List;

import javax.persistence.EntityManager;

import model.TelefoneUser;
import posjavamavenhibernate.HibernateUtil;

public class DaoTelefoneUser<E> extends DaoGeneric<TelefoneUser>{
	
	@SuppressWarnings("unchecked")
	public List<E >listaTelefone(Class<E> entidade, Long id){
		EntityManager entityManager = HibernateUtil.getEntityManager();
		List<E> lista=  entityManager.createQuery(" SELECT a FROM  "  + entidade.getName() + "  a where a.usuarioPessoa.id = " + id + " ORDER BY a.id ASC").getResultList();
		return lista;
	}
}
