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
        return text.getString().contains(" Rewards\\n§7Interact to Open");
    }

    public CaveReward(Text text) {
        super(text);
        this.valCaveName = text.getString().replaceAll("§e§l", "").replaceAll(" Rewards\\n.+", "");
        this.keyCaveName = "wytr.cave." + normalizeStringCaveName(valCaveName);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.caveReward";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text cave;
        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            cave = newTranslate(keyCaveName);
        }
        else {
            cave = Text.literal(valCaveName);
        }

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, cave).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true)));
    }
}
