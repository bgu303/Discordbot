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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class EventListener extends ListenerAdapter {
    private static int count = 0;
    private final Dotenv config = Dotenv.configure().load();
    String FACEIT_TOKEN = this.config.get("FACEIT_TOKEN");

    public static List<OnMessageReceived> eventListeners = new ArrayList<>();

    public EventListener() {
        BinanceCommands.addListeners(eventListeners);
        WorkCommands.addListeners(eventListeners);
        TaskForceHajutonCommands.addListeners(eventListeners);
        TitislaperCommands.addListeners(eventListeners);
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getMessage().getAuthor().getId().equals("1026877644286996521")) {
            return;
        }

        for (OnMessageReceived listener : eventListeners) {
            listener.receive(event);
        }

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
