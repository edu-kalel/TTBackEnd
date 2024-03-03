package escom.ttbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class User implements Serializable {
    @Id
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = false)
    private String first_name;
    @Column(length = 50, nullable = false)
    private String last_name;
    @Column(length = 60, nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate date_of_birth;
    @Column(nullable = false, length = 10)
    private String phone;
    @Column(nullable = false)
    private boolean sex;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}