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
package ch.jamiete.hilda.file.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.Util;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.file.FilePlugin;
import net.dv8tion.jda.core.entities.Message;

public class FileListCommand extends ChannelSubCommand {
    private final FilePlugin plugin;

    protected FileListCommand(final Hilda hilda, final ChannelSeniorCommand senior, final FilePlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("list");
        this.setDescription("Lists the forbidden extensions.");
    }

    @Override
    public void execute(final Message message, final String[] args, final String label) {
        final Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getTextChannel().getId());
        final JsonArray array = cfg.getArray("extensions");

        if (array.size() == 0) {
            this.reply(message, "No extensions are blacklisted in this channel.");
            return;
        }

        final List<String> blacklisted = new ArrayList<String>();
        final Iterator<JsonElement> iterator = array.iterator();

        while (iterator.hasNext()) {
            final JsonElement element = iterator.next();
            blacklisted.add(element.getAsString());
        }

        this.reply(message, "In this channel users cannot send the following extensions: " + Util.getAsList(blacklisted));
    }

}
