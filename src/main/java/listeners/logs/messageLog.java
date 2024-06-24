package listeners.logs;
import core.Main;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class messageLog extends ListenerAdapter{

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if(event.getAuthor() != event.getJDA().getSelfUser()){
            event.getGuild().getTextChannelsByName(STATIC.MESSAGELOGCHANNEL, true).get(0).sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl())
                            .addField("#" + event.getMessage().getTextChannel().getName(), "`" + event.getMessage().getContentDisplay() + "`", false)
                            .setFooter(event.getMessage().getCreationTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")), null)
                            .build()
            ).queue();
        }
    }

    public void onGuildMessageUpdate(GuildMessageUpdateEvent event){
        if(event.getAuthor() != event.getJDA().getSelfUser()){
            event.getGuild().getTextChannelsByName(STATIC.MESSAGELOGCHANNEL, true).get(0).sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.YELLOW)
                            .setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl())
                            .addField("#" + event.getMessage().getTextChannel().getName(), "`" + event.getMessage().getContentDisplay() + "`", false)
                            .setFooter(event.getMessage().getCreationTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")), null)
                            .build()
            ).queue();
        }
    }


    /*
    public void onGuildMessageDelete(GuildMessageDeleteEvent event){
        event.getGuild().getTextChannelsByName(STATIC.MESSAGELOGCHANNEL, true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl())
                        .setDescription(event.getMessage().getContentDisplay())
                        .setFooter("Diese Nachricht wurde gelöscht am " + event.getMessage().getEditedTime().format(DateTimeFormatter.ISO_DATE_TIME), null)
                        .build()
                ).queue();

    }
     */
}
