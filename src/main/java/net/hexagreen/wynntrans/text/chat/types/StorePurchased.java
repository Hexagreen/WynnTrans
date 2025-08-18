package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.Deque;

public class StorePurchased extends WynnSystemText implements ISpaceProvider {
    public StorePurchased(Text text) {
        super(text, true);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.storePurchased";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text t0 = Text.translatable(translationKey).setStyle(getSibling(0).getSiblings().getLast().getStyle());
        Text t1 = Text.translatable(translationKey + ".desc").setStyle(getSibling(1).getSiblings().getLast().getStyle());
        Text t2 = Text.translatable("wytr.func.rewardReceived").setStyle(getSibling(3).getSiblings().getLast().getStyle());

        resultText = Text.empty();
        resultText.append(centerAlign(t0)).append("\n")
                .append(centerAlign(t1)).append("\n\n")
                .append(t2).append("\n");

        for(Text goods : getSiblings().subList(4, getSiblings().size())) {
            if(goods.getString().matches("^- .+")) {
                Deque<Text> siblings = new ArrayDeque<>(goods.getSiblings());
                MutableText line = Text.empty().setStyle(goods.getStyle());
                line.append(siblings.removeFirst());
                siblings.forEach(line::append);
                line.append("\n");
                resultText.append(line);
            }
            else return;
        }
    }
}
