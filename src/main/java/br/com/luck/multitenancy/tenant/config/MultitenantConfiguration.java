package br.com.luck.multitenancy.tenant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.luck.multitenancy.tenant.sistema.cliente.ClienteEntity;

@Configuration
public class MultitenantConfiguration {

    @Autowired
    private DataSourceProperties properties;
    
    
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
    
    
    @Bean
    public DataSource dataSource()  {
        
        Map<Object,Object> resolvedDataSources = new HashMap<>();
        DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(this.getClass().getClassLoader());

        
        DataSource dataSourcePadrao = 
        dataSourceBuilder.driverClassName(defaultClassName)
                .url(defaultUrl+"/default_db?autoReconnect=true&useSSL=false&serverTimezone=UTC")
                .username(defaultUserName)
                .password(defaultPassword).build();
        resolvedDataSources.put("DEFAULT", dataSourcePadrao);
        
        System.out.println("***********  CONECTAR NO DEFAULT **********");
        try {
        	Connection con = DriverManager.getConnection(defaultUrl+"?autoReconnect=true&useSSL=false&serverTimezone=UTC",defaultUserName,defaultPassword);
			Statement stm = con.createStatement();
			stm.execute("Create database if not exists default_db");
			StringBuilder str = new StringBuilder();
			str.append("Create table if not exists default_db.Cliente (");
			str.append("id bigInt(20) primary key");
			str.append(",razaoSocial VarChar(50) ");
			str.append(",tenant varchar(20) ) ");
			stm.execute(str.toString());
		} catch (Exception e) {
			System.out.println("ERRO DE CONEXAO, NÃO CONSEGUIU CONECTAR NO SERVIDOR PARA OBTER O DEFAULT_DB");
			return null;
		}

    
        System.out.println("************** OBTENDO LISTA DE TENANTS A PARTIR DO DEFAULT_DB.CLIENTE");
        List<ClienteEntity> clientes = new ArrayList<>();
		try {
			Connection con = DriverManager.getConnection(defaultUrl+"/default_db?autoReconnect=true&useSSL=false&serverTimezone=UTC",defaultUserName,defaultPassword);
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("select id, razaoSocial, tenant from cliente;");
			while (rs.next()) {

				ClienteEntity cli = new ClienteEntity();
				cli.setId(rs.getLong("id"));
				cli.setRazaoSocial(rs.getString("razaoSocial"));
				cli.setTenant(rs.getString("tenant"));
				clientes.add(cli);
			}			
		} catch (SQLException e) {
			System.out.println("********** ERRO OBTER LISTA DE TENANTS A PARTIR DO DEFAULT_DB.CLIENTE   ");
		}

        
        clientes.forEach(cliente -> {
        	String banco = cliente.getTenant()+"_db";
            String tenantId = cliente.getTenant();
            dataSourceBuilder.driverClassName(tenantClassName)
                    .url(tenantUrl+"/"+banco+"?autoReconnect=true&useSSL=false&serverTimezone=UTC")
                    .username(tenantUserName)
                    .password(tenantPassword);

            if(properties.getType() != null) {
                dataSourceBuilder.type(properties.getType());
            }

            resolvedDataSources.put(tenantId, dataSourceBuilder.build());
        	
        });

        MultitenantDataSource multitenantdataSource = new MultitenantDataSource();
        multitenantdataSource.setDefaultTargetDataSource(dataSourcePadrao);
        multitenantdataSource.setTargetDataSources(resolvedDataSources);
        multitenantdataSource.afterPropertiesSet();
        System.out.println("*********** DATASOURCES SETADOS ");
        return multitenantdataSource;
    }

  
}
