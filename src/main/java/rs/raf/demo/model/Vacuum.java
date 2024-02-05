package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
//@EntityListeners({UserListener.class})
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Status status;

    @ManyToOne
    private User user;

    @Column
    private boolean active;

    @Column
    private boolean isBlocked;

    @Column
    private int cycleNumber;

    @Version
    private int version;

    @Column
    private LocalDate dateCreated;


}
