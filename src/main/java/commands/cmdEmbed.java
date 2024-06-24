package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class cmdEmbed implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        String input = Arrays.stream(args).skip(0).map(s -> " " + s).collect(Collectors.joining()).substring(1);

        if(event.getMessage().getAuthor().getId().equals(STATIC.SAKROSMEMBERID)){
            event.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.ORANGE)
                            .setAuthor(event.getMessage().getAuthor().getName(), event.getMessage().getAuthor().getAvatarUrl(), event.getMessage().getAuthor().getAvatarUrl())
                            .setDescription(input)
                            .build()
            ).queue();
        } else {
            event.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setAuthor(event.getMessage().getAuthor().getName(), event.getMessage().getAuthor().getAvatarUrl(), event.getMessage().getAuthor().getAvatarUrl())
                            .setDescription("Du hast leider keine Rechte auf diesen Befehl!")
                            .build()
            ).queue();
        }


        event.getMessage().delete().queue();


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
