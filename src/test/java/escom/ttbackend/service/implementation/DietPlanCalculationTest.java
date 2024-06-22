package escom.ttbackend.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.ActivityLevel;
import escom.ttbackend.model.enums.Sex;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import escom.ttbackend.presentation.dto.calculation.DietResponseBody;
import escom.ttbackend.presentation.dto.calculation.PortionsDTO;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DietPlanCalculationTest {

  @InjectMocks
  private DietPlanCalculationsService dietPlanCalculationsService;

  @Mock
  private CalculationRequestClient calculationRequestClient;

  @Mock
  private Mapper mapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCalculateGER() {
    User patient = new User();
    patient.setSex(Sex.MASC);
    PatientRecord patientRecord = new PatientRecord();
    patientRecord.setPatientWeight(80);
    patientRecord.setPatientHeight(190);
    int age = 25;

    int ger = dietPlanCalculationsService.calculateGER(patient, patientRecord, age);

    assertThat(ger).isEqualTo(1962);
  }

  @Test
  public void testCalculateGET() {
    int ger = 1765;
    ActivityLevel activityLevel = ActivityLevel.LIGHT;
    Sex sex = Sex.MASC;

    int get = dietPlanCalculationsService.calculateGET(ger, activityLevel, sex);

    assertThat(get).isEqualTo(2735);
  }

  @Test
  public void testGetPortions() throws IOException {
    DietRequestBody request = new DietRequestBody();
    DietResponseBody responseBody = new DietResponseBody();
    responseBody.setLecheDescremada(1.0);
    when(calculationRequestClient.sendAndReceive(request)).thenReturn(responseBody);

    PortionsDTO portionsDTO = new PortionsDTO();
    when(mapper.mapToPortionsDTO(responseBody)).thenReturn(portionsDTO);

    PortionsDTO result = dietPlanCalculationsService.getPortions(request);

    assertThat(result).isEqualTo(portionsDTO);
    verify(calculationRequestClient).sendAndReceive(request);
  }
}
