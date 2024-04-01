package escom.ttbackend.model.compositekeys;

import escom.ttbackend.model.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class PostId implements Serializable {
    private User patient_email;
    private LocalDateTime date_time;

    public PostId(User patient_email, LocalDateTime date_time) {
        this.patient_email = patient_email;
        this.date_time = date_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostId postId = (PostId) o;
        return Objects.equals(patient_email, postId.patient_email) && Objects.equals(date_time, postId.date_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient_email, date_time);
    }
}
