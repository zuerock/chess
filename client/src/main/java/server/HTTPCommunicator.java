package server;

import java.io.*;
import java.net.*;
import jsonConverter.JsonConverter;

public class HTTPCommunicator {
    private JsonConverter converter = JsonConverter.getInstance();

    private static HTTPCommunicator instance;

    private HTTPCommunicator() {}

    public static HTTPCommunicator getInstance() {
        if (instance == null) {
            instance = new HTTPCommunicator();
        }
        return instance;
    }

    public <T> T makeRequest(String type, String urlString, Object request, String authToken,
                             Class<T> responseClass) throws IOException {
        try {
            URL url = new URI(urlString).toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(5000);
            connection.setRequestMethod(type);
            connection.setDoOutput(doesOutput(type));

            if (authToken != null) {
                connection.addRequestProperty("Authorization", authToken);
            }

            connection.setRequestProperty("Content-Type", "application/json");

            connection.connect();

            if(doesOutput(type)) {
                try (OutputStream requestBody = connection.getOutputStream();) {
                    String json = converter.toJson(request);
                    requestBody.write(json.getBytes());
                    requestBody.flush();
                }
            }

            InputStream responseBody;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responseBody = connection.getInputStream();
            }
            else {
                responseBody = connection.getErrorStream();
            }
            InputStreamReader reader = new InputStreamReader(responseBody);

            return converter.fromJson(reader, responseClass);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean doesOutput(String type) {
        return type.equals("POST") || type.equals("PUT");
    }
}
