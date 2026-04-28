package com.rbank.eureka_server;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(EurekaServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

	@Bean
	public HealthIndicator customEurekaHealthIndicator() {
		return () -> Status.UP.equals(Status.UP) ?
				new org.springframework.boot.actuate.health.Health.Builder().up().build() :
				new org.springframework.boot.actuate.health.Health.Builder().down().build();
	}

	@EventListener
	public void handleInstanceRegisteredEvent(EurekaInstanceRegisteredEvent event) {
		LOGGER.info("Instance registered: {}", event.getInstanceInfo().getAppName());
	}

	@EventListener
	public void handleInstanceRenewedEvent(EurekaInstanceRenewedEvent event) {
		LOGGER.info("Instance renewed: {}", event.getInstanceInfo().getAppName());
	}

	@EventListener
	public void handleInstanceCanceledEvent(EurekaInstanceCanceledEvent event) {
		LOGGER.warn("Instance canceled: {}", event.getAppName());
	}
}
