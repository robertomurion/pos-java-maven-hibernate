package posjavamavenhibernate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testeHibernateUtil() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();

		UsuarioPessoa pessoa = new UsuarioPessoa();

		pessoa.setIdade(45);
		pessoa.setLogin("renato");
		pessoa.setNome("Renato");
		pessoa.setSenha("123");
		pessoa.setSobrenome("Penna");

		daoGeneric.salvar(pessoa);
	}

	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(2L);

		pessoa = daoGeneric.pesquisar(pessoa);

		System.out.println(pessoa);
	}

	@Test
	public void testeBuscar2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L, UsuarioPessoa.class);

		System.out.println(pessoa);
	}

	@Test
	public void testeUpdate() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L, UsuarioPessoa.class);
		pessoa.setNome("Roberto");
		pessoa = daoGeneric.updateMerge(pessoa);
		System.out.println(pessoa);
	}

	@Test
	public void testeApagar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(8L);

		pessoa = daoGeneric.pesquisar(pessoa);

		daoGeneric.deletar(pessoa);

		// System.out.println(pessoa);
	}

	@Test
	public void gerarLista() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = new ArrayList<>();
		pessoas = daoGeneric.lista(UsuarioPessoa.class);

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("--------------------------------------------------------------------------");
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testeQueryList() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa").getResultList();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testeQueryListMaxResult() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa order by id")
				.setMaxResults(4).getResultList();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");

		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testeQueryListParameter() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = daoGeneric.getEntityManager()
				.createQuery(" from UsuarioPessoa where nome = :nome or sobrenome =:sobrenome")
				.setParameter("nome", "Roberto").setParameter("sobrenome", "Penna").getResultList();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");
		}
	}

	@Test
	public void testeQuerysomaIdade() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		Long somaIdade = (Long) daoGeneric.getEntityManager().createQuery("select sum(u.idade) from UsuarioPessoa u")
				.getSingleResult();
		System.out.println("Soma de todas as idades do banco é " + somaIdade);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testeNamedQuery() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findAll")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testeNamedQuery2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> pessoas = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "Roberto").getResultList();

		for (UsuarioPessoa usuarioPessoa : pessoas) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testeGravaTelefone() {

		DaoGeneric daoGeneric = new DaoGeneric();
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(1L, UsuarioPessoa.class);

		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("comercial");
		telefoneUser.setNumero("32558399");
		telefoneUser.setUsuarioPessoa(pessoa);

		daoGeneric.salvar(telefoneUser);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testeConsultaTelefone() {

		DaoGeneric daoGeneric = new DaoGeneric();
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(4L, UsuarioPessoa.class);

		for (TelefoneUser telefoneUser : pessoa.getTelefonesUser()) {
			System.out.println(telefoneUser.getUsuarioPessoa().getNome());
			System.out.println(telefoneUser.getTipo());
			System.out.println(telefoneUser.getNumero());
			System.out.println("------------------------------------------------------------------------------");
		}
	}
	
	@Test
	public void testCurios() {
		
		TelefoneUser pessoa1 = new TelefoneUser();
		TelefoneUser pessoa2 = pessoa1;
		System.out.println(pessoa1);
		System.out.println(pessoa2);
	}
}
