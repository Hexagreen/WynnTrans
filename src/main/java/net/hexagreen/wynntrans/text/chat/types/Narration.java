package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.TextNormalizer;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class Narration extends WynnChatText {
    protected final String keyNarration;
    private final NarrationCarrier carrier;
    private boolean addiction = true;

    public Narration(Text text) {
        this(new NarrationCarrier(text));
    }

    private Narration(NarrationCarrier carrier) {
        super(carrier.getText());
        this.carrier = carrier;
        this.keyNarration = translationKey + DigestUtils.sha1Hex(inputText.getString());
    }

    public Narration setNoTranslationAddiction() {
        this.addiction = false;
        return this;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "narration.";
    }

    @Override
    protected void build() {
        List<Text> narrationArgs = carrier.getArgs(keyNarration, this::checkTranslationExist);
        if(checkTranslationExist(keyNarration, inputText.getString())) {
            resultText = Text.translatable(keyNarration, narrationArgs.toArray(Object[]::new)).setStyle(inputText.getStyle());
        }
        else {
            MutableText reassembled = Text.empty().setStyle(inputText.getStyle());
            String[] strings = inputText.getString().split("%s", -1);
            for(int j = 0, l = strings.length; j < l; j++) {
                if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                if(j != l - 1) reassembled.append(narrationArgs.removeFirst());
            }
            resultText = reassembled;
        }
    }

    private boolean checkTranslationExist(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
        else return WTS.checkTranslationDoNotRegister(key);
    }

    private static class NarrationCarrier extends TextNormalizer {

        protected NarrationCarrier(Text text) {
            super(text, WTS.getRulebooks().narrationRulebook);
        }

        @Override
        protected void normalize(Text text) {
            copiedSiblings.addFirst(MutableText.of(text.getContent()).setStyle(text.getStyle()));
            Style desiredStyle = findDesiredStyle(copiedSiblings);
            ArgsRecord argsRecord = siblingsToArgs(copiedSiblings, desiredStyle);

            this.text = Text.literal(argsRecord.textContent()).setStyle(desiredStyle);
            this.args = argsRecord.args();
            this.flags = argsRecord.flags();
        }

        private Style findDesiredStyle(Deque<Text> siblings) {
            for(Text sibling : siblings) {
                if(!sibling.getString().isBlank() && Objects.equals(TextColor.fromFormatting(Formatting.GRAY), sibling.getStyle().getColor()))
                    return sibling.getStyle();
            }
            return siblings.getFirst().getStyle();
        }
    }
}
