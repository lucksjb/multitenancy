package br.com.luck.multitenancy.tenant.sistema.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;
	
	public ClienteEntity buscaPorId(Long id) {
		return clienteRepository.findOne(id);
	}

	public List<ClienteEntity> listaTodos(){
		return clienteRepository.findAll();
	}
	
	public void novo(ClienteEntity clienteEntity) {
		clienteRepository.saveAndFlush(clienteEntity);
	}

	public void apaga(long id) {
		ClienteEntity cliente =  clienteRepository.findOne(id);
		if(cliente != null) {
			clienteRepository.delete(cliente);
		}
		
	}
}
