package org.makson.guardbot.commamds.autocompletes;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Handler
public class DepartmentAutocomplete extends ApplicationCommand {
    private static final List<String> DEPARTMENTS = List.of(
            "Отряд 'Санитаров'", "Отряд 'Эпсилон'", "Отряд 'Дельта'"
    );

    public static final String DEPARTMENT_AUTOCOMPLETE_NAME = "Department: department";

    @AutocompleteHandler(DEPARTMENT_AUTOCOMPLETE_NAME)
    public Collection<String> onReportTypeAutocomplete(CommandAutoCompleteInteractionEvent event) {
        return DEPARTMENTS.stream()
                .filter(type ->
                        type.toLowerCase().contains(
                                event.getFocusedOption().getValue().toLowerCase()
                        )
                )
                .limit(25)
                .collect(Collectors.toList());
    }
}
