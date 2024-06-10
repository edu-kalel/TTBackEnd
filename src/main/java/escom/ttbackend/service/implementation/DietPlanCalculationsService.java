package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.ActivityLevel;
import escom.ttbackend.model.enums.Sex;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import escom.ttbackend.presentation.dto.calculation.DietResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietPlanCalculationsService {

    private final CalculationRequestClient calculationRequestClient;

    public int calculateGER(User patient, PatientRecord patientRecord, int patientAge){
        if (patient.getSex().equals(Sex.MASC)){
            return (int) (66.5+(13.75*patientRecord.getPatientWeight())+(5.08*patientRecord.getPatientHeight())-(6.78*patientAge));
        }
        else {
            return (int) (66.5+(9.56*patientRecord.getPatientWeight())+(1.85*patientRecord.getPatientHeight())-(4.68*patientAge));
        }
    }

    public int calculateGET(int ger, ActivityLevel activityLevel, Sex sex){
        double get=0;
        if (sex == Sex.FEM){
            if (activityLevel == ActivityLevel.SEDENTARY)
                get = ger*1.2;
            if (activityLevel == ActivityLevel.LIGHT)
                get = ger*1.56;
            if (activityLevel == ActivityLevel.MODERATED)
                get = ger*1.64;
            if (activityLevel == ActivityLevel.INTENSE)
                get = ger*1.82;
        }
        else {
            if (activityLevel == ActivityLevel.SEDENTARY)
                get = ger*1.2;
            if (activityLevel == ActivityLevel.LIGHT)
                get = ger*1.55;
            if (activityLevel == ActivityLevel.MODERATED)
                get = ger*1.78;
            if (activityLevel == ActivityLevel.INTENSE)
                get = ger*2.10;
        }
        return (int) get;
    }


    public DietResponseBody getPortions(DietRequestBody request) throws IOException {
        log.info("enters get portions in dietplancalculation service");
        DietResponseBody received = calculationRequestClient.sendAndReceive(request);
        if (received.getLeche2_com()>received.getLeche1_com()) {
            received.setLeche1_com(received.getLeche1_com() + received.getLeche2_com() + 0.5);
            received.setLeche2_com(0);
        }
        if (received.getLeche4_com()>received.getLeche3_com()) {
            received.setLeche3_com(received.getLeche3_com() + received.getLeche4_com());
            received.setLeche4_com(0);
        }
        log.info("Modified -> {}", received);
        return received;
    }

}
