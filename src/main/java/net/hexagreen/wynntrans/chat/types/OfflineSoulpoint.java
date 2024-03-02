package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class OfflineSoulpoint extends WynnChatText {
    private final String restored;

    public OfflineSoulpoint(Text text, Pattern regex) {
        super(text, regex);
        this.restored = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "offlineSP";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey, restored).setStyle(getStyle(0)));
    }
}
