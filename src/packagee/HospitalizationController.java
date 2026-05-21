package packagee;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HospitalizationController {
    private Database database;

    public HospitalizationController() {
        this.database = Database.getInstance();
    }

    public Response requestHospitalization(String patientIdStr, String dateStr, String reason, String doctorIdStr, String roomTypeStr) {
        long patientId;
        try {
            patientId = Long.parseLong(patientIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Patient ID must be a number");
        }

        Patient patient = (Patient) getUserById(patientId);
        if (patient == null) return new Response(404, "Patient not found");

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return new Response(400, "Invalid date (AAAA-MM-DD) format");
        }

        Doctor assignedDoctor = null;
        if (doctorIdStr != null && !doctorIdStr.trim().isEmpty() && !"Select doctor".equalsIgnoreCase(doctorIdStr)) {
            long doctorId;
            try {
                doctorId = Long.parseLong(doctorIdStr.split(" - ")[0]);
            } catch (Exception e) {
                return new Response(400, "Doctor ID must be a number");
            }
            assignedDoctor = (Doctor) getUserById(doctorId);
            if (assignedDoctor == null) return new Response(404, "Doctor not found");
        }

        RoomType roomType = null;
        if (roomTypeStr != null && !roomTypeStr.trim().isEmpty() && !"Select room type".equalsIgnoreCase(roomTypeStr)) {
            try {
                roomType = RoomType.valueOf(roomTypeStr.replaceAll(" ", "_").toUpperCase());
            } catch (IllegalArgumentException e) {
                return new Response(400, "Invalid room type");
            }
        }

        String id = database.generateHospitalizationId(patientId);
        Hospitalization hospitalization = new Hospitalization(id, patient, assignedDoctor, date, reason, roomType, "");
        hospitalization.setStatus(HospitalizationStatus.REQUESTED);
        
        database.addHospitalization(hospitalization);
        patient.setHospitalization(hospitalization);
        if (assignedDoctor != null) assignedDoctor.addHospitalization(hospitalization);

        database.saveToJson();

        return new Response(201, "Hospitalization requested successfully", id);
    }

    public Response approveHospitalization(String hospitalizationId, String doctorIdStr) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        Hospitalization h = getHospitalizationById(hospitalizationId);
        if (h == null) return new Response(404, "Hospitalization not found");
        if (h.getDoctor() != null && h.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to approve this hospitalization");
        if (h.getStatus() != HospitalizationStatus.REQUESTED) return new Response(400, "Hospitalization must be REQUESTED");

        h.setStatus(HospitalizationStatus.ONGOING);
        if (h.getDoctor() == null) {
            Doctor d = (Doctor) getUserById(doctorId);
            h.setDoctor(d);
            d.addHospitalization(h);
        }
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Hospitalization approved (ONGOING)");
    }

    public Response denyHospitalization(String hospitalizationId, String doctorIdStr) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        Hospitalization h = getHospitalizationById(hospitalizationId);
        if (h == null) return new Response(404, "Hospitalization not found");
        if (h.getDoctor() != null && h.getDoctor().getId() != doctorId) return new Response(403, "Not authorized to deny this hospitalization");
        if (h.getStatus() != HospitalizationStatus.REQUESTED) return new Response(400, "Hospitalization must be REQUESTED");

        h.setStatus(HospitalizationStatus.CANCELED);
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Hospitalization denied (CANCELED)");
    }

    public Response sendFromAppointmentToHospitalization(String appointmentId, String doctorIdStr, String reason, String roomTypeStr) {
        AppointmentController ac = new AppointmentController();
        Response res = ac.completeAppointment(appointmentId, doctorIdStr, "Hospitalization Required", reason, "Hospitalization", "None");
        if (res.getStatusCode() != 200) return res;

        Appointment a = null;
        for (Appointment ap : database.getAppointments()) {
            if (ap.getId().equals(appointmentId)) {
                a = ap;
                break;
            }
        }
        
        String id = database.generateHospitalizationId(a.getPatient().getId());
        RoomType roomType = null;
        if (roomTypeStr != null && !roomTypeStr.trim().isEmpty() && !"Select room type".equalsIgnoreCase(roomTypeStr)) {
            try {
                roomType = RoomType.valueOf(roomTypeStr.replaceAll(" ", "_").toUpperCase());
            } catch (IllegalArgumentException e) {
                return new Response(400, "Invalid room type");
            }
        }
        
        Hospitalization h = new Hospitalization(id, a.getPatient(), a.getDoctor(), LocalDate.now(), reason, roomType, "");
        h.setStatus(HospitalizationStatus.ONGOING);
        
        database.addHospitalization(h);
        a.getPatient().setHospitalization(h);
        a.getDoctor().addHospitalization(h);
        
        database.saveToJson();
        return new Response(201, "Sent to hospitalization successfully", id);
    }

    private User getUserById(long id) {
        for (User u : database.getUsers()) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    private Hospitalization getHospitalizationById(String id) {
        for (Hospitalization h : database.getHospitalizations()) {
            if (h.getId().equals(id)) return h;
        }
        return null;
    }

    public Response getPatientHospitalization(String patientIdStr) {
        long patientId;
        try {
            patientId = Long.parseLong(patientIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Patient ID must be a number");
        }

        Patient patient = (Patient) getUserById(patientId);
        if (patient == null) return new Response(404, "Patient not found");
        
        Hospitalization h = patient.getHospitalization();
        if (h == null) return new Response(200, "No hospitalization", null);
        
        return new Response(200, "Success", serializeHospitalization(h));
    }

    public Response getDoctorHospitalizations(String doctorIdStr) {
        long doctorId;
        try {
            doctorId = Long.parseLong(doctorIdStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Doctor ID must be a number");
        }

        List<Hospitalization> result = new ArrayList<>();
        for (Hospitalization h : database.getHospitalizations()) {
            if (h.getDoctor() != null && h.getDoctor().getId() == doctorId) {
                result.add(h);
            }
        }
        
        List<Map<String, Object>> serialized = new ArrayList<>();
        for (Hospitalization h : result) {
            serialized.add(serializeHospitalization(h));
        }
        return new Response(200, "Success", serialized);
    }

    private Map<String, Object> serializeHospitalization(Hospitalization h) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", h.getId());
        map.put("patientId", h.getPatient().getId());
        map.put("patientName", h.getPatient().getFirstname() + " " + h.getPatient().getLastname());
        map.put("doctorId", h.getDoctor() != null ? h.getDoctor().getId() : 0);
        map.put("doctorName", h.getDoctor() != null ? (h.getDoctor().getFirstname() + " " + h.getDoctor().getLastname()) : "N/A");
        map.put("date", h.getDate().toString());
        map.put("reason", h.getReason());
        map.put("roomType", h.getRoomType() != null ? h.getRoomType().name() : "N/A");
        map.put("observations", h.getObservations() != null ? h.getObservations() : "");
        map.put("status", h.getStatus().name());
        return map;
    }

    public Response cancelHospitalization(String hospitalizationId) {
        Hospitalization h = getHospitalizationById(hospitalizationId);
        if (h == null) return new Response(404, "Hospitalization not found");
        h.setStatus(HospitalizationStatus.CANCELED);
        database.notifyObservers();
        database.saveToJson();
        return new Response(200, "Hospitalization canceled successfully");
    }
}
