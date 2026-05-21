package packagee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentController {
    private Database database;

    public AppointmentController() {
        this.database = Database.getInstance();
    }

    public Response requestAppointment(String patientIdStr, String dateStr, String timeStr, boolean isRemote, String reason, String specialtyStr, String doctorIdStr) {
        long patientId;
        try {
            patientId = Long.parseLong(patientIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Patient ID must be a number");
        }

        Patient patient = (Patient) getUserById(patientId);
        if (patient == null) return new Response(404, "Patient not found");

        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(dateStr);
            time = LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            return new Response(400, "Invalid date (AAAA-MM-DD) or time (hh:mm) format");
        }

        if (time.getMinute() % 15 != 0) {
            return new Response(400, "Time minutes must be 00, 15, 30, or 45");
        }

        LocalDateTime datetime = LocalDateTime.of(date, time);
        Doctor assignedDoctor = null;
        Specialty specialty = null;

        if (doctorIdStr != null && !doctorIdStr.trim().isEmpty() && !"Select doctor".equalsIgnoreCase(doctorIdStr)) {
            long doctorId;
            try {
                doctorId = Long.parseLong(doctorIdStr.split(" - ")[0]);
            } catch (Exception e) {
                return new Response(400, "Doctor ID must be a number");
            }
            assignedDoctor = (Doctor) getUserById(doctorId);
            if (assignedDoctor == null) return new Response(404, "Doctor not found");
            if (!isDoctorAvailable(assignedDoctor, datetime)) {
                return new Response(400, "Doctor is not available at the requested time");
            }
            specialty = assignedDoctor.getSpecialty();
        } else if (specialtyStr != null && !specialtyStr.trim().isEmpty() && !"Select specialty".equalsIgnoreCase(specialtyStr)) {
            try {
                specialty = Specialty.valueOf(specialtyStr.replaceAll(" & ", "_").replaceAll(" ", "_").toUpperCase());
            } catch (IllegalArgumentException e) {
                return new Response(400, "Invalid specialty");
            }
            for (User u : database.getUsers()) {
                if (u instanceof Doctor) {
                    Doctor d = (Doctor) u;
                    if (d.getSpecialty() == specialty && isDoctorAvailable(d, datetime)) {
                        assignedDoctor = d;
                        break;
                    }
                }
            }
            if (assignedDoctor == null) {
                return new Response(400, "No doctors available for the selected specialty at the requested time");
            }
        } else {
            return new Response(400, "Must provide either a specific doctor or a specialty");
        }

        String id = database.generateAppointmentId(patientId);
        Appointment appointment = new Appointment(id, patient, assignedDoctor, specialty, datetime, reason, isRemote);
        
        database.addAppointment(appointment);
        patient.addAppointment(appointment);
        assignedDoctor.getAppointments().add(appointment);
        
        database.saveToJson();

        return new Response(201, "Appointment requested successfully", appointment.getId());
    }

    private boolean isDoctorAvailable(Doctor doctor, LocalDateTime datetime) {
        for (Appointment a : doctor.getAppointments()) {
            if (a.getDatetime().equals(datetime) && a.getStatus() != AppointmentStatus.CANCELED) {
                return false;
            }
        }
        return true;
    }

    private User getUserById(long id) {
        for (User u : database.getUsers()) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public Response acceptAppointment(String appointmentId, String doctorIdStr) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        Appointment a = getAppointmentById(appointmentId);
        if (a == null) return new Response(404, "Appointment not found");
        if (a.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to accept this appointment");
        if (a.getStatus() != AppointmentStatus.REQUESTED) return new Response(400, "Appointment is not in REQUESTED state");
        
        a.setStatus(AppointmentStatus.PENDING);
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Appointment accepted");
    }

    public Response completeAppointment(String appointmentId, String doctorIdStr, String diagnosis, String observations, String recommendedTreatment, String followUp) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        Appointment a = getAppointmentById(appointmentId);
        if (a == null) return new Response(404, "Appointment not found");
        if (a.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to complete this appointment");
        if (a.getStatus() != AppointmentStatus.PENDING) return new Response(400, "Appointment must be PENDING to be completed");
        
        a.setStatus(AppointmentStatus.COMPLETED);
        a.setDiagnosis(diagnosis);
        a.setObservations(observations);
        a.setRecommendedTreatment(recommendedTreatment);
        a.setFollowUp(followUp);
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Appointment completed");
    }

    public Response cancelAppointment(String appointmentId, String patientIdStr) {
        long patientId;
        try {
            patientId = Long.parseLong(patientIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Patient ID must be a number");
        }

        Appointment a = getAppointmentById(appointmentId);
        if (a == null) return new Response(404, "Appointment not found");
        if (a.getPatient().getId() != patientId) return new Response(403, "Not authorized to cancel this appointment");
        if (a.getStatus() == AppointmentStatus.COMPLETED) return new Response(400, "Cannot cancel a completed appointment");
        
        a.setStatus(AppointmentStatus.CANCELED);
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Appointment canceled");
    }

    public Response rescheduleAppointment(String appointmentId, String doctorIdStr, String newTimeStr, String reason) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        Appointment a = getAppointmentById(appointmentId);
        if (a == null) return new Response(404, "Appointment not found");
        if (a.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to reschedule this appointment");
        
        LocalTime newTime;
        try {
            newTime = LocalTime.parse(newTimeStr);
        } catch (DateTimeParseException e) {
            return new Response(400, "Invalid time format (hh:mm)");
        }
        if (newTime.getMinute() % 15 != 0) {
            return new Response(400, "Time minutes must be 00, 15, 30, or 45");
        }

        LocalDateTime newDatetime = LocalDateTime.of(a.getDatetime().toLocalDate(), newTime);
        if (!isDoctorAvailable(a.getDoctor(), newDatetime)) {
            return new Response(400, "Doctor is not available at the new time");
        }
        
        a.setDatetime(newDatetime);
        a.setReason(a.getReason() + " | Reschedule reason: " + reason);
        
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Appointment rescheduled");
    }

    public Response prescribeMedication(String appointmentId, String doctorIdStr, String medicationName, String doseStr, String administrationRoute, String treatmentDurationStr, String additionalInstructions, String frecuencyStr) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        double dose;
        try {
            dose = Double.parseDouble(doseStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Dose must be a number");
        }

        int treatmentDuration;
        try {
            treatmentDuration = Integer.parseInt(treatmentDurationStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Treatment duration must be a number");
        }

        int frecuency;
        try {
            frecuency = Integer.parseInt(frecuencyStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Frequency must be a number");
        }

        Appointment a = getAppointmentById(appointmentId);
        if (a == null) return new Response(404, "Appointment not found");
        if (a.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to prescribe in this appointment");
        if (a.getStatus() != AppointmentStatus.PENDING) return new Response(400, "Appointment must be PENDING to prescribe medication");
        
        new Prescription(a, medicationName, dose, administrationRoute, treatmentDuration, additionalInstructions, frecuency);
        database.notifyObservers();
        database.saveToJson();
        return new Response(201, "Medication prescribed successfully");
    }

    private Appointment getAppointmentById(String id) {
        for (Appointment a : database.getAppointments()) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    public Response getPatientAppointments(String patientIdStr) {
        long patientId;
        try {
            patientId = Long.parseLong(patientIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Patient ID must be a number");
        }

        List<Appointment> result = new ArrayList<>();
        for (Appointment a : database.getAppointments()) {
            if (a.getPatient().getId() == patientId) result.add(a);
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return new Response(200, "Success", serializeAppointments(result));
    }

    public Response getDoctorAppointments(String doctorIdStr, boolean onlyPending) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        List<Appointment> result = new ArrayList<>();
        for (Appointment a : database.getAppointments()) {
            if (a.getDoctor().getId() == doctorId) {
                if (!onlyPending || a.getStatus() == AppointmentStatus.PENDING) {
                    result.add(a);
                }
            }
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return new Response(200, "Success", serializeAppointments(result));
    }

    private List<Map<String, Object>> serializeAppointments(List<Appointment> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Appointment a : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("patientId", a.getPatient().getId());
            map.put("patientName", a.getPatient().getFirstname() + " " + a.getPatient().getLastname());
            map.put("doctorId", a.getDoctor() != null ? a.getDoctor().getId() : 0);
            map.put("doctorName", a.getDoctor() != null ? (a.getDoctor().getFirstname() + " " + a.getDoctor().getLastname()) : "N/A");
            map.put("specialty", a.getSpecialty() != null ? a.getSpecialty().name() : "N/A");
            map.put("datetime", a.getDatetime().toString());
            map.put("reason", a.getReason());
            map.put("isRemote", a.isType());
            map.put("status", a.getStatus().name());
            map.put("diagnosis", a.getDiagnosis() != null ? a.getDiagnosis() : "");
            map.put("observations", a.getObservations() != null ? a.getObservations() : "");
            map.put("recommendedTreatment", a.getRecommendedTreatment() != null ? a.getRecommendedTreatment() : "");
            map.put("followUp", a.getFollowUp() != null ? a.getFollowUp() : "");
            
            List<Map<String, Object>> prescList = new ArrayList<>();
            if (a.getPrescriptions() != null) {
                for (Prescription p : a.getPrescriptions()) {
                    Map<String, Object> pMap = new HashMap<>();
                    pMap.put("medicationName", p.getMedicationName());
                    pMap.put("dose", p.getDose());
                    pMap.put("administrationRoute", p.getAdministrationRoute());
                    pMap.put("treatmentDuration", p.getTreatmentDuration());
                    pMap.put("additionalInstructions", p.getAdditionalInstructions() != null ? p.getAdditionalInstructions() : "");
                    pMap.put("frecuency", p.getFrecuency());
                    prescList.add(pMap);
                }
            }
            map.put("prescriptions", prescList);
            result.add(map);
        }
        return result;
    }
}
