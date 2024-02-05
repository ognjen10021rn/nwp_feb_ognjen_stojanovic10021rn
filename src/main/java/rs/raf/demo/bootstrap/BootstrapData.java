package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.time.LocalDate;


@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VacuumRepository vacuumRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(VacuumRepository vacuumRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.vacuumRepository = vacuumRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("user1"));
        user1.setCan_add_vacuum(true);
        user1.setCan_start_vacuum(true);
        user1.setFirstName("usr");
        user1.setLastName("usrercic");

        this.userRepository.save(user1);

        Vacuum vacuum = new Vacuum();
        Vacuum vacuum2 = new Vacuum();
        vacuum.setActive(true);
        vacuum.setName("Vacuum1");
        vacuum.setBlocked(false);
        vacuum.setCycleNumber(0);
        vacuum.setDateCreated(LocalDate.now());
        vacuum.setStatus(Status.STOP); //ON, OFF, DISCHARGING
        vacuum.setUser(user1);

        this.vacuumRepository.save(vacuum);
        vacuum2.setActive(true);
        vacuum2.setName("Vacuum2");
        vacuum2.setBlocked(false);
        vacuum2.setCycleNumber(0);
        vacuum2.setDateCreated(LocalDate.now());
        vacuum2.setStatus(Status.STOP); //ON, OFF, DISCHARGING
        vacuum2.setUser(user1);

        this.vacuumRepository.save(vacuum2);
    }
}
