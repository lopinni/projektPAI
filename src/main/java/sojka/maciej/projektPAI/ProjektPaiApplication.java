package sojka.maciej.projektPAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import sojka.maciej.projektPAI.entities.User;
import sojka.maciej.projektPAI.entities.UserRepo;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ProjektPaiApplication {

	@Autowired
	private UserRepo repo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ProjektPaiApplication.class, args);
	}

	@PostConstruct
	public void init() {
		repo.save(new User(
				"admin@admin.com",
				passwordEncoder.encode("admin"),
				"admin",
				"admin",
				null,
				true
		));
	}

}
