package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShamanTotem extends WynnDisplayText {
    private final boolean isHealingTotem;
    private Text timer;
    private Text owner;
    private MutableText icon;
    private String heal;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("§c\\d+s\\n§c\\+\\d+❤§7/s") || text.getString().matches(".+'s Totem\\n\\uE01F \\d+s");
    }

    public ShamanTotem(Text text) {
        super(text);
        this.isHealingTotem = text.getString().contains("❤");
        if(isHealingTotem) {
            String[] split = text.getString().split("\\n");
            this.timer = new Timer(Text.literal(split[0])).text();
            this.heal = split[1];
        }
        else {
            Matcher matcher = Pattern.compile("(.+)'s Totem\\n\\uE01F (\\d+s)").matcher(text.getString());
            if(matcher.find()) {
                this.owner = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
                this.timer = ITime.translateTime(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
                this.icon = text.getSiblings().getFirst().getSiblings().getFirst().getSiblings().getFirst().copy();
                icon.setStyle(icon.getStyle().withColor(Formatting.LIGHT_PURPLE));
            }
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.shamanTotem";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(isHealingTotem) {
            resultText = Text.empty();
            resultText.append(timer).append("\n").append(heal);
        }
        else {
            resultText = Text.translatable(translationKey, owner, icon, timer).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
    }
}
