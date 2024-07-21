package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecretDiscovery extends WynnChatText implements ICenterAligned {
    private static final Pattern DISCOVERY_NAME = Pattern.compile(": §b(.+)§d( \\(\\+\\d+ XP\\))");
    private static final Pattern DISCOVERY_COUNT = Pattern.compile(" +§f(.+) \\[(\\d+)/(\\d+)]");
    private final String keyDiscoveryName;
    private final String valDiscoveryName;
    private final String keyDiscoveryArea;
    private final String valDiscoveryArea;
    private final String experience;
    private final String revealed;
    private final String total;

    public SecretDiscovery(Text text, Pattern regex) {
        super(text, regex);
        Matcher discoveryNameMatcher = DISCOVERY_NAME.matcher(getSibling(1).getString());
        String discoveryName = discoveryNameMatcher.find() ? discoveryNameMatcher.group(1) : "UNKNOWN DISCOVERY";
        this.keyDiscoveryName = rootKey + "discovery." + normalizeStringAreaName(discoveryName);
        this.valDiscoveryName = discoveryName;
        this.experience = discoveryNameMatcher.group(2);
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

        MutableText t0 = Text.empty().append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
        if(WTS.checkTranslationExist(keyDiscoveryName, valDiscoveryName)) {
            t0.append(newTranslate(keyDiscoveryName).setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                    .append(Text.literal(experience).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        else {
            t0.append(Text.literal(valDiscoveryName).setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                    .append(Text.literal(experience).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }
        resultText.append(getCenterIndent(t0)).append(t0).append("\n");

        MutableText area = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        if(WTS.checkTranslationExist(keyDiscoveryArea, valDiscoveryArea)) {
            area.append(newTranslate(keyDiscoveryArea))
                    .append(Text.of(" [" + revealed + "/" + total + "]"));
        }
        else {
            area.append(getSibling(2).getString().replaceAll("^ *", ""));
        }
        resultText.append(getCenterIndent(area)).append(area).append("\n\n");

        for(int i = 3; inputText.getSiblings().size() > i; i++) {
            MutableText line = Text.empty();
            String keyAreaLore = keyDiscoveryName + "_" + (i - 2);
            String valAreaLore = getSibling(i).getString().replaceAll(" *§.", "");
            if(WTS.checkTranslationExist(keyAreaLore, valAreaLore)) {
                line.append(newTranslate(keyAreaLore).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            else {
                line.append(getSibling(i).getString().replaceAll("^ *", ""));
            }
            resultText.append(getCenterIndent(line)).append(line).append("\n");
        }
    }
}
