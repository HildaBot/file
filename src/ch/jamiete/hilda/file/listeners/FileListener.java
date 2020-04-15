/*******************************************************************************
 * Copyright 2017 jamietech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ch.jamiete.hilda.file.listeners;

import java.util.Iterator;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.events.EventHandler;
import ch.jamiete.hilda.file.FilePlugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class FileListener {
    private final FilePlugin plugin;

    public FileListener(final FilePlugin plugin) {
        this.plugin = plugin;
    }

    public void delete(final Message message) {
        message.delete().reason("I deleted this message because it breached a file prohibition.").queue(s -> {
            // Nothing
        }, f -> {
            // Nothing
        });
        message.getTextChannel().sendMessage(":no_good: I've deleted your message, " + message.getAuthor().getAsMention() + ". You aren't allowed to post that type of file!").queue();
    }

    @EventHandler
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        if (event.getMessage().getAttachments().isEmpty() || !event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE)) {
            return;
        }

        final Configuration cfg = this.plugin.getHilda().getConfigurationManager().getConfiguration(this.plugin, event.getMessage().getTextChannel().getId());
        final JsonArray array = cfg.getArray("extensions");

        if (array.size() == 0) {
            return;
        }

        final List<Message.Attachment> attachments = event.getMessage().getAttachments();

        for (JsonElement element : array) {
            for (final Message.Attachment attachment : attachments) {
                if (!attachment.getFileName().contains(".") && element.getAsString().equals("$BLANK")) {
                    this.delete(event.getMessage());
                    return;
                }

                if (attachment.getFileName().toLowerCase().endsWith("." + element.getAsString().toLowerCase())) {
                    this.delete(event.getMessage());
                    return;
                }
            }
        }
    }

}
