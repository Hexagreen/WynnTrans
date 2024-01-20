package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecretDiscovery extends WynnChatText implements ICenterAligned {
    private static final Pattern DISCOVERY_COUNT = Pattern.compile(" +(.+) \\[(\\d+)/(\\d+)]");
    private final String keyDiscoveryName;
    private final String valDiscoveryName;
    private final String keyDiscoveryArea;
    private final String valDiscoveryArea;
    private final String revealed;
    private final String total;

    public SecretDiscovery(Text text, Pattern regex) {
        super(text, regex);
        String discoveryName = getSibling(1).getSiblings().get(2).getString();
        this.keyDiscoveryName = rootKey + "discovery." + normalizeStringAreaName(discoveryName);
        this.valDiscoveryName = discoveryName;
        Matcher m = DISCOVERY_COUNT.matcher(getSibling(2).getString());
        boolean ignore = m.find();
        String areaName = m.group(1);
        this.keyDiscoveryArea = rootKey + "discovery.area." + normalizeStringAreaName(areaName);
        this.valDiscoveryArea = m.group(1);
        this.revealed = m.group(2);
        this.total = m.group(3);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "secretDiscovery";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        MutableText t0 = Text.empty().append(newTranslate(parentKey).setStyle(getSibling(1).getSiblings().get(1).getStyle()));
        if(WTS.checkTranslationExist(keyDiscoveryName, valDiscoveryName)) {
            t0.append(newTranslate(keyDiscoveryName).setStyle(getSibling(1).getSiblings().get(2).getStyle()))
                    .append(getSibling(1).getSiblings().get(3));
        }
        else {
            t0.append(getSibling(1).getSiblings().get(2))
                    .append(getSibling(1).getSiblings().get(3));
        }
        resultText.append(getCenterIndent(t0)).append(t0).append("\n");

        MutableText area = Text.empty();
        if(WTS.checkTranslationExist(keyDiscoveryArea, valDiscoveryArea)) {
            area.append(newTranslate(keyDiscoveryArea).setStyle(getSibling(2).getSiblings().get(1).getStyle())
                    .append(Text.of(" [" + revealed + "/" + total + "]")));
        }
        else {
            area.append(getSibling(2).getSiblings().get(1));
        }
        resultText.append(getCenterIndent(area)).append(area).append("\n\n");

        for(int i = 3; inputText.getSiblings().size() > i; i++) {
            MutableText line = Text.empty();
            String keyAreaLore = keyDiscoveryName + "_" + (i - 2);
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
