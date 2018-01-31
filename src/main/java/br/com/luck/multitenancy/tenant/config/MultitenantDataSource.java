package br.com.luck.multitenancy.tenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant() == null ?  "DEFAULT": TenantContext.getCurrentTenant();
    }
}
