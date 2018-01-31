package br.com.luck.multitenancy.tenant.sistema.usuario;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.luck.multitenancy.tenant.sistema.cliente.ClienteEntity;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

	private Long id;
	private String login;
	private String senha;
	private ClienteEntity cliente;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@ManyToOne
	@JoinColumn(name = "cliente_id", referencedColumnName = "id")
	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

}
