package io.openliberty.guides.inventory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {

	public static final String READINESS_CHECK = InventoryResource.class.getSimpleName() + " Liveness Check" ;
	
	@Override
	public HealthCheckResponse call() {
		
		MemoryMXBean memoryBean =ManagementFactory.getMemoryMXBean();
		long memoryUsed = memoryBean.getHeapMemoryUsage().getUsed();
		long memoryMax = memoryBean.getHeapMemoryUsage().getMax();
		
		return HealthCheckResponse.named( READINESS_CHECK )
				.withData("memory used", memoryUsed )
				.withData("memory max", memoryMax )
				.state( memoryUsed < memoryMax * 0.9 ).build();
		
	}

}
