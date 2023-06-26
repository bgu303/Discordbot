package hajutonbot;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.TextNode;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class EventListener extends ListenerAdapter {
    private static int count = 0;
    private final Dotenv config = Dotenv.configure().load();
    String FACEIT_TOKEN = this.config.get("FACEIT_TOKEN");

    public EventListener() {
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String checkChannel = event.getMessage().getChannel().getId();
        String message = event.getMessage().getContentRaw();
        MessageChannel channel;
        if (event.getMessage().getGuild().getName().equals("Reenit")) {

            // if (event.getChannel().getId().equals("1030802808372482170") && event.getAuthor().getId().equals("164331054814068736")) {
            // event.getMessage().delete().queue();
            // }

            channel = event.getGuild().getTextChannelsByName("reenit", true).get(0);
            if (event.getChannel().getName().equals("general") || event.getChannel().getName().equals("reenit")) {
                if (message.equals("!pop")) {
                    channel.sendMessage("@everyone https://popflash.site/-/reenit/scrim").queue();
                }

                if (message.equals("!pop 0")) {
                    channel.sendMessage("FULL!!!!!! FULLLLL!!!!!!!!!!!!").queue();
                    return;
                }

                if (message.equals("!kannustus")) {
                    channel.sendMessage("https://cdn.discordapp.com/attachments/754101531799715950/1029097708708966400/unknown.png").queue();
                }

                if (message.startsWith("!pop ")) {
                    String subString = message.substring(5);
                    channel.sendMessage("@everyone https://popflash.site/-/reenit/scrim +" + subString.replaceAll("[^0-9]", "")).queue();
                }

                if (message.equals("!psykoterapia")) {
                    channel.sendMessage("https://www.mielenterveystalo.fi/aikuiset/Tietopankki/Hoitomuotoja/Pages/Psykoterapia.aspx").queue();
                }
            }
        }

        if (event.getMessage().getChannel().getName().equals("turle-bot") && message.equals("!clear") && event.getAuthor().getId().equals("300290102108618764")) {
            event.getChannel().getIterableHistory().takeAsync(500).thenAccept(event.getChannel()::purgeMessages);
        }

        if (checkChannel.equals("1027151644963655731")) {
            try {
                channel = event.getGuild().getTextChannelsByName("bot-channel", true).get(0);
                if (message.equals("!clear") && event.getAuthor().getId().equals("300290102108618764")) {
                    event.getChannel().getIterableHistory().takeAsync(500).thenAccept(event.getChannel()::purgeMessages);
                }

                String rightAnswer;
                if (message.equals("aaa")) {
                    rightAnswer = event.getMessage().getAuthor().getId();
                    System.out.println(rightAnswer);
                    channel.sendMessage("moi <@" + rightAnswer + ">").queue();
                }


                if (message.equals("!nimet")) {
                    String output = "";
                    int line = 0;
                    try {
                        File myObj = new File("nimet.txt");
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            line++;
                            String data = myReader.nextLine();
                            if (line % 200 == 0) {
                                channel.sendMessage(output).queue();
                                line = 0;
                                output = "";
                            } else {
                                output += data + "\n";
                            }
                        }
                        channel.sendMessage(output).queue();
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }

                if (message.equals("!moodle")) {
                    channel.sendMessage("https://hhmoodle.haaga-helia.fi/my/").queue();
                }

                if (message.startsWith("!find ")) {
                    HttpClient client = HttpClient.newHttpClient();
                    String nickName = message.substring(6);
                    String playerId = "";
                    int elo = 0;
                    int skillLevel = 0;
                    String country = "";
                    String faceitURL = "";
                    double kd = 0.0;
                    int numberOfGames = 0;
                    double winrate = 0.0;

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("https://open.faceit.com/data/v4/players?nickname=" + nickName))
                            .header("Authorization", "Bearer " + FACEIT_TOKEN)
                            .GET()
                            .build();
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode() == 200) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response.body());

                            elo = rootNode.get("games").get("csgo").get("faceit_elo").asInt();
                            skillLevel = rootNode.get("games").get("csgo").get("skill_level").asInt();
                            country = rootNode.get("country").asText();
                            faceitURL = "https://www.faceit.com/fi/players/" + nickName;
                            playerId = rootNode.get("player_id").asText();
                        } else {
                            channel.sendMessage("Player not found!").queue();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HttpRequest request2 = HttpRequest.newBuilder()
                            .uri(new URI("https://open.faceit.com/data/v4/players/" + playerId + "/stats/csgo"))
                            .header("Authorization", "Bearer " + FACEIT_TOKEN)
                            .GET()
                            .build();
                    try {
                        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode() == 200) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response.body());
                            kd = rootNode.get("lifetime").get("Average K/D Ratio").asDouble();
                            numberOfGames = rootNode.get("lifetime").get("Matches").asInt();
                            winrate = rootNode.get("lifetime").get("Win Rate %").asDouble();

                            channel.sendMessage("Country: " + country.toUpperCase() + "\nFaceit Level: " + skillLevel +
                                    "\nFaceit ELO: " + elo + "\nK/D: " + kd + "\nMatches: " + numberOfGames + "\nWinRate: " + winrate + "%" +
                                    "\nProfile: " + faceitURL).queue();
                        } else {
                            channel.sendMessage("Player not found!").queue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (message.startsWith("!scout ")) {
                    DecimalFormat df = new DecimalFormat("0.0");
                    double vertigoGames = 0;
                    double vertigoWins = 0;

                    double overpassGames = 0;
                    double overpassWins = 0;

                    double dust2Games = 0;
                    double dust2Wins = 0;

                    double anubisGames = 0;
                    double anubisWins = 0;

                    double ancientGames = 0;
                    double ancientWins = 0;

                    double mirageGames = 0;
                    double mirageWins = 0;

                    double infernoGames = 0;
                    double infernoWins = 0;

                    double nukeGames = 0;
                    double nukeWins = 0;


                    HttpClient client = HttpClient.newHttpClient();
                    String gameId = message.substring(7);
                    List<String> enemyIdList = new ArrayList<String>();
                    List<String> enemyNameList = new ArrayList<String>();
                    String textResponse = "";

                    HttpRequest getEnemyPlayerIds = HttpRequest.newBuilder()
                            .uri(new URI("https://open.faceit.com/data/v4/matches/" + gameId))
                            .header("Authorization", "Bearer " + FACEIT_TOKEN)
                            .GET()
                            .build();

                    try {
                        HttpResponse<String> response = client.send(getEnemyPlayerIds, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode() == 200) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response.body());
                            JsonNode rosterNode1 = rootNode.get("teams").get("faction1").get("roster");
                            JsonNode rosterNode2 = rootNode.get("teams").get("faction2").get("roster");

                            for (JsonNode playerNode : rosterNode2) {
                                if (playerNode.get("player_id").equals(new TextNode("8055c304-d278-4737-bbdb-fb45d08eac9b"))) {
                                    enemyIdList.clear();
                                    enemyNameList.clear();
                                    break;
                                } else {
                                    enemyIdList.add(playerNode.get("player_id").asText());
                                    enemyNameList.add(playerNode.get("nickname").asText());
                                }
                            }
                            if (enemyIdList.isEmpty()) {
                                for (JsonNode playerNode : rosterNode1) {
                                    enemyIdList.add(playerNode.get("player_id").asText());
                                    enemyNameList.add(playerNode.get("nickname").asText());
                                }
                            }
                        } else {
                            channel.sendMessage("Something went wrong in Match fetching part.").queue();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < enemyIdList.size(); i++) {
                        HttpRequest getEnemyPlayerStats = HttpRequest.newBuilder()
                                .uri(new URI("https://open.faceit.com/data/v4/players/" + enemyIdList.get(i).toString() + "/stats/csgo"))
                                .header("Authorization", "Bearer " + FACEIT_TOKEN)
                                .GET()
                                .build();
                        try {
                            HttpResponse<String> response2 = client.send(getEnemyPlayerStats, HttpResponse.BodyHandlers.ofString());
                            if (response2.statusCode() == 200) {
                                ObjectMapper objectMapper2 = new ObjectMapper();
                                JsonNode rootNode2 = objectMapper2.readTree(response2.body());
                                JsonNode mapNodes = rootNode2.get("segments");
                                // textResponse += enemyNameList.get(i).toString() + " All time KD: " + rootNode2.get("lifetime").get("Average K/D Ratio").asText() + "\n";

                                for (JsonNode map : mapNodes) {
                                    if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_vertigo"))) {
                                        vertigoGames += map.get("stats").get("Matches").asInt();
                                        vertigoWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_anubis"))) {
                                        anubisGames += map.get("stats").get("Matches").asInt();
                                        anubisWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_ancient"))) {
                                        ancientGames += map.get("stats").get("Matches").asInt();
                                        ancientWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_overpass"))) {
                                        overpassGames += map.get("stats").get("Matches").asInt();
                                        overpassWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_dust2"))) {
                                        dust2Games += map.get("stats").get("Matches").asInt();
                                        dust2Wins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_nuke"))) {
                                        nukeGames += map.get("stats").get("Matches").asInt();
                                        nukeWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_inferno"))) {
                                        infernoGames += map.get("stats").get("Matches").asInt();
                                        infernoWins += map.get("stats").get("Wins").asInt();
                                    } else if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_mirage"))) {
                                        mirageGames += map.get("stats").get("Matches").asInt();
                                        mirageWins += map.get("stats").get("Wins").asInt();
                                    }

                                    /* if (map.get("mode").equals(new TextNode("5v5")) && map.get("label").equals(new TextNode("de_anubis")) ||
                                            map.get("label").equals(new TextNode("de_ancient")) ||
                                            map.get("label").equals(new TextNode("de_dust2")) ||
                                            map.get("label").equals(new TextNode("de_overpass")) ||
                                            map.get("label").equals(new TextNode("de_nuke")) ||
                                            map.get("label").equals(new TextNode("de_vertigo")) ||
                                            map.get("label").equals(new TextNode("de_inferno")) ||
                                            map.get("label").equals(new TextNode("de_mirage"))) {
                                        textResponse += (map.get("label").asText() + " \t \t \t" + map.get("stats").get("Win Rate %").asText() + "% WR") + "\n";
                                    } */
                                }
                            } else {
                                channel.sendMessage("Something went wrong in Enemy Thingy part lol XD").queue();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String anubisAvg = df.format(anubisWins / anubisGames * 100).replace(",", ".");
                    String overpassAvg = df.format(overpassWins / overpassGames * 100).replace(",", ".");
                    String nukeAvg = df.format(nukeWins / nukeGames * 100).replace(",", ".");
                    String ancientAvg = df.format(ancientWins / ancientGames * 100).replace(",", ".");
                    String dust2Avg = df.format(dust2Wins / dust2Games * 100).replace(",", ".");
                    String infernoAvg = df.format(infernoWins / infernoGames * 100).replace(",", ".");
                    String mirageAvg = df.format(mirageWins / mirageGames * 100).replace(",", ".");
                    String vertigoAvg = df.format(vertigoWins / vertigoGames * 100).replace(",", ".");

                    TreeMap<String, MapData> tMap = new TreeMap<>();
                    tMap.put(anubisAvg, new MapData("Anubis", anubisGames));
                    tMap.put(overpassAvg, new MapData("Overpass", overpassGames));
                    tMap.put(nukeAvg, new MapData("Nuke", nukeGames));
                    tMap.put(ancientAvg, new MapData("Ancient", ancientGames));
                    tMap.put(dust2Avg, new MapData("Dust2", dust2Games));
                    tMap.put(infernoAvg, new MapData("Inferno", infernoGames));
                    tMap.put(mirageAvg, new MapData("Mirage", mirageGames));
                    tMap.put(vertigoAvg, new MapData("Vertigo", vertigoGames));

                    for (String key : tMap.keySet()) {
                        MapData value = tMap.get(key);
                        textResponse += value + " AVG win%: " + key + " %\n";
                    }

                    channel.sendMessage("`" + textResponse + "`").queue();
                }

                if (message.startsWith("!flip ")) {
                    rightAnswer = HelperMethods.HeadOrTails();
                    if (message.length() > 6) {
                        if (!message.substring(6).equals("tails") && !message.substring(6).equals("heads")) {
                            channel.sendMessage("options: heads or tails").queue();
                            return;
                        }

                        if (message.substring(6).equals(rightAnswer)) {
                            channel.sendMessage(rightAnswer + "! You won!").queue();
                        } else if (!message.substring(6).equals(rightAnswer)) {
                            channel.sendMessage(rightAnswer + "! You lost!").queue();
                        }
                    }
                }

                if (message.equals("!disobey")) {
                    channel.sendMessage("https://disobey.fi/2023/").queue();
                }

                String decryptedMessage;
                if (message.startsWith("!encrypt ")) {
                    rightAnswer = message.substring(9);
                    decryptedMessage = HelperMethods.enCrypt(rightAnswer);
                    channel.sendMessage("MESSAGE BEING ENCRYPTED: " + decryptedMessage).queue();
                }

                if (message.startsWith("!decrypt ")) {
                    rightAnswer = message.substring(9);
                    decryptedMessage = HelperMethods.deCrypt(rightAnswer);
                    channel.sendMessage("MESSAGE BEING DECRYPTED: " + decryptedMessage).queue();
                }

                if (message.startsWith("!google ")) {
                    String subString = message.substring(8);
                    channel.sendMessage("https://www.google.com/search?q=" + subString.replaceAll(" ", "+")).queue();
                }

                if (message.equals("!commands") || message.equals("!command")) {
                    channel.sendMessage("Current commands:\n!google\n!encrypt\n!decrypt\n!clear (for admins >:))\n!moodle\n!disobey\n!find\n!scout").queue();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (event.getChannel().getId().equals("1122833104030158949") && event.getAuthor().getId().equals("300290102108618764")) {
            MessageChannel workChannel = event.getGuild().getTextChannelsByName("botti", true).get(0);

            if (message.equals("!start")) {
                HttpClient httpClient = HttpClient.newHttpClient();

                HttpRequest request = null;
                try {
                    request = HttpRequest.newBuilder()
                            .uri(new URI("https://www.courtlistener.com/docket/19857399/feed/"))
                            .GET()
                            .build();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                HttpResponse<String> response = null;
                try {
                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                //workChannel.sendMessage("response sent").queue();
                try {
                    String rBodyString = response.body();

                    System.out.println(rBodyString);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new InputSource(new StringReader(rBodyString)));
                    Element root = doc.getDocumentElement();

                    NodeList childElements = root.getChildNodes();

                    System.out.println(childElements);

                } catch (Exception e) {
                    System.out.println("Error handling the response body: " + e);
                    workChannel.sendMessage("Error handling the response body: " + e).queue();
                }
            }
        }
    }

    public void onShutdown(@NotNull ShutdownEvent event) {
        File file = new File("count.txt");

        try {
            if (file.exists()) {
                file.delete();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            fw.write(Integer.toString(count));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        String emoji = event.getReaction().getEmoji().toString();
        if (!event.getChannel().getName().equals("turle-bot")) {
            if (event.getGuild().getName().equals("Reenit")) {
                MessageChannel channel = event.getGuild().getTextChannelsByName("turle-bot", true).get(0);
                User user = event.getUser();
                if (emoji.equals("UnicodeEmoji(U+1f422)")) {
                    count++;
                    String tagUser = user.getAsTag().substring(0, user.getAsTag().length() - 5);
                    channel.sendMessage(tagUser + " löysi turlen " + Emoji.fromUnicode("U+1f422").getAsReactionCode() + " kanavalla " + event.getChannel().getName() + "\nTURLE count: " + count).queue();
                }

                if (emoji.equals("CustomEmoji:flle(1027606407605932132)") && event.getGuild().getName().equals("Reenit")) {
                    channel.sendMessage("FÄLLE " + Emoji.fromUnicode("U+1f60d").getAsReactionCode()).queue();
                }
            }

        }

        if (event.getChannel().getId().equals("1027151644963655731")) {
            MessageChannel channel = event.getGuild().getTextChannelsByName("bot-channel", true).get(0);
            if (emoji.equals("UnicodeEmoji(U+1f4af)")) {
                channel.sendMessage("wohoo").queue();
            }
        }

    }

    static {
        File file = new File("count.txt");

        try {
            if (file.exists()) {
                List<String> lines = Files.readAllLines(file.toPath());
                if (lines.size() == 1) {
                    count = Integer.parseInt((String) lines.get(0));
                    System.out.println(count);
                }
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
