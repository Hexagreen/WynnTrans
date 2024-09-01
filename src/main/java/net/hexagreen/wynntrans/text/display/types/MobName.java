package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MobName extends WynnDisplayText {
    private final String keyMobName;
    private final String valMobName;
    private final Style styleMobName;

    public static boolean typeChecker(Text text){
        try {
            Text target = text.getSiblings().get(2).getSiblings().getFirst();
            if(target.getString().contains("\uE00B\uE015 ")) {
                if(target.getStyle().getFont().equals(Identifier.of("minecraft:banner/pill"))) {
                    return true;
                }
            }
        }
        catch(Exception ignore) {}
        return false;
    }

    public MobName(Text text) {
        super(text);
        this.valMobName = getContentString(0);
        this.keyMobName = parentKey + normalizeStringNPCName(valMobName);
        this.styleMobName = getStyle(0);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "mobName.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        if(WTS.checkTranslationExist(keyMobName, valMobName)) {
            resultText.append(newTranslate(keyMobName).setStyle(styleMobName));
        }
        else {
            resultText.append(getSibling(0));
        }
        for(int i = 1; i < inputText.getSiblings().size(); i++) {
            resultText.append(getSibling(i));
        }
    }
}
