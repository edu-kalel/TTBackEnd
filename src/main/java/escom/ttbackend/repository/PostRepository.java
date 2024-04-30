package escom.ttbackend.repository;

import escom.ttbackend.model.compositekeys.PostId;
import escom.ttbackend.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, PostId> {
}
