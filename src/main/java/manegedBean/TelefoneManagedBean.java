package manegedBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefoneUser;
import dao.DaoUsuarioPessoa;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ViewScoped
@ManagedBean(name = "telefoneManagedBean")
public class TelefoneManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoTelefoneUser<TelefoneUser> daoTelefoneUser = new DaoTelefoneUser<TelefoneUser>();
	private DaoUsuarioPessoa<UsuarioPessoa> daoUsuarioPessoa = new DaoUsuarioPessoa<UsuarioPessoa>();
	private TelefoneUser telefoneUser = new TelefoneUser();
	private List<TelefoneUser> telefones = new ArrayList<TelefoneUser>();

	public List<TelefoneUser> getTelefones() {
		telefones = daoTelefoneUser.listaTelefone(TelefoneUser.class, usuarioPessoa.getId());
		return telefones;
	}

	public void setTelefones(List<TelefoneUser> telefones) {
		this.telefones = telefones;
	}

	public TelefoneUser getTelefoneUser() {
		return telefoneUser;
	}

	public void setTelefoneUser(TelefoneUser telefoneUser) {
		this.telefoneUser = telefoneUser;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	@PostConstruct
	public void init() {
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigopessoa");
		usuarioPessoa = daoUsuarioPessoa.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}

	public String salvar() {
		telefoneUser.setUsuarioPessoa(usuarioPessoa);
		if (telefoneUser.getId() == null) {
			mostrarMsg("Cadastrado com sucesso!");
		} else {
			mostrarMsg("Atualizado com sucesso!");
		}
		daoTelefoneUser.updateMerge(telefoneUser);
		telefones.add(telefoneUser);
		getTelefones();
		novo();
		return "";
	}

	public String novo() {
		telefoneUser = new TelefoneUser();
		return "";
	}

	public String apagar() {
		telefoneUser.setUsuarioPessoa(usuarioPessoa);
		daoTelefoneUser.deletar(telefoneUser);
		mostrarMsg("Excluído com sucesso!");
		getTelefones();
		novo();
		return "";
	}

	private void mostrarMsg(String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(mensagem);
		context.addMessage(null, message);
	}

}
