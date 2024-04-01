package escom.ttbackend.model.entities;

import escom.ttbackend.model.compositekeys.PostId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@IdClass(PostId.class)
public class Post implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "patient_email", referencedColumnName = "email", nullable = false)
    private User patient_email;
    @Id
    @Column(nullable = false)
    private LocalDateTime date_time;

    @Column(nullable = false)
    private String content;
//    @Column(nullable = false)
//    private LocalDateTime date_time;
}
