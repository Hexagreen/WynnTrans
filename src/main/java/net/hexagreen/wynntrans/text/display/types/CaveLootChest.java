package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveLootChest extends WynnDisplayText {
    private static final Pattern slay = Pattern.compile("§c§lSLAY!!?§r ?§7 ?Defeat (an?|§f\\d+/\\d+|)(?:§.| )*(.+)$");
    private static final Pattern puzzle = Pattern.compile("§c§lPUZZLE!§r §7.+");
    private final Text chest;
    private final Style color;
    private String progress = "";
    private String valTarget = "";
    private String other = "";

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^§.Locked §.Loot Chest(.|\\n)+");
    }

    public CaveLootChest(Text text) {
        super(text);
        this.chest = new LootChest(Text.literal(text.getString().replaceFirst("^..Locked ", "").replaceFirst("\\n.+$", ""))).text();
        this.color = parseStyleCode(text.getString().substring(0, 2));
        Matcher m = slay.matcher(text.getString());
        if(m.find()) {
            this.progress = m.group(1);
            this.valTarget = m.group(2);
        }
        else if(puzzle.matcher(text.getString()).find()) {
            this.other = text.getString().split("\\n")[1];
        }
        else throw new TextTranslationFailException("CaveLootChest.class");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.lockedChest";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, chest).setStyle(color)).append("\n");
        if(!other.isBlank()) {
            String key = translationKey + ".obj." + DigestUtils.sha1Hex(other).substring(0, 4);
            if(WTS.checkTranslationExist(key, other)) {
                resultText.append(key);
                return;
            }
        }

        if(valTarget.contains("Mobs")) {
            resultText.append(Text.translatable(translationKey + ".slayMob", progress).setStyle(GRAY));
        }
        else {
            MutableText target;
            String keyTarget = "wytr.mobName." + normalizeStringForKey(valTarget);
            if(WTS.checkTranslationExist(keyTarget, valTarget)) {
                target = Text.translatable(keyTarget).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            }
            else {
                target = Text.literal(valTarget).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            }
            if(!progress.contains("a")) {
                resultText.append(Text.translatable(translationKey + ".slayTarget", target, progress).setStyle(GRAY));
            }
            else {
                resultText.append(Text.translatable(translationKey + ".slayHead", target).setStyle(GRAY));
            }
        }
    }
}
