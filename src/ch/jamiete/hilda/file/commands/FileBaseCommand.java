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

import java.util.Arrays;
import ch.jamiete.hilda.Hilda;
import ch.jamiete.hilda.commands.ChannelSeniorCommand;
import ch.jamiete.hilda.file.FilePlugin;
import net.dv8tion.jda.core.Permission;

public class FileBaseCommand extends ChannelSeniorCommand {

    public FileBaseCommand(final Hilda hilda, final FilePlugin plugin) {
        super(hilda);

        this.setName("file");
        this.setAliases(Arrays.asList(new String[] { "files" }));
        this.setDescription("Manages the file blocker.");
        this.setMinimumPermission(Permission.MANAGE_CHANNEL);

        this.registerSubcommand(new FileForbidCommand(hilda, this, plugin));
        this.registerSubcommand(new FileListCommand(hilda, this, plugin));
        this.registerSubcommand(new FilePermitCommand(hilda, this, plugin));
    }

}
