package packagee;

import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private Database database;

    public AuthController() {
        this.database = Database.getInstance();
    }

    public Response login(String username, String password) {
        for (User user : database.getUsers()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    Map<String, Object> serializedUser = new HashMap<>();
                    serializedUser.put("id", user.getId());
                    serializedUser.put("username", user.getUsername());
                    serializedUser.put("firstname", user.getFirstname());
                    serializedUser.put("lastname", user.getLastname());
                    
                    if (user instanceof Administrator) {
                        serializedUser.put("type", "admin");
                    } else if (user instanceof Doctor) {
                        Doctor d = (Doctor) user;
                        serializedUser.put("type", "doctor");
                        serializedUser.put("specialty", d.getSpecialty());
                        serializedUser.put("licenceNumber", d.getLicenceNumber());
                    } else if (user instanceof Patient) {
                        serializedUser.put("type", "patient");
                    }
                    
                    return new Response(200, "Login successful", serializedUser);
                } else {
                    return new Response(401, "Invalid password");
                }
            }
        }
        return new Response(404, "User not found");
    }
}
