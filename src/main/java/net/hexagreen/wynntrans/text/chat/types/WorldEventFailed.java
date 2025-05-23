package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WorldEventFailed extends WynnSystemText implements ISpaceProvider {
    private final Text eventName;
    private final String keyFailGuide;

    public WorldEventFailed(Text text) {
        super(text, true);
        this.eventName = initEventName(getSibling(0));
        this.keyFailGuide = initKeyGuide(getSibling(5));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.worldEvent.fail";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle(0));
        resultText.append(Text.translatable("wytr.func.worldEvent.failedOne", eventName));

        Text t2 = Text.empty().append(getSibling(2).getSiblings().get(1)).append(getSibling(2).getSiblings().get(2));
        Text t3 = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        Text t5 = Text.translatable(keyFailGuide).setStyle(Style.EMPTY.withColor(0xAEB8BF));

        resultText.append("\n")
                .append(getSystemTextCenterIndent(t2).append(t2)).append("\n")
                .append(getSystemTextCenterIndent(t3).append(t3)).append("\n")
                .append("\n")
                .append(getSystemTextCenterIndent(t5).append(t5)).append("\n")
                .append("\n");
    }

    private Text initEventName(Text text) {
        String valName = text.getString().replaceAll("You have failed ", "").replaceAll("\\.$", "");
        String keyName = rootKey + "worldEvent." + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return Text.translatable(keyName);
        }
        else {
            return Text.literal(valName);
        }
    }

    private String initKeyGuide(Text text) {
        String content = text.getString();
        if(content.contains("Everyone in your group has died!")) {
            return translationKey + ".eliminated";
        }
        if(content.contains("The time limit has expired!")) {
            return translationKey + ".timeOut";
        }
        return null;
    }
}
