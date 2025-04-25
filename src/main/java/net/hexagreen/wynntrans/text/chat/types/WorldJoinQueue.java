package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class WorldJoinQueue extends WynnChatText implements ISpaceProvider {
    private final String worldChannel;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n +§b§lYou are in world ((?:NA|EU|AS)\\d+)!").matcher(text.getString()).find();
    }

    public WorldJoinQueue(Text text) {
        super(text);
        this.worldChannel = inputText.getString().replaceFirst("(?s).+((?:NA|EU|AS)\\d+).+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.worldJoinQueue";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t0 = Text.translatable(translationKey, worldChannel).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true));
        Text t1 = Text.translatable(translationKey + ".guide");

        resultText.append(centerAlign(t0)).append("\n").append(centerAlign(t1)).append("\n");
    }
}
