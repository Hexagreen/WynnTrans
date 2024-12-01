package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CaveReward extends WynnDisplayText {
    private final String keyCaveName;
    private final String valCaveName;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().contains(" Rewards\n§7Interact to Open");
    }

    public CaveReward(Text text) {
        super(text);
        this.valCaveName = text.getString().replaceAll("§e§l", "").replaceAll(" Rewards\\n.+", "");
        this.keyCaveName = "wytr.cave." + normalizeStringForKey(valCaveName);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.caveReward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text cave;
        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            cave = Text.translatable(keyCaveName);
        }
        else {
            cave = Text.literal(valCaveName);
        }

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, cave).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true)));
    }
}
