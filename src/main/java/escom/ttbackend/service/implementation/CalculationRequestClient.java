package escom.ttbackend.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import escom.ttbackend.presentation.dto.calculation.DietResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculationRequestClient {

    public DietResponseBody sendAndReceive(DietRequestBody request) throws IOException {

        log.info("enters send and receive in calc request client");

        String nu_valid = "a3b33be29a4be101d4fb67aa141b2eb3";
        String nutre_in = "8acbb2c1f58bf19b0f3e8ebb1c49d58b3963f526";
        HttpURLConnection http = getHttpURLConnection(nu_valid, nutre_in);

        byte[] out = getBytes(request, nu_valid);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        // After sending the data
        InputStream inputStream = http.getInputStream();

// Read response headers
//        Map<String, List<String>> headers;
//        headers = http.getHeaderFields();
//        System.out.println("Response Headers:");
//        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
//            String headerName = entry.getKey();
//            List<String> headerValues = entry.getValue();
//            if (headerName != null) {
//                System.out.print(headerName + ": ");
//            }
//            for (String value : headerValues) {
//                System.out.print(value + " ");
//            }
//            System.out.println();
//        }

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

//        trying to parse it as object DietResponseBody
//        DietResponseBody dietResponseBody = responseContent.;

// Get response content as string
        String responseHTML = responseContent.toString();
//        System.out.println("Response HTML:\n" + responseHTML);

        // Deserialize JSON response to POJO
        ObjectMapper objectMapper;
        objectMapper = new ObjectMapper();
        DietResponseBody responseObj = objectMapper.readValue(responseHTML, DietResponseBody.class);

        log.info("Mapped Response Object Received -> {}", responseObj);

        http.disconnect();


        return responseObj;
    }

    private static byte[] getBytes(DietRequestBody request, String nu_valid) {
        String data = "eqverduras=&eqfrutas=&eqcereales1=&eqcereales2=&eqleguminosas=&eqaoa1=&eqaoa2=&eqaoa3=&eqaoa4=&eqleche1=&eqleche2=&eqleche3=&eqleche4=&eqaceites1=&eqaceites2=&eqazucar1=&eqazucar2=&totalKcal="
                + request.getTotalKcal()
                + "&porcentajeHco="
                + request.getPorcentajeHco()
                + "&porcentajeLip="
                + request.getPorcentajeLip()
                + "&porcentajePro="
                + request.getPorcentajePro()
                + "&t_valid="
                + nu_valid;

        return data.getBytes(StandardCharsets.UTF_8);
    }

    private static HttpURLConnection getHttpURLConnection(String nu_valid, String nutre_in) throws IOException {
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
        http.setRequestProperty("Cookie", "nu_valid=" + nu_valid + "; nutre_in=" + nutre_in + "; nu_valid=" + nu_valid + "; nutre_in=" + nutre_in);
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
        return http;
    }


}
