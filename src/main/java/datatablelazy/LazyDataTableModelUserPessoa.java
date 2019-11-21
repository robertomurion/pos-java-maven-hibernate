package datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import dao.DaoUsuarioPessoa;
import model.UsuarioPessoa;

public class LazyDataTableModelUserPessoa<T> extends LazyDataModel<UsuarioPessoa> {

	private static final long serialVersionUID = 1L;
	
	private DaoUsuarioPessoa<UsuarioPessoa> daoUsuarioPessoa = new DaoUsuarioPessoa<UsuarioPessoa>();
	public List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private String sql = " from UsuarioPessoa ";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters){

		list = daoUsuarioPessoa.getEntityManager().createQuery(getSql() +" ORDER BY id ASC").
				setFirstResult(first).
				setMaxResults(pageSize).getResultList();
		sql = " from UsuarioPessoa ";
		
		setPageSize(pageSize);
		
		Integer qtdRegistro = Integer.parseInt(daoUsuarioPessoa.getEntityManager().createQuery("select count(1) " + getSql()).getSingleResult().toString());
		setRowCount(qtdRegistro);
		return list;
	}
	
	public String getSql() {
		return sql;
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	
	public void pesquisarPessoas(String campoPesquisa) {
		sql += " where nome like '%"+campoPesquisa+"%'"; 
	}
	
}
