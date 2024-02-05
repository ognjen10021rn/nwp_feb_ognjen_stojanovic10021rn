package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.model.VacuumDto;
import rs.raf.demo.services.UserService;
import rs.raf.demo.services.VacuumService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/vacuum")
@CrossOrigin
public class VacuumController {

    private final VacuumService vacuumService;

    @Autowired
    public VacuumController(VacuumService vacuumService) {
        this.vacuumService = vacuumService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createVacuum(@RequestHeader(name = "Authorization")String authorization, @RequestBody VacuumDto vacuum) {
        Vacuum v = this.vacuumService.createVacuum(authorization, vacuum);
        if(v == null){
            return new ResponseEntity<>( "Greska",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/allVacuums", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Vacuum> all(@Valid @RequestHeader(name = "Authorization") String auth) {
        return this.vacuumService.getAllVacuums(auth);
    }
    @GetMapping(value = "/start/{id}")
    public ResponseEntity<?> startVacuum(@RequestHeader(name = "Authorization")String authorization, @PathVariable Long id) throws InterruptedException {
        System.out.println("USAOOOOO U START");
        return this.vacuumService.startVacuum(authorization, id);
    }
    @GetMapping(value = "/stop/{id}")
    public ResponseEntity<?> stopVacuum(@RequestHeader(name = "Authorization")String authorization, @PathVariable Long id) throws InterruptedException {
        return this.vacuumService.stopVacuum(authorization, id);
    }
    @GetMapping(value = "/discharge/{id}")
    public ResponseEntity<?> dischargeVacuum(@RequestHeader(name = "Authorization")String authorization, @PathVariable Long id) throws InterruptedException {
        return this.vacuumService.dischargeVacuum(authorization, id);
    }


}
