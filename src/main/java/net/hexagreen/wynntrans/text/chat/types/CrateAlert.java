package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CrateAlert extends WynnChatText {
    private final Text crateName;
    private final Text command = Text.literal("/crates").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crates")));

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^Use /crates to open your (.+ Crate)!$").matcher(text.getString()).find();
    }

    public CrateAlert(Text text) {
        super(text, Pattern.compile("^Use /crates to open your (.+ Crate)!$"));
        this.crateName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.crateAlert";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, command, crateName).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
