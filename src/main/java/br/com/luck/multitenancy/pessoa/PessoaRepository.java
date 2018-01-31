package br.com.luck.multitenancy.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<PessoaEntity, Long> {
	
}
