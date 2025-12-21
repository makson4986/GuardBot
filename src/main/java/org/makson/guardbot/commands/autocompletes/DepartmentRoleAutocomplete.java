package org.makson.guardbot.commands.autocompletes;

import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Handler
public class DepartmentRoleAutocomplete {
    public static final String DEPARTMENT_ROLE_AUTOCOMPLETE_NAME = "DepartmentRole: role";

    @AutocompleteHandler(DEPARTMENT_ROLE_AUTOCOMPLETE_NAME)
    public Collection<String> onReportTypeAutocomplete(CommandAutoCompleteInteractionEvent event) {
        List<String> ROLES = List.of("Глава", "Участник");

        return ROLES.stream()
                .filter(type ->
                        type.toLowerCase().contains(
                                event.getFocusedOption().getValue().toLowerCase()
                        )
                )
                .limit(25)
                .collect(Collectors.toList());
    }
}
