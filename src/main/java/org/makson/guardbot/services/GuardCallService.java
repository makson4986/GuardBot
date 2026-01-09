package org.makson.guardbot.services;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.makson.guardbot.exceptions.ModalFieldIsEmptyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardCallService {
    public String checkModalField(ModalMapping field) {
        if (field == null) {
            throw new ModalFieldIsEmptyException("Field is empty");
        }

        return field.getAsString();
    }

    public String checkSelectMenu(ModalMapping field) {
        if (field == null) {
            throw new ModalFieldIsEmptyException("No option selected");
        }

        return field.getAsStringList().getFirst();
    }

    public Modal createGuardCallModal() {
        TextInput usernameTextInput = TextInput.create("username", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Mayzito")
                .setRequired(true)
                .build();

        TextInput coordinatesTextInput = TextInput.create("coordinates", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Верхний мир/Незер/Край -100,-100")
                .setRequired(true)
                .build();

        StringSelectMenu reasonCall = StringSelectMenu.create("reason")
                .addOption("RP", "RP")
                .addOption("PvP", "PvP")
                .addOption("CoreProtect", "CoreProtect")
                .addOption("Другое", "Другое")
                .build();

        TextInput reasonTextInput = TextInput.create("description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Причина")
                .setRequired(true)
                .build();

        return Modal.create("guard_call_modal", "Форма вызова гвардии")
                .addComponents(
                        List.of(
                                Label.of("Ник", usernameTextInput),
                                Label.of("Координаты, куда нужно прибыть гвардии", coordinatesTextInput),
                                Label.of("Причина вызова ", reasonCall),
                                Label.of("Кратко опишите причину вызова", reasonTextInput)
                        )
                )
                .build();
    }
}
