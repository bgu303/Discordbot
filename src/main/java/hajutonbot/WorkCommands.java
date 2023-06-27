package hajutonbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkCommands {

    public static void addListeners(List<OnMessageReceived> list) {
        list.add((e) -> {
            String message = e.getMessage().getContentRaw();
            MessageChannel workChannel = e.getGuild().getTextChannelsByName("botti", true).get(0);
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

            if (e.getChannel().getId().equals("1122833104030158949")) {
                if (message.equals("!start")) {
                    Runnable task = () -> {
                        HttpClient httpClient = HttpClient.newHttpClient();

                        HttpRequest request = null;
                        try {
                            request = HttpRequest.newBuilder()
                                    .uri(new URI("https://www.courtlistener.com/docket/19857399/feed/"))
                                    .GET()
                                    .build();
                        } catch (URISyntaxException error) {
                            error.printStackTrace();
                        }

                        HttpResponse<String> response = null;
                        try {
                            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                        } catch (IOException | InterruptedException error) {
                            error.printStackTrace();
                        }
                        try {
                            String rBodyString = response.body();

                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                            DocumentBuilder db = dbf.newDocumentBuilder();
                            Document doc = db.parse(new InputSource(new StringReader(rBodyString)));

                            NodeList publishTags = doc.getElementsByTagName("published");
                            NodeList titleTags = doc.getElementsByTagName("summary");

                            for (int i = 0; i < publishTags.getLength(); i++) {
                                Node node = publishTags.item(i);
                                String textContent = node.getTextContent();
                                if (!textContent.equals("2023-06-13T00:00:00-07:00") && !textContent.equals("2023-06-26T00:00:00-07:00")) {
                                    workChannel.sendMessage("Uutta paskaa! @everyone " + titleTags.item(i).getTextContent() + " ").queue();
                                    executorService.shutdown();
                                    return;
                                }
                            }
                            System.out.println("Latest news: " + publishTags.item(0).getTextContent());
                        } catch (Exception error) {
                            System.out.println("Error handling the response body: " + error);
                            workChannel.sendMessage("Error handling the response body: " + error).queue();
                        }
                    };
                    executorService.scheduleAtFixedRate(task, 0, 15, TimeUnit.SECONDS);
                }
            }
        });
    }
}
