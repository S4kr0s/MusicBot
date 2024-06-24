package listeners;

import core.commandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.time.format.DateTimeFormatter;

public class commandListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {

        if(event.getMessage().getChannelType().isGuild()){
            if(event.getMessage().getContentDisplay().startsWith(STATIC.PREFIX) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentDisplay().toLowerCase(), event));
            }
            System.out.println("[" + event.getMessage().getCreationTime().format(DateTimeFormatter.BASIC_ISO_DATE) + "] @ " + event.getGuild().getName() + " | in  " + event.getTextChannel().getName() + " | " + event.getMessage().getAuthor().getName() + " - " + event.getMessage().getContentDisplay());
        }else if (!event.getMessage().getChannelType().isGuild()){
            if(event.getMessage().getContentDisplay().startsWith(STATIC.PREFIX) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentDisplay().toLowerCase(), event));
            }
        }
    }

}
