package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class KeyCollector extends WynnChatText {
    public KeyCollector(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "keyCollector";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)).append(": "));

        if(inputText.getSiblings().size() == 2) {
            resultText.append(newTranslate(parentKey + ".keyPassed").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        else {
            resultText.append(newTranslate(parentKey + ".needKey", getSibling(2)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
    }
}
