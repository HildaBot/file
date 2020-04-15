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
import java.util.Arrays;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.Util;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.commands.ChannelSubCommand;
import ch.jamiete.hilda.configuration.Configuration;
import ch.jamiete.hilda.file.FilePlugin;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class FileForbidCommand extends ChannelSubCommand {
    private final FilePlugin plugin;

    protected FileForbidCommand(final Hilda hilda, final ChannelSeniorCommand senior, final FilePlugin plugin) {
        super(hilda, senior);

        this.plugin = plugin;

        this.setName("forbid");
        this.setAliases(Arrays.asList("ban", "prohibit", "disallow", "block"));
        this.setDescription("Forbids a file type in the channel.");
    }

    @Override
    public void execute(final Message message, final String[] args, final String label) {
        final Configuration cfg = this.hilda.getConfigurationManager().getConfiguration(this.plugin, message.getTextChannel().getId());
        final JsonArray array = cfg.getArray("extensions");

        if (args.length == 0) {
            this.usage(message, "<extension...>");
            return;
        }

        final List<String> added = new ArrayList<>();
        final List<String> ignored = new ArrayList<>();

        for (String arg : args) {
            arg = arg.toLowerCase();

            if (arg.trim().length() == 0) {
                continue;
            }

            if (arg.startsWith(".")) {
                arg = arg.substring(1);
            }

            final JsonPrimitive primitive = new JsonPrimitive(arg);

            if (!array.contains(primitive)) {
                array.add(primitive);
                added.add(arg);
            } else {
                ignored.add(arg);
            }
        }

        cfg.get().add("extensions", array);
        cfg.save();

        final MessageBuilder mb = new MessageBuilder();

        mb.append("OK, I've ");

        if (added.size() > 0) {
            mb.append("added ").append(Util.getAsList(added)).append(" to the blacklist");
        }

        if (ignored.size() > 0 && added.size() > 0) {
            mb.append(" but I");
        }

        if (ignored.size() > 0) {
            mb.append(" ignored ").append(Util.getAsList(ignored)).append(" because ");
            mb.append(ignored.size() == 1 ? "it was" : "they were");
            mb.append(" already on the blacklist");
        }

        mb.append("!");

        mb.buildAll(MessageBuilder.SplitPolicy.SPACE).forEach(m -> this.reply(message, m));
    }

}
