package br.com.luck.multitenancy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.luck.multitenancy.pessoa.PessoaEntity;
import br.com.luck.multitenancy.pessoa.PessoaService;
import br.com.luck.multitenancy.tenant.config.FwConfig;
import br.com.luck.multitenancy.tenant.config.TenantContext;
import br.com.luck.multitenancy.tenant.sistema.cliente.ClienteEntity;
import br.com.luck.multitenancy.tenant.sistema.cliente.ClienteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoaServiceTest {
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ClienteService clienteService;
		
	@Test
	public void incluiCliente() throws Exception {
		TenantContext.setCurrentTenant("DEFAULT");
		
		clienteService.apaga(1L);
		clienteService.apaga(2L);
		clienteService.apaga(3L);
		clienteService.novo(new ClienteEntity(1L,"SUPERMERCADO BOM PRECO", "BOMPRECO"));
		clienteService.novo(new ClienteEntity(2L,"SUPERMERCADO UNIAO", "UNIAO"));
		clienteService.novo(new ClienteEntity(3L,"SUPERMERCADO PUDIM", "PUDIM"));
		clienteService.novo(new ClienteEntity(4L,"SUPERMERCADO NOBRE", "NOBRE"));
		assertEquals(clienteService.buscaPorId(1L).getTenant(), "BOMPRECO");
		assertEquals(clienteService.buscaPorId(2L).getTenant(), "UNIAO");
		assertEquals(clienteService.buscaPorId(3L).getTenant(), "PUDIM");
		assertEquals(clienteService.buscaPorId(4L).getTenant(), "NOBRE");

		System.out.println("Executa Migration dos clientes PESSOASERVICE ");
		clienteService.listaTodos().forEach(cliente ->{
			System.out.println(cliente.toString());
		});
	}
	
	@Test
	public void novasPessoas() throws Exception {
		
		TenantContext.setCurrentTenant("UNIAO");
		pessoaService.novo(new PessoaEntity(1L,"CLIENTE01_UNIAO"));
		pessoaService.novo(new PessoaEntity(2L,"CLIENTE02_UNIAO"));
		pessoaService.novo(new PessoaEntity(3L,"CLIENTE03_UNIAO"));
		assertEquals(pessoaService.buscaPorId(1L).getNome(), "CLIENTE01_UNIAO");
		assertEquals(pessoaService.buscaPorId(2L).getNome(), "CLIENTE02_UNIAO");
		assertEquals(pessoaService.buscaPorId(3L).getNome(), "CLIENTE03_UNIAO");
		
		TenantContext.setCurrentTenant("BOMPRECO");
		pessoaService.novo(new PessoaEntity(1L,"CLIENTE01_BOMPRECO"));
		pessoaService.novo(new PessoaEntity(2L,"CLIENTE02_BOMPRECO"));
		pessoaService.novo(new PessoaEntity(3L,"CLIENTE03_BOMPRECO"));
		assertEquals(pessoaService.buscaPorId(1L).getNome(), "CLIENTE01_BOMPRECO");
		assertEquals(pessoaService.buscaPorId(2L).getNome(), "CLIENTE02_BOMPRECO");
		assertEquals(pessoaService.buscaPorId(3L).getNome(), "CLIENTE03_BOMPRECO");
		
		TenantContext.setCurrentTenant("PUDIM");
		pessoaService.novo(new PessoaEntity(1L,"CLIENTE01_PUDIM"));
		pessoaService.novo(new PessoaEntity(2L,"CLIENTE02_PUDIM"));
		pessoaService.novo(new PessoaEntity(3L,"CLIENTE03_PUDIM"));
		assertEquals(pessoaService.buscaPorId(1L).getNome(), "CLIENTE01_PUDIM");
		assertEquals(pessoaService.buscaPorId(2L).getNome(), "CLIENTE02_PUDIM");
		assertEquals(pessoaService.buscaPorId(3L).getNome(), "CLIENTE03_PUDIM");
	}
	
	@Test
	public void listaPessoas() throws Exception {
		
		System.out.println("SETTING UNIAO TENANT");
		TenantContext.setCurrentTenant("UNIAO");
		pessoaService.listaTodos().forEach(pessoa -> {
			System.out.println(pessoa.toString());
		});

		System.out.println("SETTING PUDIM TENANT");
		TenantContext.setCurrentTenant("PUDIM");
		pessoaService.listaTodos().forEach(pessoa -> {
			System.out.println(pessoa.toString());
		});

		System.out.println("SETTING BOMPRECO TENANT");
		TenantContext.setCurrentTenant("BOMPRECO");
		pessoaService.listaTodos().forEach(pessoa -> {
			System.out.println(pessoa.toString());
		});
	}
}
