package dey.sayantan.property.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dey.sayantan.property.management.repositry.ComplainRepository;
import dey.sayantan.property.management.repositry.FeedbackRepository;
import dey.sayantan.property.management.repositry.PaymentRepository;
import dey.sayantan.property.management.repositry.PropertyOccupancyRepository;
import dey.sayantan.property.management.repositry.PropertyRepository;
import dey.sayantan.property.management.repositry.ProprietorRepository;
import dey.sayantan.property.management.repositry.TenantRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = { TenantRepository.class, ProprietorRepository.class,
		FeedbackRepository.class, ComplainRepository.class, PaymentRepository.class, PropertyRepository.class,
		PropertyOccupancyRepository.class })
@EntityScan
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}
