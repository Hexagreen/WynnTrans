package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class AreaEnter extends WynnChatText {
    private final Text areaText;

    public AreaEnter(Text text, Pattern regex) {
        super(text, regex);
        String areaName = matcher.group(1);
        String keyAreaName = rootKey + "area." + normalizeStringForKey(areaName);
        if(WTS.checkTranslationExist(keyAreaName, areaName)) {
            this.areaText = Text.translatable(keyAreaName).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
        else {
            this.areaText = Text.literal(areaName);
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.area.enter";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, areaText).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
