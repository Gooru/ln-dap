
package org.gooru.dap.constants;

/**
 * @author szgooru Created On 04-Apr-2019
 */
public final class HttpConstants {
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_JSON = "application/json";

  private HttpConstants() {
    throw new AssertionError();
  }

  public enum HttpStatus {

    SUCCESS(200, "Successful"), CREATED(201, "Created"), ACCEPTED(202, "Accepted"), NO_CONTENT(204,
        "No Content"), MOVED_PERMANENTLY(302, "Moved Permanently"), NOT_MODIFIED(304,
            "Not Modified"), BAD_REQUEST(400, "Bad Request"), UNAUTHORIZED(401,
                "Unauthorized"), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404,
                    "Not Found"), NOT_ACCEPTABLE(406, "Not Acceptable"), TIMED_OUT(408,
                        "Request Timeout"), TOO_LARGE(413,
                            "Request Entity Too Large"), TOO_MANY_REQUESTS(429,
                                "Too Many Requests"), ERROR(500,
                                    "Internal Server Error"), UPSTREAM_ERROR(502,
                                        "Error from upstream server");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return this.code;
    }

    public String getMessage() {
      return this.message;
    }
  }
}
