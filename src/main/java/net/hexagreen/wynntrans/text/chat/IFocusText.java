package net.hexagreen.wynntrans.text.chat;

import com.wynntils.core.components.Managers;
import com.wynntils.features.overlays.NpcDialogueFeature;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Deque;
import java.util.List;
import java.util.Objects;

import static net.hexagreen.wynntrans.WynnTrans.wynnTranslationStorage;

public interface IFocusText extends ISpaceProvider {
    default MutableText setToConfirmless(Text text) {
        MutableText confirmless = Text.empty().append("\n");
        confirmless.append(text).append("\n");
        confirmless.append(Text.empty());

        return confirmless;
    }

    default MutableText setToPressShift(Text text, Text fullText) {
        MutableText confirmable = Text.empty().append("\n");
        confirmable.append(text).append("\n");
        confirmable.append(Text.empty()).append("\n");
        confirmable.append(pressShiftToContinue(fullText)).append("\n");
        confirmable.append(Text.empty());

        return confirmable;
    }

    default MutableText setToSelectOption(Text text, Text fullText, String pKeyDialog) {
        MutableText confirmable = Text.empty().append("\n");
        confirmable.append(text).append("\n");
        confirmable.append(Text.empty()).append("\n");
        selectionOptions(confirmable, fullText, pKeyDialog);
        confirmable.append(Text.empty()).append("\n");
        confirmable.append(selectOptionContinue(fullText)).append("\n");
        confirmable.append(Text.empty());

        return confirmable;
    }

    default MutableText setToCutScene(Text text) {
        MutableText cutScene = Text.empty().append("\n");
        cutScene.append(text).append("\n");
        cutScene.append(Text.empty()).append("\n");
        cutScene.append(Text.empty()).append("\n");
        cutScene.append(Text.empty());

        return cutScene;
    }

    default FocusType detectFocusType(Text text) {
        if(text.getSiblings().size() == 5) return FocusType.AUTO;
        else if(text.getSiblings().size() == 9) {
            if(FunctionalRegex.DIALOG_END.match(text, 6)) return FocusType.PRESS_SHIFT;
            else return FocusType.CUTSCENE;
        }
        else return FocusType.SELECT_OPTION;
    }

    default void clearChat() {
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
    }

    private Text pressShiftToContinue(Text fullText) {
        String key = "wytr.func.pressShift";
        if(WynnTrans.wynntilsLoaded) {
            NpcDialogueFeature feature = Managers.Feature.getFeatureInstance(NpcDialogueFeature.class);
            if(feature.isEnabled()) return fullText.getSiblings().get(6);
        }
        List<Text> original = fullText.getSiblings().get(6).getSiblings();
        MutableText textBody = (Text.translatable(key + ".1").setStyle(original.get(0).getStyle())).append(Text.translatable(key + ".2").setStyle(original.get(1).getStyle())).append(Text.translatable(key + ".3").setStyle(original.get(2).getStyle()));
        return centerAlign(textBody);
    }

    private Text selectOptionContinue(Text fullText) {
        String key = "wytr.func.selectOption";
        if(WynnTrans.wynntilsLoaded) {
            NpcDialogueFeature feature = Managers.Feature.getFeatureInstance(NpcDialogueFeature.class);
            if(feature.isEnabled()) return fullText.getSiblings().get(findLastOptionIndex(fullText) + 4);
        }
        List<Text> original = fullText.getSiblings().get(findLastOptionIndex(fullText) + 4).getSiblings();
        MutableText textBody = (Text.translatable(key + ".1").setStyle(original.get(0).getStyle())).append(Text.translatable(key + ".2").setStyle(original.get(1).getStyle())).append(Text.translatable(key + ".3").setStyle(original.get(1).getStyle()));
        return centerAlign(textBody);
    }

    private int findLastOptionIndex(Text fullText) {
        int selTooltipIdx = 9;
        for(int i = fullText.getSiblings().size() - 1; i >= 9; i--) {
            if(FunctionalRegex.SELECTION_END.match(fullText, i)) {
                selTooltipIdx = i;
                break;
            }
        }
        return selTooltipIdx - 4;
    }

    private void selectionOptions(MutableText constructingText, Text fullText, String pKeyDialog) {
        List<Text> original = fullText.getSiblings();
        for(int i = 6; i <= findLastOptionIndex(fullText); i = i + 2) {
            SelectionNormalizer carrier = new SelectionNormalizer(original.get(i));

            Text normalized = carrier.getText();

            // Selection number
            MutableText result = MutableText.of(normalized.getContent()).setStyle(normalized.getStyle())
                    .append(normalized.getSiblings().getFirst());

            // Selection body
            Text textBody = normalized.getSiblings().get(1);
            String valSelOpt = textBody.getString();
            String keySelOpt = pKeyDialog + ".selOpt." + DigestUtils.sha1Hex(valSelOpt).substring(0, 4);
            List<Text> textArgs = carrier.getArgs(keySelOpt, wynnTranslationStorage::checkTranslationExist);
            if(wynnTranslationStorage.checkTranslationExist(keySelOpt, valSelOpt)) {
                result.append(Text.translatable(keySelOpt, textArgs.toArray(Object[]::new)).setStyle(textBody.getStyle()));
            }
            else {
                MutableText reassembled = Text.empty().setStyle(textBody.getStyle());
                String[] strings = textBody.getString().split("%s", -1);
                for(int j = 0, l = strings.length; j < l; j++) {
                    if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                    if(j != l - 1) reassembled.append(textArgs.removeFirst());
                }
                result.append(reassembled);
            }
            constructingText.append(result).append("\n");
        }
    }

    enum FocusType {
        PRESS_SHIFT, SELECT_OPTION, AUTO, CUTSCENE
    }

    class SelectionNormalizer extends TextNormalizer {

        private SelectionNormalizer(Text text) {
            super(text, wynnTranslationStorage.getRulebooks().selectionRulebook);
        }

        @Override
        protected void normalize(Text text) {
            MutableText result = Text.empty().setStyle(text.getStyle());

            // Selection number
            Text selectionNumber = MutableText.of(text.getContent())
                    .append(copiedSiblings.removeFirst())
                    .append(copiedSiblings.removeFirst());
            result.append(selectionNumber);

            // Selection body
            Style desiredStyle = findDesiredStyle(copiedSiblings);
            ArgsRecord argsRecord = siblingsToArgs(copiedSiblings, desiredStyle);

            this.text = result.append(Text.literal(argsRecord.textContent()).setStyle(desiredStyle));
            this.args = argsRecord.args();
            this.flags = argsRecord.flags();
        }

        private Style findDesiredStyle(Deque<Text> siblings) {
            for(Text sibling : siblings) {
                if(Objects.equals(TextColor.fromFormatting(Formatting.GRAY), sibling.getStyle().getColor()))
                    return sibling.getStyle();
                if(Objects.equals(TextColor.fromFormatting(Formatting.LIGHT_PURPLE), sibling.getStyle().getColor()))
                    return sibling.getStyle();
            }
            return siblings.getFirst().getStyle();
        }
    }
}
