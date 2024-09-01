package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveLootChest extends WynnDisplayText {
    private static final Pattern regex = Pattern.compile("§c§lSLAY!§r§7 Defeat (§f\\d+/\\d+) ?(.+)$");
    private final Text chest;
    private final Style color;
    private final String progress;
    private final String valTarget;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^§.Locked §.Loot Chest(.|\\n)+");
    }

    public CaveLootChest(Text text) {
        super(text);
        this.chest = new LootChest(
                Text.literal(text.getString().replaceAll("^..Locked ", "").replaceAll("\\n.+$", ""))
        ).text();
        this.color = parseStyleCode(text.getString().substring(0, 2));
        Matcher m = regex.matcher(text.getString());
        if(m.find()) {
            this.progress = m.group(1);
            this.valTarget = m.group(2);
        }
        else throw new TextTranslationFailException("CaveLootChest.class");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.lockedChest";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, chest).setStyle(color)).append("\n");
        if(valTarget.equals("§7 Mobs")) {
            resultText.append(newTranslate(parentKey + ".slayMob", progress).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
        else {
            MutableText target;
            String keyTarget = "wytr.mobName." + normalizeStringNPCName(valTarget);
            if(WTS.checkTranslationExist(keyTarget, valTarget)) {
                target = newTranslate(keyTarget).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            }
            else {
                target = Text.literal(valTarget).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            }
            resultText.append(newTranslate(parentKey + ".slayTarget", progress, target).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }
}
