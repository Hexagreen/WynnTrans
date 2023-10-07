package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.ChatType;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.regex.Pattern;

public abstract class FocusText extends NpcDialog {
    private final int selLastOptionIdx;
    private final Text fullText;
    private int selTooltipIdx = 10;

    FocusText(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
        for(int i = text.getSiblings().size() - 1; i > 10; i--) {
            if(ChatType.SELECTION_END.match(text, i)) {
                selTooltipIdx = i;
                break;
            }
        }
        selLastOptionIdx = selTooltipIdx - 4;
    }

    protected void setToConfirmless() {
        MutableText confirmless = Text.empty().append("\n");
        confirmless.append(resultText).append("\n");
        confirmless.append(Text.empty());
        resultText = confirmless;
    }

    protected void setToPressShift() {
        MutableText confirmable = Text.empty().append("\n");
        confirmable.append(resultText).append("\n");
        confirmable.append(Text.empty()).append("\n");
        confirmable.append(pressShiftToContinue()).append("\n");
        confirmable.append(Text.empty());
        resultText = confirmable;
    }

    protected void setToSelectOption() {
        MutableText confirmable = Text.empty().append("\n");
        confirmable.append(resultText).append("\n");
        confirmable.append(Text.empty()).append("\n");
        resultText = confirmable;
        selectionOptions();
        resultText.append(Text.empty()).append("\n");
        resultText.append(selectOptionContinue()).append("\n");
        resultText.append(Text.empty());
    }

    private Text pressShiftToContinue() {
        String key = rootKey + dirFunctional + "PressShift";
        List<Text> original = fullText.getSiblings().get(6).getSiblings();
        MutableText text = newTranslate(key + "_indent");
        text.append(newTranslate(key + "_1").setStyle(original.get(0).getStyle()))
                .append(newTranslate(key + "_2").setStyle(original.get(1).getStyle()))
                .append(newTranslate(key + "_3").setStyle(original.get(2).getStyle()));
        return text;
    }

    private Text selectOptionContinue() {
        String key = rootKey + dirFunctional + "SelectOption";
        List<Text> original = fullText.getSiblings().get(selTooltipIdx).getSiblings();
        MutableText text = newTranslate(key + "_indent");
        text.append(newTranslate(key + "_1").setStyle(original.get(0).getStyle()))
                .append(newTranslate(key + "_2").setStyle(original.get(1).getStyle()))
                .append(newTranslate(key + "_3").setStyle(original.get(2).getStyle()));
        return text;
    }

    private void selectionOptions() {
        List<Text> original = fullText.getSiblings();
        for(int i = 6; i <= selLastOptionIdx; i = i + 2) {
            Text textBody = original.get(i).getSiblings().get(2);
            String keySelOpt = pKeyDialog + ".selOpt." + DigestUtils.sha1Hex(textBody.getString()).substring(0, 4);
            String valSelOpt = ((LiteralTextContent) textBody.getContent()).string();
            MutableText selection = MutableText.of(original.get(i).getContent())
                    .append(original.get(i).getSiblings().get(0))
                    .append(original.get(i).getSiblings().get(1));
            if(WTS.checkTranslationExist(keySelOpt, valSelOpt)) {
                selection.append(newTranslate(keySelOpt).setStyle(textBody.getStyle()));
            }
            else {
                selection.append(textBody);
            }
            resultText.append(selection).append("\n");
        }
    }
}
