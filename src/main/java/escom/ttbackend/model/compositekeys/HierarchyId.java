package escom.ttbackend.model.compositekeys;

import escom.ttbackend.model.entities.User;

import java.io.Serializable;
import java.util.Objects;

public class HierarchyId implements Serializable {
    private User parent_email;
    private User child_email;

    public HierarchyId(User parent_email, User child_email) {
        this.parent_email = parent_email;
        this.child_email = child_email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HierarchyId that = (HierarchyId) o;
        return Objects.equals(parent_email, that.parent_email) && Objects.equals(child_email, that.child_email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent_email, child_email);
    }
}
