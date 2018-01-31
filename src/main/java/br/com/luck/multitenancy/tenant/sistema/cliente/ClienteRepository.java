package br.com.luck.multitenancy.tenant.sistema.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
	
}
