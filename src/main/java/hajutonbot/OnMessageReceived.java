package hajutonbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@FunctionalInterface
public interface OnMessageReceived {
    public void receive(MessageReceivedEvent event);
}
