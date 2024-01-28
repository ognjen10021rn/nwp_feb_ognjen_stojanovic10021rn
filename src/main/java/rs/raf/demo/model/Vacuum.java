package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private User user;

    @Column
    private boolean active;

    @Version
    private int version;

    @Column
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date dateFrom;

    @Column
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date dateTo;

}
