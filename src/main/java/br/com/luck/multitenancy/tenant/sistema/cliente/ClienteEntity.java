package br.com.luck.multitenancy.tenant.sistema.cliente;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
public class ClienteEntity {

	private Long id;
	private String razaoSocial;
	private String tenant;

	public ClienteEntity() {
	
	}
	
	
	public ClienteEntity(Long id, String razaoSocial, String tenant) {
		this.id = id;
		this.razaoSocial = razaoSocial;
		this.tenant = tenant;
	}


	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}


	@Override
	public String toString() {
		return "ClienteEntity [id=" + id + ", razaoSocial=" + razaoSocial + ", tenant=" + tenant + "]";
	}

	
}
