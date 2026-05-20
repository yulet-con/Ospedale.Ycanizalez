package packagee;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Database implements Subject {
    private static Database instance;
    private List<User> users;
    private List<Appointment> appointments;
    private List<Hospitalization> hospitalizations;
    private List<Observer> observers;
    private int appointmentCounter = 0;
    private int hospitalizationCounter = 0;

    private Database() {
        users = new ArrayList<>();
        appointments = new ArrayList<>();
        hospitalizations = new ArrayList<>();
        observers = new ArrayList<>();
        loadFromJson();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public User findUserById(long id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    private void loadFromJson() {
        try {
            // Load Users
            File usersFile = new File("json/users.json");
            if (usersFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get("json", "users.json")));
                JSONObject jsonObject = new JSONObject(content);
                JSONArray usersArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject u = usersArray.getJSONObject(i);
                    String type = u.getString("type");
                    long id = u.getLong("id");
                    String username = u.getString("username");
                    String firstname = u.getString("firstname");
                    String lastname = u.getString("lastname");
                    String password = u.getString("password");
                    
                    if (type.equals("admin")) {
                        users.add(new Administrator(id, username, firstname, lastname, password));
                    } else if (type.equals("patient")) {
                        String email = u.optString("email", "");
                        String birthdateStr = u.optString("birthdate", "2000-01-01");
                        LocalDate birthdate = LocalDate.parse(birthdateStr);
                        boolean gender = u.optBoolean("gender", true);
                        long phone = u.optLong("phone", 0);
                        String address = u.optString("address", "");
                        users.add(new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address));
                    } else if (type.equals("doctor")) {
                        String specStr = u.optString("specialty", "GENERAL_MEDICINE");
                        Specialty specialty = Specialty.valueOf(specStr);
                        String licenceNumber = u.optString("licenceNumber", "");
                        String assignedOffice = u.optString("assignedOffice", "");
                        users.add(new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice));
                    }
                }
            }

            // Load Appointments
            File apptsFile = new File("json/appointments.json");
            if (apptsFile.exists()) {
                String content = new String(Files.readAllBytes(apptsFile.toPath()));
                JSONObject jsonObject = new JSONObject(content);
                JSONArray apptsArray = jsonObject.getJSONArray("appointments");
                for (int i = 0; i < apptsArray.length(); i++) {
                    JSONObject aJson = apptsArray.getJSONObject(i);
                    String id = aJson.getString("id");
                    long patientId = aJson.getLong("patientId");
                    long doctorId = aJson.getLong("doctorId");
                    String specStr = aJson.optString("specialty", "");
                    Specialty specialty = specStr.isEmpty() ? null : Specialty.valueOf(specStr);
                    LocalDateTime datetime = LocalDateTime.parse(aJson.getString("datetime"));
                    String reason = aJson.getString("reason");
                    boolean type = aJson.getBoolean("type");
                    AppointmentStatus status = AppointmentStatus.valueOf(aJson.getString("status"));
                    String diagnosis = aJson.optString("diagnosis", "");
                    String observations = aJson.optString("observations", "");
                    String recommendedTreatment = aJson.optString("recommendedTreatment", "");
                    String followUp = aJson.optString("followUp", "");

                    Patient patient = (Patient) findUserById(patientId);
                    Doctor doctor = (Doctor) findUserById(doctorId);

                    Appointment app = new Appointment(id, patient, doctor, specialty, datetime, reason, type);
                    app.setStatus(status);
                    if (!diagnosis.isEmpty()) app.setDiagnosis(diagnosis);
                    if (!observations.isEmpty()) app.setObservations(observations);
                    if (!recommendedTreatment.isEmpty()) app.setRecommendedTreatment(recommendedTreatment);
                    if (!followUp.isEmpty()) app.setFollowUp(followUp);

                    // Load prescriptions
                    JSONArray prescriptionsArr = aJson.optJSONArray("prescriptions");
                    if (prescriptionsArr != null) {
                        for (int j = 0; j < prescriptionsArr.length(); j++) {
                            JSONObject pJson = prescriptionsArr.getJSONObject(j);
                            String medName = pJson.getString("medicationName");
                            double dose = pJson.getDouble("dose");
                            String adminRoute = pJson.getString("administrationRoute");
                            int duration = pJson.getInt("treatmentDuration");
                            String addInstr = pJson.optString("additionalInstructions", "");
                            int freq = pJson.getInt("frecuency");
                            new Prescription(app, medName, dose, adminRoute, duration, addInstr, freq);
                        }
                    }

                    appointments.add(app);
                    if (patient != null) {
                        patient.addAppointment(app);
                    }
                    if (doctor != null) {
                        doctor.getAppointments().add(app);
                    }

                    // Update appointmentCounter
                    try {
                        String[] parts = id.split("-");
                        int num = Integer.parseInt(parts[parts.length - 1]);
                        if (num >= appointmentCounter) {
                            appointmentCounter = num + 1;
                        }
                    } catch (Exception ignored) {}
                }
            }

            // Load Hospitalizations
            File hospsFile = new File("json/hospitalizations.json");
            if (hospsFile.exists()) {
                String content = new String(Files.readAllBytes(hospsFile.toPath()));
                JSONObject jsonObject = new JSONObject(content);
                JSONArray hospsArray = jsonObject.getJSONArray("hospitalizations");
                for (int i = 0; i < hospsArray.length(); i++) {
                    JSONObject hJson = hospsArray.getJSONObject(i);
                    String id = hJson.getString("id");
                    long patientId = hJson.getLong("patientId");
                    long doctorId = hJson.getLong("doctorId");
                    LocalDate date = LocalDate.parse(hJson.getString("date"));
                    String reason = hJson.getString("reason");
                    String roomTypeStr = hJson.optString("roomType", "");
                    RoomType roomType = roomTypeStr.isEmpty() ? null : RoomType.valueOf(roomTypeStr);
                    String observations = hJson.optString("observations", "");
                    HospitalizationStatus status = HospitalizationStatus.valueOf(hJson.getString("status"));

                    Patient patient = (Patient) findUserById(patientId);
                    Doctor doctor = (Doctor) findUserById(doctorId);

                    Hospitalization hosp = new Hospitalization(id, patient, doctor, date, reason, roomType, observations, status);
                    
                    hospitalizations.add(hosp);

                    // Update hospitalizationCounter
                    try {
                        String[] parts = id.split("-");
                        int num = Integer.parseInt(parts[parts.length - 1]);
                        if (num >= hospitalizationCounter) {
                            hospitalizationCounter = num + 1;
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToJson() {
        try {
            // Create json directory if it doesn't exist
            File jsonDir = new File("json");
            if (!jsonDir.exists()) {
                jsonDir.mkdirs();
            }

            // Save Users
            JSONObject usersObj = new JSONObject();
            JSONArray usersArr = new JSONArray();
            for (User u : users) {
                JSONObject uJson = new JSONObject();
                uJson.put("id", u.getId());
                uJson.put("username", u.getUsername());
                uJson.put("firstname", u.getFirstname());
                uJson.put("lastname", u.getLastname());
                uJson.put("password", u.getPassword());
                if (u instanceof Patient) {
                    Patient p = (Patient) u;
                    uJson.put("type", "patient");
                    uJson.put("email", p.getEmail());
                    uJson.put("birthdate", p.getBirthdate().toString());
                    uJson.put("gender", p.isGender());
                    uJson.put("phone", p.getPhone());
                    uJson.put("address", p.getAddress());
                } else if (u instanceof Doctor) {
                    Doctor d = (Doctor) u;
                    uJson.put("type", "doctor");
                    uJson.put("specialty", d.getSpecialty().name());
                    uJson.put("licenceNumber", d.getLicenceNumber());
                    uJson.put("assignedOffice", d.getAssignedOffice());
                } else if (u instanceof Administrator) {
                    uJson.put("type", "admin");
                }
                usersArr.put(uJson);
            }
            usersObj.put("users", usersArr);
            Files.write(Paths.get("json", "users.json"), usersObj.toString(2).getBytes());

            // Save Appointments
            JSONObject apptsObj = new JSONObject();
            JSONArray apptsArr = new JSONArray();
            for (Appointment a : appointments) {
                JSONObject aJson = new JSONObject();
                aJson.put("id", a.getId());
                aJson.put("patientId", a.getPatient().getId());
                aJson.put("doctorId", a.getDoctor() != null ? a.getDoctor().getId() : 0);
                aJson.put("specialty", a.getSpecialty() != null ? a.getSpecialty().name() : "");
                aJson.put("datetime", a.getDatetime().toString());
                aJson.put("reason", a.getReason());
                aJson.put("type", a.isType());
                aJson.put("status", a.getStatus().name());
                aJson.put("diagnosis", a.getDiagnosis() != null ? a.getDiagnosis() : "");
                aJson.put("observations", a.getObservations() != null ? a.getObservations() : "");
                aJson.put("recommendedTreatment", a.getRecommendedTreatment() != null ? a.getRecommendedTreatment() : "");
                aJson.put("followUp", a.getFollowUp() != null ? a.getFollowUp() : "");

                JSONArray prescArr = new JSONArray();
                if (a.getPrescriptions() != null) {
                    for (Prescription p : a.getPrescriptions()) {
                        JSONObject pJson = new JSONObject();
                        pJson.put("medicationName", p.getMedicationName());
                        pJson.put("dose", p.getDose());
                        pJson.put("administrationRoute", p.getAdministrationRoute());
                        pJson.put("treatmentDuration", p.getTreatmentDuration());
                        pJson.put("additionalInstructions", p.getAdditionalInstructions() != null ? p.getAdditionalInstructions() : "");
                        pJson.put("frecuency", p.getFrecuency());
                        prescArr.put(pJson);
                    }
                }
                aJson.put("prescriptions", prescArr);
                apptsArr.put(aJson);
            }
            apptsObj.put("appointments", apptsArr);
            Files.write(Paths.get("json", "appointments.json"), apptsObj.toString(2).getBytes());

            // Save Hospitalizations
            JSONObject hospsObj = new JSONObject();
            JSONArray hospsArr = new JSONArray();
            for (Hospitalization h : hospitalizations) {
                JSONObject hJson = new JSONObject();
                hJson.put("id", h.getId());
                hJson.put("patientId", h.getPatient().getId());
                hJson.put("doctorId", h.getDoctor() != null ? h.getDoctor().getId() : 0);
                hJson.put("date", h.getDate().toString());
                hJson.put("reason", h.getReason());
                hJson.put("roomType", h.getRoomType() != null ? h.getRoomType().name() : "");
                hJson.put("observations", h.getObservations() != null ? h.getObservations() : "");
                hJson.put("status", h.getStatus().name());
                hospsArr.put(hJson);
            }
            hospsObj.put("hospitalizations", hospsArr);
            Files.write(Paths.get("json", "hospitalizations.json"), hospsObj.toString(2).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public String generateAppointmentId(long patientId) {
        String id = String.format("A-%d-%04d", patientId, appointmentCounter++);
        return id;
    }

    public String generateHospitalizationId(long patientId) {
        String id = String.format("H-%d-%04d", patientId, hospitalizationCounter++);
        return id;
    }

    public void addUser(User user) {
        this.users.add(user);
        notifyObservers();
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        notifyObservers();
    }

    public void addHospitalization(Hospitalization hospitalization) {
        this.hospitalizations.add(hospitalization);
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
