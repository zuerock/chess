package client;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.reflect.TypeToken;
import model.*;

public class ServerFacade {
    int port;
    String baseUrl;
    String authToken;

    public ServerFacade(int givenPort) {
        port = givenPort;
        baseUrl = "http://localhost:" + port;
        authToken = null;
    }

    public Map<String, String> doPost(URL url, JsonObject reqJson) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        //write out header
        connection.addRequestProperty("authorization", authToken);

        // write json to output stream
        try (OutputStream outputStream = connection.getOutputStream()) {
            var jsonBody = new Gson().toJson(reqJson);
            outputStream.write(jsonBody.getBytes());
        }

        // get response
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = connection.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                return new Gson().fromJson(reader, Map.class);
            }
        }
        else {
            InputStream responseBody = connection.getErrorStream();
            // TODO: correctly handle error response
            throw new IOException("Failed to post: HTTP error code : " + responseBody);
        }
    }

    public Map<String, String> doGet (URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        connection.addRequestProperty("Authorization", authToken);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream resBody = connection.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                return new Gson().fromJson(reader, Map.class);
            }
        } else {
            InputStream responseBody = connection.getErrorStream();
            // TODO: correctly handle error response
            throw new IOException("Failed to get: HTTP error code : " + responseBody);
        }
    }