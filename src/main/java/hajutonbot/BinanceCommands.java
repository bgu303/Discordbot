package hajutonbot;

import java.util.List;

public class BinanceCommands {

    public static void addListeners(List<OnMessageReceived> list) {
        list.add((m) -> {
            if (m.getAuthor().getId().equals("asdsdsd")) {
                System.out.println("homo " + m.getAuthor());
            }
        });
    }

}
