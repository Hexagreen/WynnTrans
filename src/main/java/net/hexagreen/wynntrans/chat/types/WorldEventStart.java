package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class WorldEventStart extends WynnSystemText {
    private static final Text CLICK2TRACK = Text.translatable("wytr.func.clickToTrack")
            .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withUnderline(true));
    private final String keyWorldEventName;
    private final String valWorldEventName;
    private final String leftTime;
    private final Text distance;


    public WorldEventStart(Text text, Pattern regex) {
        super(text, regex);
        this.valWorldEventName = matcher.group(1);
        this.keyWorldEventName = parentKey + normalizeStringWorldEventName(valWorldEventName);
        this.leftTime = matcher.group(2);
        this.distance = initDistanceText();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "worldEvent.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException {
        resultText = Text.empty()
                .append(header).setStyle(getStyle(0));
        if(WTS.checkTranslationExist(keyWorldEventName, valWorldEventName)) {
            Text worldEvent = newTranslate(keyWorldEventName);
            resultText.append(newTranslateWithSplit(rootKey + "func.worldEvent.start", worldEvent, leftTime, distance, CLICK2TRACK));
        }
        else {
            resultText.append(newTranslateWithSplit(rootKey + "func.worldEvent.start", valWorldEventName, leftTime, distance, CLICK2TRACK));
        }
    }

    private Text initDistanceText() {
        String distance = getSibling(1).getString().replaceAll("\\D", "");
        return newTranslate(rootKey + "func.worldEvent.distance", distance).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }
}
