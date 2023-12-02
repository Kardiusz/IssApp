import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static final String ISS_API_LOCATION = "http://api.open-notify.org/iss-now.json";

    public static void main(String[] args) throws IOException, InterruptedException {

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

                    System.out.println(send.body());
                    break;


                case 2:
                    System.out.println("Zamykamy appkę");
            }


        } while (choice != 2);
        scanner.close();
    }
}
