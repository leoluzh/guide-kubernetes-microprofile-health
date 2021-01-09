package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class InventoryLivenessCheck implements HealthCheck {

	public static final String READINESS_CHECK = InventoryResource.class.getSimpleName() + " Readiness Check" ;
	
	@Inject
	@ConfigProperty( name = "SYS_APP_HOSTNAME")
	private String hostname;
	
	@Inject
	@ConfigProperty( name = "SYS_APP_PORT" )
	private String port;
	
	@Override
	public HealthCheckResponse call() {
		if( isSystemServiceReachable() ) {
			return HealthCheckResponse.up(READINESS_CHECK);
		}else {
			return HealthCheckResponse.down(READINESS_CHECK);
		}
	}

	protected boolean isSystemServiceReachable() {
		try {
			Client client = ClientBuilder.newClient();
			client.target( 
					StringUtils.join("http://",hostname , ":" ,"9080","/system/properties") )
				.request()
				.post(null);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
}
