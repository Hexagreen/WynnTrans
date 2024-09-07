package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class RewardChest extends WynnDisplayText {
    private final Text lastTime;
    private final Style timeStyle;

    public static boolean typeChecker(Text text) {
        return text.getString().contains("\uE011\uE004\uE016\uE000\uE011\uE003 \uE002\uE007\uE004\uE012\uE013\uDB00\uDC02");
    }

    public RewardChest(Text text) {
        super(text);
        Text time = getSibling(0).getSiblings().get(2).getSiblings().getFirst();
        this.timeStyle = time.getStyle();
        this.lastTime = ITime.translateTime(time.getString()).setStyle(timeStyle);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.rewardChest";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle(0));
        resultText.append(getSibling(0).getSiblings().getFirst()).append("\n");
        resultText.append(newTranslate(parentKey, lastTime).setStyle(timeStyle));
    }
}
