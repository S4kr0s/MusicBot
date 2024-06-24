package listeners.logs;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;


public class serverLog extends ListenerAdapter {

    public void CheckChannel(MessageReceivedEvent event){
        if (event.getGuild().getTextChannelsByName(STATIC.VOICELOGCHANNEL, true).get(0).getTopic().isEmpty() && event.getGuild().getCategoriesByName(STATIC.LOGCATEGORY, true).get(0) != null){
            event.getGuild().getTextChannelsByName(STATIC.VOICELOGCHANNEL, true).get(0).getManager().setTopic(STATIC.LOGCHANNELTOPIC).queue();
        }else if (event.getGuild().getTextChannelsByName(STATIC.VOICELOGCHANNEL, true).get(0) == null && event.getGuild().getCategoriesByName(STATIC.LOGCATEGORY, true).get(0) != null){
            event.getGuild().getCategoriesByName(STATIC.LOGCATEGORY, true).get(0).createTextChannel(STATIC.VOICELOGCHANNEL).setName(STATIC.VOICELOGCHANNEL).queue();
        }else if (event.getGuild().getTextChannelsByName(STATIC.VOICELOGCHANNEL, true).get(0) == null && event.getGuild().getCategoriesByName(STATIC.LOGCATEGORY, true).get(0) == null){

        }
            //event.getGuild().
    }
}
