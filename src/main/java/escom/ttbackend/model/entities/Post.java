package escom.ttbackend.model.entities;

import escom.ttbackend.model.compositekeys.PostId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "patient", referencedColumnName = "email", nullable = false)
    private User patient;
    @Id
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String content;
}
