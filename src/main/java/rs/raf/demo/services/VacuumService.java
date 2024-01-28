package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumRepository;
import rs.raf.demo.utils.JwtUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacuumService {

    private TaskScheduler taskScheduler;
    private VacuumRepository vacuumRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, TaskScheduler taskScheduler, JwtUtil jwtUtil, UserRepository userRepository) {
        this.vacuumRepository = vacuumRepository;
        this.taskScheduler = taskScheduler;
        this.jwtUtil = jwtUtil;
    }

    public Vacuum createVacuum(String authorization, Vacuum vacuum){
        String jwt = authorization.substring(7);
        String userName = jwtUtil.extractUsername(jwt);
        if(userName == null){
            return null;
        }
        User user = userRepository.findByUsername(userName);
        if(!user.isCan_add_vacuum()){
            return null;
        }
        vacuumRepository.save(vacuum);
        return vacuum;
    }
    public Vacuum deleteVacuum(String authorization, Vacuum vacuum){
        String jwt = authorization.substring(7);
        String userName = jwtUtil.extractUsername(jwt);
        if(userName == null){
            return null;
        }
        User user = userRepository.findByUsername(userName);
        if(!vacuum.getUser().getUsername().equals(user.getUsername())){
            return null;
        }
        if(!user.isCan_remove_vacuums()){
            return null;
        }
        vacuumRepository.delete(vacuum);
        return vacuum;
    }

    public List<Vacuum> searchVacuums(String authorization, String name, List<String> status, Date dateFrom, Date dateTo){
        // ako su svi null onda po svim
        List<Status> statusEnums = status.stream()
                .map(Status::valueOf)
                .collect(Collectors.toList());
        return vacuumRepository.findByParameters(name, statusEnums, dateFrom, dateTo);
    }


}
