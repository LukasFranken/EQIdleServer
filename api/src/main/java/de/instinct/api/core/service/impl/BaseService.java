package de.instinct.api.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import de.instinct.api.core.API;
import de.instinct.api.core.model.HeaderValue;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.discovery.dto.ServiceInfoDTO;

public class BaseService implements BaseServiceInterface {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    private boolean connected;
    protected String baseUrl;
    private final String tag;

    public BaseService(String tag) {
        this.tag = tag;
        this.client = new OkHttpClient();
    }

    public void loadURL() {
        ServiceInfoDTO serviceInfo = API.discovery().discover(tag);
        if (serviceInfo == null) {
            System.out.println("Error loading baseUrl for " + tag);
            return;
        }
        baseUrl = URLBuilder.build(serviceInfo);
    }

    @Override
    public void connect() {
        if (baseUrl == null) {
            System.out.println("Can't connect: Missing baseUrl for " + tag);
            return;
        }
        if (connected) {
            System.out.println("Already connected: " + tag);
            return;
        }

        String url = baseUrl + "/ping";
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                connected = true;
                System.out.println("Connected to " + tag + " via URL: " + baseUrl);
            } else {
                System.out.println("Error connecting to URL: " + baseUrl + " -> HTTP " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Error connecting to URL: " + baseUrl);
        }
    }

    @Override
    public void disconnect() {
        client = null;
        connected = false;
    }

    @Override
    public boolean isConnected() {
        if (baseUrl == null) System.out.println("No URL loaded for " + tag);
        if (!connected) System.out.println("Not connected to URL: " + baseUrl);
        return connected;
    }

    public String sendRequest(RESTRequest request) {
        setAuthToken(request);
        return switch (request.getType()) {
            case GET -> sendGetRequest(request);
            case POST -> sendPostRequest(request);
            case PUT -> sendPutRequest(request);
            case DELETE -> sendDeleteRequest(request);
        };
    }

    private void setAuthToken(RESTRequest request) {
        if (request.getRequestHeader() == null) {
            request.setRequestHeader(new ArrayList<>());
        }
        request.getRequestHeader().add(
            HeaderValue.builder()
                .key("token")
                .value(API.authKey == null ? "" : API.authKey)
                .build()
        );
    }

    private String sendGetRequest(RESTRequest request) {
        String url = baseUrl + buildURI(request);
        Request.Builder builder = new Request.Builder()
            .url(url)
            .get();
        applyHeaders(builder, request);
        return executeRequest(builder);
    }

    private String sendPostRequest(RESTRequest request) {
        String url = baseUrl + buildURI(request);
        String payload = request.getPayload() == null ? "{}" : ObjectJSONMapper.mapObject(request.getPayload());
        RequestBody body = RequestBody.create(payload, JSON);
        Request.Builder builder = new Request.Builder()
            .url(url)
            .post(body);
        applyHeaders(builder, request);
        return executeRequest(builder);
    }

    private String sendPutRequest(RESTRequest request) {
        String url = baseUrl + buildURI(request);
        String payload = request.getPayload() == null ? "{}" : ObjectJSONMapper.mapObject(request.getPayload());
        RequestBody body = RequestBody.create(payload, JSON);
        Request.Builder builder = new Request.Builder()
            .url(url)
            .put(body);
        applyHeaders(builder, request);
        return executeRequest(builder);
    }

    private String sendDeleteRequest(RESTRequest request) {
        String url = baseUrl + buildURI(request);
        Request.Builder builder = new Request.Builder()
            .url(url)
            .delete();
        applyHeaders(builder, request);
        return executeRequest(builder);
    }

    private String executeRequest(Request.Builder builder) {
        try (Response response = client.newCall(builder.build()).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            System.out.println("Error during request: " + e.getMessage());
        }
        return null;
    }

    private String buildURI(RESTRequest request) {
        String endpoint = request.getEndpoint() == null ? "" : "/" + request.getEndpoint();
        String param    = request.getPathVariable() == null ? "" : "/" + request.getPathVariable();
        return endpoint + param;
    }

    private void applyHeaders(Request.Builder builder, RESTRequest request) {
        List<HeaderValue> headers = request.getRequestHeader();
        if (headers != null) {
            for (HeaderValue header : headers) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
    }
}
