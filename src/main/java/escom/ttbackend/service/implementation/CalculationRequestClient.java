package escom.ttbackend.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.MediaType;
import okhttp3.MediaType;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import okhttp3.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculationRequestClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    class GzipRequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink;
                    gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

    public String disisit() throws IOException {
        URL url = new URL("https://nutre.in/ajax/automatico");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        http.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
        http.setRequestProperty("Accept-Language", "es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7,es-419;q=0.6,pt;q=0.5");
        http.setRequestProperty("Connection", "keep-alive");
        http.setRequestProperty("Content-Length", "287");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.setRequestProperty("Cookie", "nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab; nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab");
        http.setRequestProperty("Host", "nutre.in");
        http.setRequestProperty("Origin", "https://nutre.in");
        http.setRequestProperty("Referer", "https://nutre.in/");
        http.setRequestProperty("Sec-Fetch-Dest", "empty");
        http.setRequestProperty("Sec-Fetch-Mode", "cors");
        http.setRequestProperty("Sec-Fetch-Site", "same-origin");
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        http.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        http.setRequestProperty("sec-ch-ua", " \"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"");
        http.setRequestProperty("sec-ch-ua-mobile", "?0");
        http.setRequestProperty("sec-ch-ua-platform", "macOS");

        String data = "eqverduras=&eqfrutas=&eqcereales1=&eqcereales2=&eqleguminosas=&eqaoa1=&eqaoa2=&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=&eqleche3=&eqleche4=&eqaceites1=&eqaceites2=&eqazucar1=&eqazucar2=&totalKcal=2000&porcentajeHco=60&porcentajeLip=25&porcentajePro=15&t_valid=663b935e6b2e3903c98827a35f7fbfa5";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        // After sending the data
        InputStream inputStream = http.getInputStream();

// Read response headers
        Map<String, List<String>> headers;
        headers = http.getHeaderFields();
        System.out.println("Response Headers:");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            if (headerName != null) {
                System.out.print(headerName + ": ");
            }
            for (String value : headerValues) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

// Check if the response is gzipped
        String contentEncoding = http.getHeaderField("Content-Encoding");
        InputStream decompressedInputStream = inputStream;
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
            decompressedInputStream = new GZIPInputStream(inputStream);
        }

// Read response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(decompressedInputStream));
        StringBuilder responseContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();

// Close input streams
        decompressedInputStream.close();
        inputStream.close();

// Get response content as string
        String responseHTML = responseContent.toString();
        System.out.println("Response HTML:\n" + responseHTML);

//        System.out.println(http.getResponseCode() + " " + http.getResponseMessage() + " " + http.toString() + " "+ stream.toString());
        log.info("response code -> {}", http.getResponseCode());
        log.info("response message -> {}", http.getResponseMessage());
        log.info("response -> {}", http);
        log.info("dis -> {}", http.getContent());
        http.disconnect();


        return "iiii";
    }

    public String yawei(){
//        Unirest.(0, 0);
        HttpResponse<String> response = Unirest.post("https://nutre.in/ajax/automatico")
                .header("Accept", " application/json, text/javascript, */*; q=0.01")
                .header("Accept-Encoding", " gzip, deflate, br, zstd")
                .header("Accept-Language", " es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7,es-419;q=0.6,pt;q=0.5")
                .header("Connection", " keep-alive")
                .header("Content-Length", " 287")
                .header("Content-Type", " application/x-www-form-urlencoded; charset=UTF-8")
                .header("Cookie", " nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab; nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab")
                .header("Host", " nutre.in")
                .header("Origin", " https://nutre.in")
                .header("Referer", " https://nutre.in/")
                .header("Sec-Fetch-Dest", " empty")
                .header("Sec-Fetch-Mode", " cors")
                .header("Sec-Fetch-Site", " same-origin")
                .header("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .header("X-Requested-With", " XMLHttpRequest")
                .header("sec-ch-ua", " \"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                .header("sec-ch-ua-mobile", " ?0")
                .header("sec-ch-ua-platform", " \"macOS\"")
                .body("eqverduras=&eqfrutas=&eqcereales1=&eqcereales2=&eqleguminosas=&eqaoa1=&eqaoa2=&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=&eqleche3=&eqleche4=&eqaceites1=&eqaceites2=&eqazucar1=&eqazucar2=&totalKcal=2000&porcentajeHco=60&porcentajeLip=25&porcentajePro=15&t_valid=663b935e6b2e3903c98827a35f7fbfa5")
                .asString();

        log.info(":( -> {}", response.getBody());
        return "yawei";
//        return response.getBody().toString();
    }

    public String orasisi() throws IOException {

//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new GzipRequestInterceptor())
//                .build();




        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse(" application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "eqverduras=&eqfrutas=&eqcereales1=&eqcereales2=&eqleguminosas=&eqaoa1=&eqaoa2=&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=&eqleche3=&eqleche4=&eqaceites1=&eqaceites2=&eqazucar1=&eqazucar2=&totalKcal=2000&porcentajeHco=60&porcentajeLip=25&porcentajePro=15&t_valid=663b935e6b2e3903c98827a35f7fbfa5");
        Request request = new Request.Builder()
                .url("https://nutre.in/ajax/automatico")
                .method("POST", body)
                .addHeader("Accept", " application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Encoding", " gzip, deflate, br, zstd")
                .addHeader("Accept-Language", " es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7,es-419;q=0.6,pt;q=0.5")
                .addHeader("Connection", " keep-alive")
                .addHeader("Content-Length", " 287")
                .addHeader("Content-Type", " application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cookie", " nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab; nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab")
                .addHeader("Host", " nutre.in")
                .addHeader("Origin", " https://nutre.in")
                .addHeader("Referer", " https://nutre.in/")
                .addHeader("Sec-Fetch-Dest", " empty")
                .addHeader("Sec-Fetch-Mode", " cors")
                .addHeader("Sec-Fetch-Site", " same-origin")
                .addHeader("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .addHeader("X-Requested-With", " XMLHttpRequest")
                .addHeader("sec-ch-ua", " \"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                .addHeader("sec-ch-ua-mobile", " ?0")
                .addHeader("sec-ch-ua-platform", " \"macOS\"")
                .build();
        Response response = client.newCall(request).execute();
        log.info("Response Headers -> {}", response.headers().toString());
        log.info("orasisi -> {}", response);
//        log.info("orasisi body -> {}", response.body().string());
//        System.out.println(response.body().string());
        return (response.body().toString());

    }

    public String aiuda() throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream;
        URL url = new URL("https://nutre.in/ajax/automatico");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
        connection.setRequestProperty("Accept-Language", "es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7,es-419;q=0.6,pt;q=0.5");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Length", "300");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Cookie", "nu_valid=0976931bd574d373b9feea9916cb389f; nutre_in=fdecc9da30ae31c56cab1ee49e035c3a04e2c1a9");
        connection.setRequestProperty("Host", "nutre.in");
        connection.setRequestProperty("Origin", "https://nutre.in");
        connection.setRequestProperty("Referer", "https://nutre.in/");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");

        OutputStream stream = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        writer.write("eqverduras=4&eqfrutas=4&eqcereales1=10&eqcereales2=&eqleguminosas=1.5&eqaoa1=&eqaoa2=3&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=1.5&eqleche3=&eqleche4=&eqaceites1=8&eqaceites2=&eqazucar1=2&eqazucar2=&totalKcal=2000&porcentajeHco=60&porcentajeLip=25&porcentajePro=15&t_valid=0976931bd574d373b9feea9916cb389f");
        writer.flush();
        writer.close();
        stream.close();
        connection.connect();

        inputStream = connection.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(buffer))!=-1){
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] responseData = byteArrayOutputStream.toByteArray();
        return new String(responseData, StandardCharsets.UTF_8);
    }

    public ResponseEntity<String> sendAndReceive(DietRequestBody dietRequestBody){
        try {
            MultiValueMap<String, String> formData = convertToMultiValueMap(dietRequestBody);

            log.info("aaaaa -> {}", formData);
            String url = "https://nutre.in/ajax/automatico";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json, text/javascript, */*; q=0.01");
            headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
            headers.set("Accept-Language", "es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7,es-419;q=0.6,pt;q=0.5");
            headers.set("Connection", "keep-alive");
            headers.setContentLength(300);
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Cookie", " nu_valid=663b935e6b2e3903c98827a35f7fbfa5; nutre_in=3ccc4dd21dec3b7a1097810abd8a07fa8c87deab");
            headers.set("Host", "nutre.in");
            headers.set("Origin", "https://nutre.in");
            headers.set("Referer", "https://nutre.in/");
            headers.set("Sec-Fetch-Dest", "empty");
            headers.set("Sec-Fetch-Mode", "cors");
            headers.set("Sec-Fetch-Site", "same-origin");
            headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
            headers.set("X-Requested-With", "XMLHttpRequest");
            headers.set("sec-ch-ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"");
            headers.set("sec-ch-ua-mobile", "?0");
            headers.set("sec-ch-ua-platform", "\"macOS\"");

//        HttpEntity<DietRequestBody> request = new HttpEntity<>(dietRequestBody, headers);
            String jaja ="eqverduras=4&eqfrutas=4&eqcereales1=10&eqcereales2=&eqleguminosas=1.5&eqaoa1=&eqaoa2=3&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=1.5&eqleche3=&eqleche4=&eqaceites1=8&eqaceites2=&eqazucar1=2&eqazucar2=&totalKcal=2000&porcentajeHco=60&porcentajeLip=25&porcentajePro=15&t_valid=663b935e6b2e3903c98827a35f7fbfa5";
            HttpEntity<String> requestEntity = new HttpEntity<>(jaja, headers);
//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            log.info("sadasd -> {}", requestEntity);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            String responseBody = responseEntity.getBody();
            return responseEntity;
//            return objectMapper.readValue(responseBody, DietResponseBody.class);
        }
        catch (HttpClientErrorException e){
            throw new RestClientException("Error occurred during the request: " + e.getResponseBodyAsString(), e);
        }catch (Exception e) {
            // Handle other exceptions
            throw new RestClientException("Error occurred while processing the response", e);
        }

//        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, DietResponseBody.class).getBody();
    }

    public MultiValueMap<String, String> convertToMultiValueMap(DietRequestBody requestBody) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("eqverduras", requestBody.getEqverduras());
        map.add("eqfrutas", requestBody.getEqfrutas());
        map.add("eqcereales1", requestBody.getEqcereales1());
        map.add("eqcereales2", requestBody.getEqcereales2());
        map.add("eqleguminosas", requestBody.getEqleguminosas());
        map.add("eqaoa1", requestBody.getEqaoa1());
        map.add("eqaoa2", requestBody.getEqaoa2());
        map.add("eqaoa3", requestBody.getEqaoa3());
        map.add("eqaoa4", requestBody.getEqaoa4());
        map.add("eqleche1", requestBody.getEqleche1());
        map.add("eqleche2", requestBody.getEqleche2());
        map.add("eqleche3", requestBody.getEqleche3());
        map.add("eqleche4", requestBody.getEqleche4());
        map.add("eqaceites1", requestBody.getEqaceites1());
        map.add("eqaceites2", requestBody.getEqaceites2());
        map.add("eqazucar1", requestBody.getEqazucar1());
        map.add("eqazucar2", requestBody.getEqazucar2());
        map.add("totalKcal", requestBody.getTotalKcal());
        map.add("porcentajeHco", requestBody.getPorcentajeHco());
        map.add("porcentajeLip", requestBody.getPorcentajeLip());
        map.add("porcentajePro", requestBody.getPorcentajePro());
        map.add("t_valid", requestBody.getT_valid());
        return map;
    }
}
