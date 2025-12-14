package org.makson.guardbot.commands.autocompletes;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Handler
public class ReportTypeAutocomplete extends ApplicationCommand {
    public static final String REPORT_TYPE_AUTOCOMPLETE_NAME = "ReportType: type";

    @AutocompleteHandler(REPORT_TYPE_AUTOCOMPLETE_NAME)
    public Collection<String> onReportTypeAutocomplete(CommandAutoCompleteInteractionEvent event) {
        List<String> TYPES = List.of(
                "RP", "PvP", "Помощь игроку", "Штраф", "Другое"
        );

        return TYPES.stream()
                .filter(type ->
                        type.toLowerCase().contains(
                                event.getFocusedOption().getValue().toLowerCase()
                        )
                )
                .limit(25)
                .collect(Collectors.toList());
    }
}
