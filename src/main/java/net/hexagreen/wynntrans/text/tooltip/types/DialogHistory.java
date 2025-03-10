package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.chat.types.Narration;
import net.hexagreen.wynntrans.text.chat.types.NpcDialog;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogHistory extends WynnTooltipText {
    private static final Pattern npcDialogPattern = Pattern.compile("^\\[\\d+/\\d+]");
    private static final Pattern pageCounterPattern = Pattern.compile("Page (\\d+) of (\\d+)");

    public static boolean typeChecker(List<Text> texts) {
        return texts.getFirst().getString().equals("Dialogue History");
    }

    public DialogHistory(List<Text> texts) {
        super(colorCodedToStyledBatch(texts));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.dialogHistory";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText.append(Text.translatable(translationKey))
                .append(" ");
        for(Text text : parseDialog()) {
            resultText.append(text);
        }
        resultText.append(" ")
                .append(getPageCounter())
                .append(" ")
                .append(Text.translatable("wytr.tooltip.nextPage"))
                .append(Text.translatable("wytr.tooltip.previousPage"));
    }

    private Text getPageCounter() {
        Matcher m = pageCounterPattern.matcher(getSiblings().get(getSiblings().size() - 4).getString());
        boolean ignore = m.find();
        String n1 = m.group(1);
        String n2 = m.group(2);
        return Text.translatable("wytr.tooltip.pageCounter", n1, n2).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
    }

    private List<Text> parseDialog() {
        DialogParser parser = new DialogParser();
        for(Text text : getSiblings().subList(2, getSiblings().size() - 5)) {
            if(text.getString().contains("• Dialogue")) {
                parser.emptyBuffers();
                parser.addToResult(text);
                continue;
            }
            if(npcDialogPattern.matcher(text.getString()).find()) {
                parser.emptyBuffers();
                parser.addToDBuffer(text, true);
                continue;
            }
            if(!parser.addToDBuffer(text, false)) {
                parser.addToNBuffer(text);
            }
        }
        parser.emptyBuffers();
        return parser.getResult();
    }

    private class DialogParser {
        private final List<Text> storage = new ArrayList<>();
        private final List<Text> nBuffer = new ArrayList<>();
        private final List<Text> dBuffer = new ArrayList<>();

        private List<Text> getResult() {
            List<Text> result = new ArrayList<>();
            for(Text text : storage) {
                result.addAll(wrapLine(text, 180));
            }
            return result;
        }

        private void addToResult(Text text) {
            if(text.getString().matches("• Dialogue Start")) {
                storage.add(Text.translatable(translationKey + ".start").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
            else if(text.getString().matches("• Dialogue End")) {
                storage.add(Text.translatable(translationKey + ".end").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
            else storage.add(text);
        }

        private boolean addToDBuffer(Text text, boolean force) {
            if(force) dBuffer.add(text);
            else if(!dBuffer.isEmpty()) dBuffer.add(text);
            else return false;
            Text tmpText = mergeTextStyleSide(dBuffer);
            Text translated = new NpcDialog(tmpText).setNoTranslationAddiction().text();
            if(translated.getSiblings().get(1).getContent() instanceof TranslatableTextContent) {
                storage.add(translated);
                dBuffer.clear();
                nBuffer.clear();
                return true;
            }
            return false;
        }

        private void addToNBuffer(Text text) {
            nBuffer.add(text);
            Text tmpText = mergeTextStyleSide(nBuffer);
            Text translated = new Narration(tmpText).setNoTranslationAddiction().text();
            if(translated.getContent() instanceof TranslatableTextContent) {
                storage.add(translated);
                nBuffer.clear();
                dBuffer.clear();
            }
        }

        public void emptyBuffers() {
            if(!dBuffer.isEmpty()) storage.addAll(dBuffer);
            else storage.addAll(nBuffer);
            dBuffer.clear();
            nBuffer.clear();
        }
    }
}
