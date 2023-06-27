package hajutonbot;

import java.util.List;

public class WorkCommands {

    public static void addListeners(List<OnMessageReceived> list) {
        list.add((m) -> {
            if (m.getChannel().getId().equals("asddsaasd")) {
                m.getChannel().sendMessage(("moi")).queue();
            }
        });
    }
}
