package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdHelp implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.green)
                        .setTitle("Übersicht der Bot-Befehle:", "https://www.youtube.com/c/Sakros")
                        .addField("`!hilfe` oder `!help`", "Damit kannst du diesen Text einsehen.", false)
                        .addBlankField(false)
                        .addField("Weitere Befehle","Bald verfügbar.",false)
                        .build()

        ).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
