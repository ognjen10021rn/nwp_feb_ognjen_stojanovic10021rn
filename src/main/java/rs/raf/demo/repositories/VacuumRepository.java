package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;

import java.util.Date;
import java.util.List;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {
    public Vacuum findByUser(User user);

    @Query("SELECT v FROM Vacuum v WHERE "
            + "(:name IS NULL OR v.name = :name) "
            + "AND (:status IS NULL OR v.status IN :status) "
            + "AND (:dateFrom IS NULL OR v.dateFrom = :dateFrom) "
            + "AND (:dateTo IS NULL OR v.dateTo = :dateTo)")
    List<Vacuum> findByParameters(
            @Param("name") String name,
            @Param("status") List<Status> status,
            @Param("dateFrom") Date dateFrom,
            @Param("dateTo") Date dateTo
    );

}
