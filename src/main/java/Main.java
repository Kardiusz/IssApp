import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

public class Main {

    private static final String ISS_API_LOCATION = "http://api.open-notify.org/iss-now.json";

    public static <jsonNode> void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        int choice;

        do {
            System.out.println("1. Pobierz położenie ISS");
            System.out.println("2. Zakończ aplikację");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // sprawdż położenie ISS


                    // Stworzenie HTTP klienta, request i wysyłanie requestu z rządaniem odpowiedzi
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = (HttpRequest) HttpRequest.newBuilder().
                            uri(URI.create(ISS_API_LOCATION)).
                            build();
                    final HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Tworzymy sobie mappera, żeby wciągnąć wartośc z JSONa, czyli odpowiedzi z zewnętrznego serwisu
                    ObjectMapper objectMapper = new ObjectMapper();
                    final JsonNode jsonNode = objectMapper.readTree(send.body());

                    // Wyciągamy timestamp jako long
                    long timestamp = jsonNode.at("/timestamp").asLong();

                    Instant instant = Instant.ofEpochSecond(timestamp);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                    // Wyciągamy szerokość i długość
                    final double lat = jsonNode.at("/iss_position/latitude").asDouble();
                    final double lon = jsonNode.at("/iss_position/longitude").asDouble();

                    System.out.println("Dnia " + localDateTime + " ISS " + " jest w miejscu szerokość " + lat + " długośc " + lon);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("iss_location.csv", true))){
                        StringBuilder line = new StringBuilder();
                        line.append("date").append(",").append(localDateTime).append(",")
                                .append("lat").append(",").append("lat").append(",").append("lon").append(",").append("lon").append("\n");
                        writer.write(line.toString());
                    }


                    break;


                case 2:
                    System.out.println("Zamykamy appkę");

                default:
                    System.out.println("Nie ma takiej komendy");
                    break;
            }


        } while (choice != 2);
        scanner.close();
    }
}
