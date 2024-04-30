package escom.ttbackend.model.compositekeys;

import escom.ttbackend.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class AppointmentId implements Serializable {
    private User nutritionist;
    private LocalDateTime starting_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentId that = (AppointmentId) o;
        return Objects.equals(nutritionist, that.nutritionist) && Objects.equals(starting_time, that.starting_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nutritionist, starting_time);
    }
}
