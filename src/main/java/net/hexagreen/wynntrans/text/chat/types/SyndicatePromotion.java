package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Locale;

public class SyndicatePromotion extends WynnChatText implements ISpaceProvider {

    public static Text getTranslatedRankName(String rankNameString) {
        String valRank = rankNameString.replaceAll("§.", "").replaceAll(" I+$", "");
        String keyRank = "wytr.syndicate.rank." + valRank.toLowerCase(Locale.ENGLISH);
        String bar = rankNameString.replaceFirst(".+( I+)$", "$1");
        return Text.translatable(keyRank).setStyle(parseStyleCode(rankNameString)).append(bar);
    }

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
        Text rank = getTranslatedRankName(rankOrigin.getString());

        resultText = Text.empty();
        resultText.append(centerAlign(t0)).append("\n")
                .append(centerAlign(t1)).append("\n")
                .append(centerAlign(t2)).append("\n\n")
                .append(centerAlign(rank)).append("\n");

        if(getSiblings().size() >= 6) {
            for(int i = 5, size = getSiblings().size(); i < size; i++) {
                String promotionString = getSibling(i).getSiblings().getLast().getString();
                if(promotionString.matches("§7\\[.+]")) {
                    String valPro = promotionString.replaceFirst("§7\\[", "").replaceFirst("]", "");
                    String keyPro = translationKey + "reward." + DigestUtils.sha1Hex(valPro).substring(0, 4);
                    if(valPro.matches("^.+ Beacon$")) {
                        Text promotionReward = Text.literal("[").append(LootrunBeacon.getBeaconNameText(valPro)).append("]")
                                .setStyle(GRAY);
                        resultText.append(centerAlign(promotionReward)).append("\n");
                    }
                    else if(WTS.checkTranslationExist(keyPro, valPro)) {
                        Text promotionReward = Text.literal("[").append(Text.translatable(keyPro)).append("]")
                                .setStyle(GRAY);
                        resultText.append(centerAlign(promotionReward)).append("\n");
                    }
                    continue;
                }
                Text promotionReward = getSibling(i).getSiblings().get(1);
                resultText.append(centerAlign(promotionReward)).append("\n");
            }
        }
    }
}
