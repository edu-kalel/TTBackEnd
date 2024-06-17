package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.Post;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.DietPlanDTO;
import escom.ttbackend.presentation.dto.PostDTO;
import escom.ttbackend.presentation.dto.SimpleDietPlanDTO;
import escom.ttbackend.repository.DietPlanRepository;
import escom.ttbackend.repository.PostRepository;
import escom.ttbackend.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
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
    private final DietPlanRepository dietPlanRepository;

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



    public List<SimpleDietPlanDTO> getDietPlansList(String email) {
      List<DietPlan> dietPlanList = dietPlanRepository.findAllByUser_EmailOrderByDateDesc(email);
      List<SimpleDietPlanDTO> list = new ArrayList<>();
      for (DietPlan dietPlan : dietPlanList){
        SimpleDietPlanDTO simpleDietPlanDTO = mapper.mapToSimpleDietPlanDTO(dietPlan);
        list.add(simpleDietPlanDTO);
      }
      return list;
    }

  public DietPlanDTO getDietPlanById(String email, Long dietPlanId) {
    var dietPlan =  dietPlanRepository.findById(dietPlanId)
      .orElseThrow(() -> new UsernameNotFoundException("Plan de dieta no existe"));
    DietPlanDTO dietPlanDTO = mapper.mapToDietPlanDTO(dietPlan);
    return dietPlanDTO;
  }
}
