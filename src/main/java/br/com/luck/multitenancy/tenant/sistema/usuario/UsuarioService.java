package br.com.luck.multitenancy.tenant.sistema.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UsuarioEntity buscaPorId(Long id) {
		UsuarioEntity usuario = usuarioRepository.findOne(id);
		return usuario;
	}

	public void novo(UsuarioEntity usuario) {
		usuarioRepository.save(usuario);
	}
}
