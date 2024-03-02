package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class AreaDiscovery extends WynnChatText implements ICenterAligned {
    private final String keyAreaName;
    private final String valAreaName;
    private final boolean shortForm;

    public AreaDiscovery(Text text, Pattern regex) {
        super(text, regex);
        String areaName = getSibling(1).getSiblings().get(2).getString();
        this.keyAreaName = rootKey + "area." + normalizeStringAreaName(areaName);
        this.valAreaName = areaName;
        this.shortForm = text.getSiblings().size() == 2;
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "areaDiscovered";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        MutableText t0 = Text.empty().append(newTranslate(parentKey).setStyle(getSibling(1).getSiblings().get(1).getStyle()));
        if(WTS.checkTranslationExist(keyAreaName, valAreaName)) {
            t0.append(newTranslate(keyAreaName).setStyle(getSibling(1).getSiblings().get(2).getStyle()))
                    .append(getSibling(1).getSiblings().get(3));
        }
        else {
            t0.append(getSibling(1).getSiblings().get(2))
                    .append(getSibling(1).getSiblings().get(3));
        }
        resultText.append(getCenterIndent(t0)).append(t0).append("\n");

        if(shortForm) return;
        else resultText.append("\n");

        for(int i = 2; inputText.getSiblings().size() > i; i++) {
            MutableText line = Text.empty();
            String keyAreaLore = keyAreaName + "." + (i - 1);
            String valAreaLore = getSibling(i).getSiblings().get(1).getString();
            if(WTS.checkTranslationExist(keyAreaLore, valAreaLore)) {
                line.append(newTranslate(keyAreaLore).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            else {
                line.append(getSibling(i).getSiblings().get(1));
            }
            resultText.append(getCenterIndent(line)).append(line).append("\n");
        }
    }
}
