//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package hajutonbot;

import hajutonbot.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class DiscordBot {
    private final Dotenv config = Dotenv.configure().load();
    private final ShardManager shardManager;

    public DiscordBot() throws LoginException {
        String token = this.config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("With ur mom's saggy tits"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        this.shardManager = builder.build();
        this.shardManager.addEventListener(new EventListener());

        String line;
        do {
            Scanner scanner = new Scanner(System.in);
            line = scanner.nextLine();
        } while(!line.equals("close"));

        this.shardManager.shutdown();
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public Dotenv getConfig() {
        return this.config;
    }

    public static void main(String[] args) {
        try {
            DiscordBot discordBot = new DiscordBot();
        } catch (LoginException e) {
            System.out.println("ERROR");
        }

    }
}
