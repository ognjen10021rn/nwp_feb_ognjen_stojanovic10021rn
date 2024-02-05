package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.ErrorRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.utils.JwtUtil;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsyncVacuumService {

    private TaskScheduler taskScheduler;
    private VacuumRepository vacuumRepository;
    private ErrorRepository errorRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public AsyncVacuumService(ErrorRepository errorRepository ,VacuumRepository vacuumRepository, TaskScheduler taskScheduler, JwtUtil jwtUtil, UserRepository userRepository) {
        this.vacuumRepository = vacuumRepository;
        this.taskScheduler = taskScheduler;
        this.jwtUtil = jwtUtil;
        this.errorRepository = errorRepository;
    }

    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void asyncStartVacuum(Vacuum vacuum) throws InterruptedException {
        if(vacuum.isBlocked()){
            return;
        }
        if(!vacuum.isActive()){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(LocalDate.now());
            errorMessage.setMessage("Vacuum is not active!");
            errorMessage.setVacuum(vacuum);
            errorRepository.save(errorMessage);
        }

        if(vacuum.getStatus() == Status.STOP){

            vacuum.setBlocked(true);
            this.vacuumRepository.save(vacuum);
            this.vacuumRepository.flush();
            Thread.sleep(5000);
            Optional<Vacuum> vc = vacuumRepository.findById(vacuum.getId());
            vc.get().setStatus(Status.START);
            vc.get().setBlocked(false);
            this.vacuumRepository.save(vc.get());
        }
        // postaviti error repozitorijume
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Async
    public void asyncStopVacuum(Vacuum vacuum) throws InterruptedException {

        if(vacuum.isBlocked()){
            return;
        }
        if(!vacuum.isActive()){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(LocalDate.now());
            errorMessage.setMessage("Vacuum is not active!");
            errorMessage.setVacuum(vacuum);
            errorRepository.save(errorMessage);
        }

        if(vacuum.getStatus() == Status.START){
            vacuum.setBlocked(true);
            this.vacuumRepository.save(vacuum);
            this.vacuumRepository.flush();
            Thread.sleep(4000);
            Optional<Vacuum> vacuum1 = vacuumRepository.findById(vacuum.getId());
            vacuum1.get().setStatus(Status.STOP);
            vacuum1.get().setBlocked(false);
            this.vacuumRepository.save(vacuum1.get());
        }
    }
    @Transactional
    @Async
    public void asyncDischargeVacuum(Vacuum vacuum) throws InterruptedException {

        if(vacuum.isBlocked()){
            return;
        }
        if(!vacuum.isActive()){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setDate(LocalDate.now());
            errorMessage.setMessage("Vacuum is not active!");
            errorMessage.setVacuum(vacuum);
            errorRepository.save(errorMessage);
        }

        if(vacuum.getStatus() == Status.STOP){
            vacuum.setBlocked(true);
            vacuumRepository.save(vacuum);
            vacuumRepository.flush();
            Thread.sleep(3000);
            Optional<Vacuum> vc = vacuumRepository.findById(vacuum.getId());
            vc.get().setStatus(Status.DISCHARGE);
            vacuumRepository.save(vc.get());
            vacuumRepository.flush();
            Thread.sleep(3000);
            Optional<Vacuum> vc2 = vacuumRepository.findById(vacuum.getId());
            vc2.get().setBlocked(false);
            vc2.get().setStatus(Status.STOP);
            vacuumRepository.save(vc2.get());
            vacuumRepository.flush();
        }
    }

}
