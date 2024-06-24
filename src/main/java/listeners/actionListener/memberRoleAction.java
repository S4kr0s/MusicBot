package listeners.actionListener;

import db.MySQLAccess;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class memberRoleAction extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){

        MySQLAccess.connect();

        Member member = event.getMember();
        String userId = member.getUser().getId();

        if (MySQLAccess.getResult("SELECT * FROM sakros.discord_userdata WHERE userid='" + userId + "'") != null) {

        } else {
            MySQLAccess.update("INSERT INTO sakros.discord_userdata (warnpoints, kicks, mutes, bans, userid, notifcations) VALUES (0, 0, 0, 0, " + userId + ", true)");
        }

        MySQLAccess.close();
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        event.getGuild().getTextChannelById(STATIC.CHATTEXTCHANNEL).sendMessage(event.getMember().getNickname() + " hat uns leider verlassen.").queue();
    }
}