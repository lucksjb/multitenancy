package br.com.luck.multitenancy.pessoa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pessoa")
public class PessoaEntity {

	private Long id;
	private String nome;

	public PessoaEntity() {
	
	}

	public PessoaEntity(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}


	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "PessoaEntity [id=" + id + ", nome=" + nome + "]";
	}

	
}
