package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ILootrun;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class LootrunChallengeFail extends WynnChatText implements ILootrun {

    public LootrunChallengeFail(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.challengeFail";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true));
        List<Text> desc = wrapLine(Text.translatable(translationKey + ".desc").setStyle(Style.EMPTY.withColor(Formatting.GRAY)), (int) (CHAT_HUD_WIDTH * 0.65));
        resultText = Text.empty()
                .append(centerAlign(title)).append("\n");

        desc.forEach(t -> resultText.append(centerAlign(t)).append("\n"));
        resultText.append("\n");

        List<List<Text>> textChunks = getTextChunks(getSiblings());
        textChunks.removeFirst();
        if(textChunks.getFirst().getFirst().getString().matches("§7.§7\\[\\+\\d+%.+")) {
            List<Text> effectsBody = textChunks.removeFirst();
            Style effectStyle = Style.EMPTY.withColor(Formatting.GRAY);
            Text damageInc = translateEffect(effectsBody.getFirst().getSiblings().getLast().getString()).setStyle(effectStyle);
            Text healthInc = translateEffect(effectsBody.getLast().getSiblings().getLast().getString()).setStyle(effectStyle);
            resultText.append(centerAlign(damageInc)).append("\n")
                    .append(centerAlign(healthInc).append("\n"));
        }
    }
}
