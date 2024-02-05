package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorMessage, Long> {
    List<ErrorMessage> findAllByVacuum_User(User user);
}
