package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Post;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.PostDTO;
import escom.ttbackend.repository.PostRepository;
import escom.ttbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PostRepository postRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;

    public PostDTO newPost(String content, String patient_email) {
        var patient = userRepository.findById(patient_email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var post = Post.builder()
                .patient(patient)
                .date_time(LocalDateTime.now())
                .content(content)
                .build();
        return mapper.mapToPostDTO(postRepository.save(post));//TODO maybe handle potential errors ?
    }
}
