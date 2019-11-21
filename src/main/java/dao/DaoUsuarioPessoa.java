package dao;

import java.util.List;

import javax.persistence.EntityManager;

import model.UsuarioPessoa;
import posjavamavenhibernate.HibernateUtil;

public class DaoUsuarioPessoa<E>  extends DaoGeneric<UsuarioPessoa>{
	
	public void removerPessLancl(UsuarioPessoa usuariopessoa ) {
		String sqlDeleteLancamento = "delete from telefoneuser where usuariopessoa_id =  " + usuariopessoa.getId();
		String sqlDeleteEmail = "delete from emailuser where usuariopessoa_id =  " + usuariopessoa.getId();
		
		getEntityManager().getTransaction().begin();
		getEntityManager().createNativeQuery(sqlDeleteLancamento).executeUpdate();
		
		getEntityManager().createNativeQuery(sqlDeleteEmail).executeUpdate();
		getEntityManager().getTransaction().commit();
		
		super.deletar(usuariopessoa);
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<E> pesquisarPessoas(Class<E> entidade, String nome){
		EntityManager entityManager = HibernateUtil.getEntityManager();
		List<E> lista=  entityManager.createQuery("SELECT a FROM  "  + entidade.getName()+ " as  a where lower(a.nome) like '%" + nome.toLowerCase() + "%' ORDER BY a.id ASC").getResultList();
		return lista;
	}
}
