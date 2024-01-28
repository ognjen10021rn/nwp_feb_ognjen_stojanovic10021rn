package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
//@EntityListeners({UserListener.class})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Version
    private int version;

    @Column
    private boolean can_search_vacuum;

    @Column
    private boolean can_start_vacuum;

    @Column
    private boolean can_discharge_vacuum;

    @Column
    private boolean can_add_vacuum;

    @Column
    private boolean can_remove_vacuums;


}
