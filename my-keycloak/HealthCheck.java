import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

// This should be much much simpler with JDK 25
public class HealthCheck {

  static final int RETURN_OK = 0;
  static final int RETURN_ERROR = -1;

  public static void main(String[] args) {
    var uri = URI.create("http://localhost:9000/health/live");

    try {
      System.exit(healthCheck(uri));
    } catch (Exception e) {
      // Shut up and give me candy
      System.exit(RETURN_ERROR);
    }
  }

  private static int healthCheck(URI uri) throws IOException {
    var connection = uri.toURL().openConnection();

    if (!(connection instanceof HttpURLConnection httpConnection)) {
      return RETURN_ERROR;
    }

    return HttpURLConnection.HTTP_OK == httpConnection.getResponseCode() ? RETURN_OK : RETURN_ERROR;
  }
}