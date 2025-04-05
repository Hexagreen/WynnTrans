package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class TradeFinish extends WynnSystemText {
    private final boolean buyingMode;
    private final Text item;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("Finished (?:buying|selling) ").matcher(text.getString()).find();
    }

    public TradeFinish(Text text) {
        super(text);
        this.buyingMode = text.getString().contains("Finished buying ");
        this.item = parseItem();
    }

    @Override
    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH / 2;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.tradeFinish.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle());
        if(buyingMode) {
            resultText.append(Text.translatable(translationKey + "buy", item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return;
        }
        resultText.append(Text.translatable(translationKey + "sell", item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }

    private Text parseItem() {
        MutableText item = Text.empty();
        if(getSiblings().size() == 2) {
            String content = getContentString(1).replaceAll("\\n|À\\.", "");
            item.append(Text.literal(content).setStyle(getStyle(1)));
        }
        else {
            String content = getContentString(0).replaceAll("^.+ §5|\\n|À\\.", "");
            item.append(Text.literal(content));
        }
        return item;
    }
}
