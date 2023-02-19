package ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;

@Data
@AllArgsConstructor
public class ReplyKeyboardRemove implements KeyboardMarkup {
    @JsonProperty("remove_keyboard")
    private boolean removeKeyboard;
}
