package packagee;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class PatientController {
    private Database database;

    public PatientController() {
        this.database = Database.getInstance();
    }

    public Response registerPatient(String firstname, String lastname, String idStr, String genderStr, String birthdateStr, String address, String phoneStr, String email, String username, String password, String confirmPassword) {
        if (firstname == null || firstname.trim().isEmpty()) return new Response(400, "Firstname is required");
        if (lastname == null || lastname.trim().isEmpty()) return new Response(400, "Lastname is required");
        if (username == null || username.trim().isEmpty()) return new Response(400, "Username is required");
        if (password == null || password.trim().isEmpty()) return new Response(400, "Password is required");

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return new Response(400, "ID must be a number");
        }

        long phone;
        try {
            phone = Long.parseLong(phoneStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Phone must be a number");
        }

        // Validation
        if (id <= 0 || String.valueOf(id).length() != 12) {
            return new Response(400, "ID must be exactly 12 digits and greater than 0");
        }
        for (User u : database.getUsers()) {
            if (u.getId() == id) return new Response(400, "ID already exists");
            if (u.getUsername().equals(username)) return new Response(400, "Username already exists");
        }
        if (!password.equals(confirmPassword)) {
            return new Response(400, "Passwords do not match");
        }
        if (String.valueOf(phone).length() != 10) {
            return new Response(400, "Phone must be exactly 10 digits");
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$", email)) {
            return new Response(400, "Email must be valid and end with .com");
        }
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(birthdateStr);
        } catch (DateTimeParseException e) {
            return new Response(400, "Birthdate must be in format AAAA-MM-DD");
        }

        Boolean gender = null;
        if ("Female".equalsIgnoreCase(genderStr)) {
            gender = false;
        } else if ("Male".equalsIgnoreCase(genderStr)) {
            gender = true;
        } else {
            return new Response(400, "Gender must be selected");
        }

        Patient newPatient = new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
        database.addUser(newPatient);
        
        // Save database changes
        database.saveToJson();

        return new Response(201, "Patient registered successfully");
    }

    public Response updatePatient(String idStr, String firstname, String lastname, String genderStr, String birthdateStr, String address, String phoneStr, String email, String username, String password, String confirmPassword) {
        if (firstname == null || firstname.trim().isEmpty()) return new Response(400, "Firstname is required");
        if (lastname == null || lastname.trim().isEmpty()) return new Response(400, "Lastname is required");
        if (username == null || username.trim().isEmpty()) return new Response(400, "Username is required");
        if (password == null || password.trim().isEmpty()) return new Response(400, "Password is required");

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return new Response(400, "ID must be a number");
        }

        long phone;
        try {
            phone = Long.parseLong(phoneStr);
        } catch (NumberFormatException e) {
            return new Response(400, "Phone must be a number");
        }

        Patient p = null;
        for (User u : database.getUsers()) {
            if (u.getId() == id && u instanceof Patient) {
                p = (Patient) u;
                break;
            }
        }
        if (p == null) return new Response(404, "Patient not found");
        
        for (User u : database.getUsers()) {
            if (u.getId() != id && u.getUsername().equals(username)) return new Response(400, "Username already exists");
        }
        if (!password.equals(confirmPassword)) {
            return new Response(400, "Passwords do not match");
        }
        if (String.valueOf(phone).length() != 10) {
            return new Response(400, "Phone must be exactly 10 digits");
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$", email)) {
            return new Response(400, "Email must be valid and end with .com");
        }
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(birthdateStr);
        } catch (DateTimeParseException e) {
            return new Response(400, "Birthdate must be in format AAAA-MM-DD");
        }

        Boolean gender = null;
        if ("Female".equalsIgnoreCase(genderStr)) {
            gender = false;
        } else if ("Male".equalsIgnoreCase(genderStr)) {
            gender = true;
        } else {
            return new Response(400, "Gender must be selected");
        }

        p.setFirstname(firstname);
        p.setLastname(lastname);
        p.setUsername(username);
        p.setPassword(password);
        p.setEmail(email);
        p.setBirthdate(birthdate);
        p.setGender(gender);
        p.setPhone(phone);
        p.setAddress(address);
        
        database.notifyObservers();
        
        // Save database changes
        database.saveToJson();

        return new Response(200, "Patient updated successfully");
    }
}
