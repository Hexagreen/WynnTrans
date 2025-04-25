package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class WorldEvent extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(0x00BDBF);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 2) return false;
        return texts.getFirst().getString().equals("§3§lWorld Event:");
    }

    public WorldEvent(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.worldEvent";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> texts = new ArrayList<>(getSiblings());
        texts.removeFirst();
        Text head = Text.translatable(translationKey).setStyle(style.withBold(true));
        resultText.append(head);

        extractUniqueLines(texts).stream()
                .map(this::mergeTextStyleSide)
                .map(this::translateLine)
                .forEach(resultText::append);
    }

    private Text translateLine(Text text) {
        if(!text.getString().matches("^- .+")) throw new TextTranslationFailException("WorldEvent");
        MutableText result = Text.literal("- ").setStyle(style);

        List<Text> siblings = text.getSiblings();
        return switch(siblings.get(1).getString()) {
            case "Event Completed" -> result.append(Text.translatable("wytr.func.worldEvent.complete")
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
            case "Time Limit: " ->
                    result.append(Text.translatable(translationKey + ".time", ITime.translateTime(siblings.getLast()))
                            .setStyle(GRAY));
            case "Waves Remaining: " -> result.append(Text.translatable(translationKey + ".wave", siblings.getLast())
                    .setStyle(GRAY));
            case "Targets Remaining: " ->
                    result.append(Text.translatable(translationKey + ".target", siblings.getLast())
                            .setStyle(GRAY));
            default -> text;
        };
    }
}
