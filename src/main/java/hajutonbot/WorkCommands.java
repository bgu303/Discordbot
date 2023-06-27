package hajutonbot;

import java.util.List;

public class WorkCommands {

    public static void addListeners(List<OnMessageReceived> list) {
        list.add((m) -> {
            if (m.getChannel().getId().equals("1111320685239869483")) {
                m.getChannel().sendMessage(("moi")).queue();
            }
        });
    }
}
