package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldEventStart extends WynnSystemText {
    private static final Text CLICK2TRACK = Text.translatable("wytr.func.clickToTrack").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withUnderline(true));
    private final String keyWorldEventName;
    private final String valWorldEventName;
    private final Text leftTime;
    private final Text distance;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("The (.+) World Event starts in ((?:\\d+\\w ?)+)!").matcher(removeTextBox(text)).find();
    }

    public WorldEventStart(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("The (.+) World Event starts in ((?:\\d+\\w ?)+)!").matcher(inputText.getString().replaceAll("\n", ""));
        boolean ignore = matcher.find();
        this.valWorldEventName = matcher.group(1);
        this.keyWorldEventName = translationKey + normalizeStringForKey(valWorldEventName);
        this.leftTime = ITime.translateTime(matcher.group(2));
        this.distance = initDistanceText();
    }

    @Override
    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH / 2;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "worldEvent.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle(0));
        if(WTS.checkTranslationExist(keyWorldEventName, valWorldEventName)) {
            Text worldEvent = Text.translatable(keyWorldEventName);
            resultText.append(Text.translatable(rootKey + "func.worldEvent.start", worldEvent, leftTime, distance, CLICK2TRACK));
        }
        else {
            resultText.append(Text.translatable(rootKey + "func.worldEvent.start", valWorldEventName, leftTime, distance, CLICK2TRACK));
        }
    }

    private Text initDistanceText() {
        String distance = getSibling(1).getString().replaceAll("\\D", "");
        return Text.translatable(rootKey + "func.worldEvent.distance", distance).setStyle(GRAY);
    }
}
