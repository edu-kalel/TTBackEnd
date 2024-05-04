package escom.ttbackend.model.compositekeys;

import escom.ttbackend.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class PostId implements Serializable {
    private User patient;
    private LocalDateTime date_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostId postId = (PostId) o;
        return Objects.equals(patient, postId.patient) && Objects.equals(date_time, postId.date_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, date_time);
    }
}
