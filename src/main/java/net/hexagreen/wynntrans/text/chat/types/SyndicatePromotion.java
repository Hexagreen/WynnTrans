package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.Locale;

public class SyndicatePromotion extends WynnChatText implements ISpaceProvider {

    public SyndicatePromotion(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "syndicate.rank.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text t0 = new SimpleText(getSibling(0).getSiblings().getLast()).text();
        Text t1 = new SimpleText(getSibling(1).getSiblings().getLast()).text();
        Text t2 = new SimpleText(getSibling(2).getSiblings().getLast()).text();

        Text rankOrigin = getSibling(4).getSiblings().getLast();
        String valRank = rankOrigin.getString().replaceAll("§.", "").replaceAll(" I+$", "");
        String keyRank = translationKey + valRank.toLowerCase(Locale.ENGLISH);
        String bar = rankOrigin.getString().replaceFirst(".+( I+)$", "$1");
        Text rank = Text.translatable(keyRank).setStyle(parseStyleCode(rankOrigin.getString())).append(bar);

        resultText = Text.empty();
        resultText.append(getCenterIndent(t0).append(t0)).append("\n")
                .append(getCenterIndent(t1).append(t1)).append("\n")
                .append(getCenterIndent(t2).append(t2)).append("\n\n")
                .append(getCenterIndent(rank).append(rank)).append("\n");

        if(getSiblings().size() >= 6) {
            for(int i = 5, size = getSiblings().size(); i < size; i++) {
                Text promotionReward = getSibling(i).getSiblings().get(1);
                resultText.append(getCenterIndent(promotionReward).append(promotionReward)).append("\n");
            }
        }
    }
}
