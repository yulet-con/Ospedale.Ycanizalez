package packagee;

public class Response {
    private int statusCode;
    private String message;
    private Object body;

    public Response(int statusCode, String message, Object body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getBody() {
        return body;
    }
}
