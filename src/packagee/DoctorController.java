package packagee;

import java.util.regex.Pattern;

public class DoctorController {
    private Database database;

    public DoctorController() {
        this.database = Database.getInstance();
    }

    public Response registerDoctor(String firstname, String lastname, String idStr, String specialtyStr, String licenceNumber, String assignedOffice, String username, String password, String confirmPassword) {
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
        if (!Pattern.matches("^L-\\d{10} MTL$", licenceNumber)) {
            return new Response(400, "Licence Number must follow the format L-XXXXXXXXXX MTL");
        }
        if (!Pattern.matches("^O-\\d{3}$", assignedOffice)) {
            return new Response(400, "Assigned office must follow the format O-XXX");
        }

        Specialty specialty;
        try {
            specialty = Specialty.valueOf(specialtyStr.replaceAll(" & ", "_").replaceAll(" ", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Response(400, "Invalid specialty");
        }

        Doctor newDoctor = new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
        database.addUser(newDoctor);
        database.saveToJson();

        return new Response(201, "Doctor registered successfully");
    }

    public Response updateDoctor(String idStr, String firstname, String lastname, String specialtyStr, String licenceNumber, String assignedOffice, String username, String password, String confirmPassword) {
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

        Doctor d = null;
        for (User u : database.getUsers()) {
            if (u.getId() == id && u instanceof Doctor) {
                d = (Doctor) u;
                break;
            }
        }
        if (d == null) return new Response(404, "Doctor not found");
        
        for (User u : database.getUsers()) {
            if (u.getId() != id && u.getUsername().equals(username)) return new Response(400, "Username already exists");
        }
        if (!password.equals(confirmPassword)) {
            return new Response(400, "Passwords do not match");
        }
        if (!Pattern.matches("^L-\\d{10} MTL$", licenceNumber)) {
            return new Response(400, "Licence Number must follow the format L-XXXXXXXXXX MTL");
        }
        if (!Pattern.matches("^O-\\d{3}$", assignedOffice)) {
            return new Response(400, "Assigned office must follow the format O-XXX");
        }

        Specialty specialty;
        try {
            specialty = Specialty.valueOf(specialtyStr.replaceAll(" & ", "_").replaceAll(" ", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Response(400, "Invalid specialty");
        }

        d.setFirstname(firstname);
        d.setLastname(lastname);
        d.setUsername(username);
        d.setPassword(password);
        d.setSpecialty(specialty);
        d.setLicenceNumber(licenceNumber);
        d.setAssignedOffice(assignedOffice);
        
        database.notifyObservers();
        database.saveToJson();

        return new Response(200, "Doctor updated successfully");
    }
}
