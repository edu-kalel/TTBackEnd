package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.enums.Ailment;
import escom.ttbackend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class User implements Serializable, UserDetails {
    @Id
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = false)
    private String clinic;
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
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "ailments",
            joinColumns = @JoinColumn(name = "patient_email")
    )
    @Enumerated(EnumType.STRING)
    private Set<Ailment> ailments;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private DietPlan dietPlan;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<PatientRecord> patientRecords;

    @OneToMany(mappedBy = "nutritionist", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Appointment> appointment_nutritionist;
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Appointment> appointment_patient;
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Post> posts;

    //TODO: one to many self for hierarchies
    @ManyToOne
    @JoinColumn(name= "parent", referencedColumnName = "email")
    private User parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> children;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}