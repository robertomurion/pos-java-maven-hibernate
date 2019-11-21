package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "telefone_seq",
sequenceName = "telefone_seq", initialValue = 1, allocationSize = 1)
public class TelefoneUser implements Serializable {

	private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telefone_seq")
		private Long id;
		@Column(nullable = false)
		private String tipo;
		@Column(nullable = false)
		private String numero;
		@ManyToOne(optional = false, fetch = FetchType.EAGER)
		UsuarioPessoa usuarioPessoa;
		
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public UsuarioPessoa getUsuarioPessoa() {
			return usuarioPessoa;
		}
		public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
			this.usuarioPessoa = usuarioPessoa;
		}
		
		
		
}
