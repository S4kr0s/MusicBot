package core;

import commands.*;
import db.MySQLAccess;
import listeners.*;
import listeners.actionListener.communityRoleAction;
import listeners.actionListener.giveawayReactionAction;
import listeners.actionListener.memberRoleAction;
import listeners.logs.messageLog;
import listeners.logs.serverLog;
import listeners.logs.voiceLog;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import util.SECRETS;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDABuilder builder;

    public static void main(String[] Args) throws Exception{
        //Registierung
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(SECRETS.TOKEN);
        builder.setAutoReconnect(true);
        builder.setAudioEnabled(true);
        builder.setStatus(OnlineStatus.IDLE);
        //0builder.setGame(Game.of(Game.GameType.DEFAULT,STATIC.VERSION + " | " + STATIC.PREFIX + "help"));
        builder.setGame(Game.of(Game.GameType.WATCHING, "Sakros", "https://www.twitch.tv/sakros))"));

        //Listener
        builder.addEventListener(new memberRoleAction());
        builder.addEventListener(new giveawayReactionAction());
        builder.addEventListener(new readyListener());
        builder.addEventListener(new commandListener());
        builder.addEventListener(new messageLog());
        builder.addEventListener(new voiceLog());
        builder.addEventListener(new serverLog());
        builder.addEventListener(new communityRoleAction());

        //Commands
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("hilfe", new cmdHelp());
        commandHandler.commands.put("music", new Music());
        commandHandler.commands.put("musik", new Music());
        commandHandler.commands.put("embed", new cmdEmbed());

        //MySQL
        MySQLAccess.connect();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


