package escom.ttbackend.model.compositekeys;

import escom.ttbackend.model.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentId implements Serializable {
    private User nutritionist_email;
    private LocalDateTime starting_time;

    public AppointmentId(User nutritionist_email, LocalDateTime starting_time) {
        this.nutritionist_email = nutritionist_email;
        this.starting_time = starting_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentId that = (AppointmentId) o;
        return Objects.equals(nutritionist_email, that.nutritionist_email) && Objects.equals(starting_time, that.starting_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nutritionist_email, starting_time);
    }
}
