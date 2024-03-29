package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Pattern;

public class CrateGet extends WynnChatText {
    public CrateGet(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "crateReward";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        if(getSibling(0).getContent().equals(TextContent.EMPTY)) {
            int nextIndexItemname = inputText.getSiblings().size() - 3;
            resultText.append(getSibling(1))
                    .append(newTranslate(parentKey + "_1").setStyle(getStyle(2)));
            for(int i = 3; nextIndexItemname > i; i++){
                resultText.append(getSibling(i));
            }
            resultText.append(newTranslate(parentKey + "_2").setStyle(getStyle(nextIndexItemname)))
                    .append(getSibling(nextIndexItemname + 1));
        }
        else {
            int nextIndexItemname = inputText.getSiblings().size() - 3;
            resultText.append(Text.literal(matcher.group(1)).setStyle(getStyle(0)))
                    .append(newTranslate(parentKey + "_1").setStyle(getStyle(0)));
            for(int i = 1; nextIndexItemname > i; i++){
                resultText.append(getSibling(i));
            }
            resultText.append(newTranslate(parentKey + "_2").setStyle(getStyle(nextIndexItemname)))
                    .append(getSibling(nextIndexItemname + 1));
        }
    }
}
