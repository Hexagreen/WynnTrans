package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SpeedBoost extends WynnChatText {
    private final boolean selfMode;
    private final Text playerName;
    private final Text duration;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().isEmpty()) return false;
        if(text.getSiblings().getLast().getString().equals(" speed boost.")) return true;
        if(text.getSiblings().size() < 4) return false;
        return text.getSiblings().get(3).getString().equals(" speed boost.");
    }

    public SpeedBoost(Text text) {
        super(text);
        this.selfMode = getSiblings().size() == 2;
        this.playerName = getSibling(0);
        this.duration = ITime.translateTime(getContentString(2).replace("+", ""));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.speedBoost";
    }

    @Override
    protected void build() {
        if(selfMode) {
            Text time = Text.literal("+").append(ITime.translateTime(getContentString(0).replace("+", "")))
                    .setStyle(Style.EMPTY.withColor(Formatting.AQUA));
            resultText = Text.translatable(translationKey + ".self", time).setStyle(getStyle());
        }

        Text buffTime = Text.literal("+").setStyle(Style.EMPTY.withColor(Formatting.AQUA)).append(duration);

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName, buffTime).setStyle(getStyle()));
    }
}
