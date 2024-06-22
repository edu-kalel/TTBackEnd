package escom.ttbackend.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.model.enums.Sex;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.StaffRegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private Mapper mapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserService userService;

  @InjectMocks
  private AdminService adminService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRegisterNewUser_UserAlreadyExists() {
    StaffRegistrationDTO request = new StaffRegistrationDTO();
    request.setEmail("existing@example.com");
    when(userRepository.existsById(request.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> adminService.registerNewUser(request, "clinic"))
      .isInstanceOf(EntityExistsException.class)
      .hasMessage("User Already Exists");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testRegisterNewUser_ParentUserNotFound() {
    StaffRegistrationDTO request = new StaffRegistrationDTO();
    request.setEmail("new@example.com");
    request.setParent_email("parent@example.com");
    when(userRepository.existsById(request.getEmail())).thenReturn(false);
    when(userRepository.findById(request.getParent_email())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminService.registerNewUser(request, "clinic"))
      .isInstanceOf(UsernameNotFoundException.class)
      .hasMessage("Parent User does not exist");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testRegisterNewUser_SuccessfulRegistration() {
    StaffRegistrationDTO request = new StaffRegistrationDTO();
    request.setEmail("new@example.com");
    request.setFirst_name("NewName");
    request.setPassword("newPassword");
    request.setDate_of_birth(LocalDate.now());
    request.setPhone("1234567890");
    request.setSex(Sex.FEM);
    request.setParent_email("");
    request.setRole(Role.PATIENT);
    when(userRepository.existsById(request.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

    User user = new User();
    user.setEmail(request.getEmail());
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserDTO userDTO = new UserDTO();
    when(mapper.mapToUserDTO(user)).thenReturn(userDTO);

    UserDTO result = adminService.registerNewUser(request, "clinic");

    assertThat(result).isEqualTo(userDTO);
    verify(userRepository).save(any(User.class));
  }

  @Test
  public void testUpdateUser_UserNotFound() {
    StaffRegistrationDTO request = new StaffRegistrationDTO();
    request.setEmail("nonexistent@example.com");
    when(userRepository.findById(request.getEmail())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminService.updateUser(request))
      .isInstanceOf(UsernameNotFoundException.class)
      .hasMessage("User does not exist");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testUpdateUser_SuccessfulUpdate() {
    StaffRegistrationDTO request = new StaffRegistrationDTO();
    request.setEmail("existing@example.com");
    request.setFirst_name("NewName");
    request.setPassword("newPassword");
    request.setDate_of_birth(LocalDate.now());
    request.setPhone("1234567890");
    request.setSex(Sex.FEM);
    request.setParent_email("");
    request.setRole(Role.PATIENT);
    when(userRepository.findById(request.getEmail())).thenReturn(Optional.of(new User()));
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedNewPassword");

    User user = new User();
    user.setEmail(request.getEmail());
    user.setFirst_name(request.getFirst_name());
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserDTO userDTO = new UserDTO();
    when(mapper.mapToUserDTO(user)).thenReturn(userDTO);

    UserDTO result = adminService.updateUser(request);

    assertThat(result).isEqualTo(userDTO);
    verify(userRepository).save(any(User.class));
  }

  @Test
  public void testDeleteUser_UserNotFound() {
    when(userRepository.findById("nonexistent@example.com")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminService.deleteUser("nonexistent@example.com"))
      .isInstanceOf(UsernameNotFoundException.class)
      .hasMessage("User does not exist");

    verify(userRepository, never()).deleteById("nonexistent@example.com");
  }

  @Test
  public void testDeleteUser_SuccessfulDeletion() {
    User user = new User();
    user.setEmail("existing@example.com");
    user.setFirst_name("NewName");
    user.setPassword("newPassword");
    user.setDate_of_birth(LocalDate.now());
    user.setPhone("1234567890");
    user.setClinic("clinic");
    user.setRole(Role.PATIENT);
    user.setSex(Sex.FEM);
    when(userRepository.findById("existing@example.com")).thenReturn(Optional.of(user));

    adminService.deleteUser("existing@example.com");

    verify(userRepository).deleteById("existing@example.com");
  }



}
