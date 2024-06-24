package escom.ttbackend.utils;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.CardDTO;
import escom.ttbackend.repository.AppointmentRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardGenerator {

  private final AppointmentRepository appointmentRepository;

  public List<CardDTO> getCards(String nutriEmail){
    List<CardDTO> cards = new ArrayList<>();

    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    List<Appointment> todayAppointments =  appointmentRepository.findByNutritionist_EmailAndStartingTimeBetweenAndAppointmentStatusOrderByStartingTimeAsc(
      nutriEmail, LocalDateTime.now(), endOfDay, AppointmentStatus.CONFIRMED);
    if (todayAppointments.isEmpty()){
      cards.add(new CardDTO("0", "Citas restantes hoy:"));
    }
    else {
      String todayAppointmentsSize = String.valueOf(todayAppointments.size());

      cards.add(new CardDTO(todayAppointmentsSize, "Citas restantes hoy:"));
    }

    Appointment nextAppointment = appointmentRepository.findFirstByNutritionist_EmailAndStartingTimeBetweenAndAppointmentStatusOrderByStartingTimeAsc(nutriEmail, LocalDateTime.now(), endOfDay, AppointmentStatus.CONFIRMED);
    String nextAppointmentTime;
    if(nextAppointment == null){
      nextAppointmentTime = "-";
    }
    else {
      int minutes = nextAppointment.getStartingTime().getMinute();
      String nextAppointmentHour;
      String nextAppointmentMinute;
      if (minutes < 10){
        nextAppointmentMinute = "0" + minutes;
      }
      else {
        nextAppointmentMinute = String.valueOf(minutes);
      }
      nextAppointmentHour = String.valueOf(nextAppointment.getStartingTime().getHour());

      nextAppointmentTime = nextAppointmentHour + ":" + nextAppointmentMinute;
    }

    cards.add(new CardDTO(nextAppointmentTime, "Siguiente cita a las:"));

    List<Appointment> solicited = appointmentRepository.findByNutritionist_EmailAndAppointmentStatus(nutriEmail, AppointmentStatus.SOLICITED);
    String solicitedCount = String.valueOf(solicited.size());
    if (solicited.isEmpty()){
      cards.add(new CardDTO("0", "Solicitudes de cita:"));
    }
    else {
      cards.add(new CardDTO(solicitedCount, "Solicitudes de cita:"));
    }

    String lastAppointmentTime;
    if (todayAppointments.isEmpty()){
      lastAppointmentTime="-";
    }
    else
    {
      Appointment last = todayAppointments.get(todayAppointments.size() - 1);
      int lastMinute = last.getStartingTime().getMinute();
      String lastMinuteString;
      if (lastMinute<10){
        lastMinuteString = "0" + lastMinute;
      }
      else{
        lastMinuteString = String.valueOf(lastMinute);
      }
      lastAppointmentTime = last.getStartingTime().getHour() + ":" + lastMinuteString;
    }

    cards.add(new CardDTO(lastAppointmentTime, "Última cita a las:"));

    return cards;
  }

}
