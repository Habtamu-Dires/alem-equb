package com.ekub;

import com.ekub.config.AllowedOriginsConfig;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
@EnableConfigurationProperties
@EnableAsync
public class AlemEQubApplication implements CommandLineRunner {

	private final AllowedOriginsConfig allowedOriginsConfig;

	public AlemEQubApplication(AllowedOriginsConfig allowedOriginsConfig) {
		this.allowedOriginsConfig = allowedOriginsConfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(AlemEQubApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		allowedOriginsConfig.getOrigins().forEach(System.out::println);
	}
}
