package manegedBean;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmailUser;
import dao.DaoUsuarioPessoa;
import datatablelazy.LazyDataTableModelUserPessoa;
import model.EmailUser;
import model.UsuarioPessoa;

@ViewScoped
@ManagedBean(name = "usuarioPessoaManagedBean")
public class UsuarioPessoaManagedBean {
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list = new LazyDataTableModelUserPessoa<UsuarioPessoa>();
	private DaoUsuarioPessoa<UsuarioPessoa> daoGeneric = new DaoUsuarioPessoa<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();
	private EmailUser emailUser = new EmailUser();
	private DaoEmailUser<EmailUser> daoEmailUser = new DaoEmailUser<EmailUser>();
	private String campoPesquisa;

	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public LazyDataTableModelUserPessoa<UsuarioPessoa> getPessoas() {
		montarGrafico();
		return list;
	}

	public String salvar() {
		if (usuarioPessoa.getId() == null) {
			mostrarMsg("Cadastrado com sucesso!");
		} else {
			if (usuarioPessoa.getSenha().isEmpty()) {
				UsuarioPessoa pessoaTemp = new UsuarioPessoa();
				pessoaTemp = daoGeneric.pesquisar(usuarioPessoa);
				usuarioPessoa.setSenha(pessoaTemp.getSenha());
			}
			mostrarMsg("Atualizado com sucesso!");
		}
		daoGeneric.updateMerge(usuarioPessoa);
		list.list.add(usuarioPessoa);
		carregarPessoas();
		novo();
		return "";
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	private void mostrarMsg(String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(mensagem);
		context.addMessage(null, message);
	}

	public String apagar() {
		mostrarMsg("Excluído com sucesso!");
		daoGeneric.removerPessLancl(getUsuarioPessoa());
		list.list.remove(getUsuarioPessoa());
		getPessoas();
		novo();
		carregarPessoas();
		return "";
	}

	@PostConstruct
	public void carregarPessoas() {
		list .load(0, 10,"id", SortOrder.ASCENDING, null);

		montarGrafico();
	}

	private void montarGrafico() {
		barChartModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries();
		userSalario.setLabel("Users");
		for (UsuarioPessoa usuarioPessoa : list.list) {
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
		}
		barChartModel.addSeries(userSalario);
		barChartModel.setTitle("Salario dos Usuarios");
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}

			UsuarioPessoa gsonAux = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);

			usuarioPessoa.setLogradouro(gsonAux.getLogradouro());
			usuarioPessoa.setComplemento(gsonAux.getComplemento());
			usuarioPessoa.setBairro(gsonAux.getBairro());
			usuarioPessoa.setLocalidade(gsonAux.getLocalidade());
			usuarioPessoa.setUf(gsonAux.getUf());
			usuarioPessoa.setUnidade(gsonAux.getUnidade());
			usuarioPessoa.setIbge(gsonAux.getIbge());
			usuarioPessoa.setGia(gsonAux.getGia());

		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o CEP");
		}
	}

	public void pesquisar() {
		list.pesquisarPessoas( campoPesquisa);
		montarGrafico();
	}

	public void upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
	}

	@SuppressWarnings("static-access")
	public void download() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");

		UsuarioPessoa usuarioPessoa = daoGeneric.pesquisar(Long.parseLong(fileDownloadId), UsuarioPessoa.class);
		byte[] imagem;
		try {
			 imagem = new Base64().decodeBase64(usuarioPessoa.getImagem().split("\\,")[1]);
		} catch (Exception e) {
			mostrarMsg("Imagem Indisponível!");
			return;
		}
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		try {
			response.getOutputStream().write(imagem);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void addEmail() {
		emailUser.setUsuarioPessoa(usuarioPessoa);
		emailUser = daoEmailUser.updateMerge(emailUser);
		usuarioPessoa.getEmailsUser().add(emailUser);
		emailUser = new EmailUser();
		usuarioPessoa = new UsuarioPessoa();
		mostrarMsg("Email salvo com sucesso");
	}

	public void removerEmail() {
		String codEmail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoemail");
		EmailUser emailUser = new EmailUser();
		emailUser.setId(Long.parseLong(codEmail));
		daoEmailUser.deletarPorId(emailUser, emailUser.getId());
		usuarioPessoa.getEmailsUser().remove(emailUser);
		mostrarMsg("Email excluído com sucesso");
	}
}
