package rs.raf.demo.repositories;

import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {
    Collection<Vacuum> findAllByUser(User user);

    @Modifying
    @Transactional
    Optional<Vacuum> findById(Long id);

    @Override
    @Transactional
    @Modifying
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Vacuum> S save(S entity);

    @Modifying
    @Transactional
    @Query("UPDATE Vacuum v SET v.isBlocked = :blocked WHERE v.id = :id")
    void setBlockedForVacuum(boolean blocked, Long id);

    @Transactional
    @Modifying
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT v FROM Vacuum v WHERE "
            + "(:name IS NULL OR v.name = :name) "
            + "AND (:status IS NULL OR v.status IN :status) "
            + "AND (:dateFrom IS NULL OR v.dateCreated >= :dateFrom) "
            + "AND (:dateTo IS NULL OR v.dateCreated <= :dateTo)")
    List<Vacuum> findByParameters(
            @Param("name") String name,
            @Param("status") List<Status> status,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo
    );

}
