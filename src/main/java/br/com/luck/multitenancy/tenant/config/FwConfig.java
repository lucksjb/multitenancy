package br.com.luck.multitenancy.tenant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import br.com.luck.multitenancy.tenant.sistema.cliente.ClienteService;

@Configuration
public class FwConfig implements InitializingBean{
	@Autowired
	private ClienteService clienteService;

	// DEFINE ACESSO AO BD (DEFAULT) QUE CONTROLA OS TENANTS
	@Value("${luck.default.datasource.url}")
	private String defaultUrl;

	@Value("${luck.default.datasource.username}")
	private String defaultUserName;

	@Value("${luck.default.datasource.password}")
	private String defaultPassword;

	@Value("${luck.default.datasource.driverClassName}")
	private String defaultClassName;

	// DEFINE OS TENANTS
	@Value("${luck.tenant.datasource.url}")
	private String tenantUrl;

	@Value("${luck.tenant.datasource.username}")
	private String tenantUserName;

	@Value("${luck.tenant.datasource.password}")
	private String tenantPassword;

	@Value("${luck.tenant.datasource.driverClassName}")
	private String tenantClassName;

	@Override
	public void afterPropertiesSet() throws Exception {
		execute();
	}

	public void execute() {
		System.out.println("*************** MIGRANDO DEFAULT ************");
		migraFlyway("default");

		System.out.println("Executa Migration dos clientes FWCONFIG ");
		clienteService.listaTodos().forEach(cliente -> {
			System.out.println(cliente.toString());
		});

		System.out.println("chegou aqui sem listar ");
		clienteService.listaTodos().forEach(cliente -> {
			System.out.println("MIGRANDO " + cliente.getTenant());
			criaBancoIfNotExists(cliente.getTenant());
			migraFlyway(cliente.getTenant());

		});
	}

	private void migraFlyway(String banco) {

		String url = "";
		Flyway flyway = new Flyway();
		flyway.setBaselineOnMigrate(true);
		if (banco.equals("default")) {
			url = defaultUrl + "/" + banco + "_db?autoReconnect=true&useSSL=false&serverTimezone=UTC";
			flyway.setDataSource(url, defaultUserName, defaultPassword);
			flyway.setLocations("classpath:db/default");
		} else {
			url = tenantUrl + "/" + banco + "_db?autoReconnect=true&useSSL=false&serverTimezone=UTC";
			flyway.setDataSource(url, tenantUserName, tenantPassword);
			flyway.setLocations("classpath:db/migration");
		}
		flyway.migrate();
	}

	private void criaBancoIfNotExists(String banco) {
		String url = tenantUrl + "?autoReconnect=true&useSSL=false&serverTimezone=UTC";
		try {

			Connection conexao = DriverManager.getConnection(url, tenantUserName, tenantPassword);
			conexao.prepareStatement("Create database if not exists " + banco + "_db;").execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


}
