package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumDto;
import rs.raf.demo.repositories.ErrorRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.utils.JwtUtil;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacuumService {

    private TaskScheduler taskScheduler;
    private VacuumRepository vacuumRepository;
    private AsyncVacuumService asyncVacuumService;
    private ErrorRepository errorRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public VacuumService(ErrorRepository errorRepository, AsyncVacuumService asyncVacuumService, VacuumRepository vacuumRepository, TaskScheduler taskScheduler, JwtUtil jwtUtil, UserRepository userRepository) {
        this.vacuumRepository = vacuumRepository;
        this.taskScheduler = taskScheduler;
        this.jwtUtil = jwtUtil;
        this.asyncVacuumService = asyncVacuumService;
        this.errorRepository = errorRepository;
        this.userRepository = userRepository;
    }

    public Collection<Vacuum> getAllVacuums(String auth){
        String userName = jwtUtil.extractUsername(auth.substring(7));
        System.out.println(userName + " USAOOOO U GET ALL");
        User usr = userRepository.findByUsername(userName);
        if(usr == null)
            return null;

        System.out.println(vacuumRepository.findAllByUser(usr));
        return vacuumRepository.findAllByUser(usr);
    }
    public Vacuum createVacuum(String authorization, VacuumDto vacuum){
        String jwt = authorization.substring(7);
        String userName = jwtUtil.extractUsername(jwt);
        System.out.println(userName);
        if(userName == null){
            return null;
        }
        User user = userRepository.findByUsername(userName);
        if(!user.isCan_add_vacuum()){
            return null;
        }
        Vacuum vc = new Vacuum();
        vc.setActive(true);
        vc.setName(vacuum.getName());
        vc.setBlocked(false);
        vc.setCycleNumber(0);
        vc.setDateCreated(LocalDate.now());
        vc.setStatus(Status.STOP); //ON, OFF, DISCHARGING
        vc.setUser(user);
        vacuumRepository.save(vc);
        return vc;
    }
    @Transactional
    public ResponseEntity<?> deleteVacuum(String authorization, Long id){
        String jwt = authorization.substring(7);
        Optional<Vacuum> vacuum = vacuumRepository.findById(id);
        String userName = jwtUtil.extractUsername(jwt);
        if(userName == null){
            return null;
        }
        User user = userRepository.findByUsername(userName);
        if(!vacuum.get().getUser().getUsername().equals(user.getUsername())){
            return null;
        }
        if(!user.isCan_remove_vacuum()){
            return null;
        }
        if(vacuum.get().getStatus() != Status.STOP){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        vacuum.get().setActive(false);
        vacuumRepository.save(vacuum.get());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public List<Vacuum> searchVacuums(String authorization, String name, List<String> status, LocalDate dateFrom, LocalDate dateTo){
        // ako su svi null onda po svim
        List<Status> statusEnums = status.stream()
                .map(Status::valueOf)
                .collect(Collectors.toList());
        return vacuumRepository.findByParameters(name, statusEnums, dateFrom, dateTo);
    }

    public ResponseEntity<?> startVacuum(String authorization, Long id) throws InterruptedException {
        Optional<Vacuum> vacuum = vacuumRepository.findById(id);
        if(vacuum.isPresent()){

            asyncVacuumService.asyncStartVacuum(vacuum.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<?> stopVacuum(String authorization, Long id) throws InterruptedException {

        Optional<Vacuum> vacuum = vacuumRepository.findById(id);
        if(vacuum.isPresent()){

            asyncVacuumService.asyncStopVacuum(vacuum.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<?> dischargeVacuum(String authorization, Long id) throws InterruptedException {

        Optional<Vacuum> vacuum = vacuumRepository.findById(id);
        if(vacuum.isPresent()){

            asyncVacuumService.asyncDischargeVacuum(vacuum.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
