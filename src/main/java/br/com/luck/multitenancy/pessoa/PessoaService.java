package br.com.luck.multitenancy.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public PessoaEntity buscaPorId(Long id) {
		PessoaEntity cli = pessoaRepository.findOne(id);
		return cli;
	}

	public void novo(PessoaEntity pessoa) {
		pessoaRepository.save(pessoa);
	}

	public List<PessoaEntity> listaTodos() {
		return pessoaRepository.findAll();
	}
}
