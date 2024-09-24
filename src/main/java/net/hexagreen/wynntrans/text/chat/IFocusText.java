package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.enums.FunctionalRegex;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import static net.hexagreen.wynntrans.WynnTrans.wynnTranslationStorage;

public interface IFocusText extends ICenterAligned {

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
		List<Text> original = fullText.getSiblings().get(6).getSiblings();
		MutableText textBody = (newTranslate(key + ".1").setStyle(original.get(0).getStyle())).append(newTranslate(key + ".2").setStyle(original.get(1).getStyle())).append(newTranslate(key + ".3").setStyle(original.get(2).getStyle()));
		return getCenterIndent(textBody).append(textBody);
	}

	private Text selectOptionContinue(Text fullText) {
		String key = "wytr.func.selectOption";
		List<Text> original = fullText.getSiblings().get(findLastOptionIndex(fullText) + 4).getSiblings();
		MutableText textBody = (newTranslate(key + ".1").setStyle(original.get(0).getStyle())).append(newTranslate(key + ".2").setStyle(original.get(1).getStyle())).append(newTranslate(key + ".3").setStyle(original.get(2).getStyle()));
		return getCenterIndent(textBody).append(textBody);
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
			Text textBody = original.get(i).getSiblings().get(2);
			String keySelOpt = pKeyDialog + ".selOpt." + DigestUtils.sha1Hex(textBody.getString()).substring(0, 4);
			String valSelOpt = ((PlainTextContent) textBody.getContent()).string();
			MutableText selection = MutableText.of(original.get(i).getContent()).setStyle(original.get(i).getStyle()).append(original.get(i).getSiblings().get(0)).append(original.get(i).getSiblings().get(1));
			if(wynnTranslationStorage.checkTranslationExist(keySelOpt, valSelOpt)) {
				selection.append(newTranslate(keySelOpt).setStyle(textBody.getStyle()));
			}
			else {
				selection.append(textBody);
			}
			constructingText.append(selection).append("\n");
		}
	}

	private MutableText newTranslate(String key) {
		return MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
	}

	enum FocusType {
		PRESS_SHIFT, SELECT_OPTION, AUTO, CUTSCENE
	}
}
