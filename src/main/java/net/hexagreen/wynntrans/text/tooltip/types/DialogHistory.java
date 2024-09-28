package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.chat.ChatType;
import net.hexagreen.wynntrans.text.chat.types.Narration;
import net.hexagreen.wynntrans.text.chat.types.NpcDialog;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogHistory extends WynnTooltipText {
	private static final Pattern npcDialogPattern = Pattern.compile("^\\[\\d+/\\d+]");
	private static final Pattern pageCounterPattern = Pattern.compile("Page (\\d+) of (\\d+)");

	public static boolean typeChecker(List<Text> text) {
		return text.getFirst().getString().equals("Dialogue History");
	}

	public DialogHistory(List<Text> text) {
		super(colorCodedToStyledBatch(text));
	}

	@Override
	protected String setParentKey() {
		return rootKey + "tooltip.dialogHistory";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText.append(newTranslate(parentKey))
				.append(" ");
		for(Text text : parseDialog()) {
			resultText.append(text);
		}
		resultText.append(" ")
				.append(getPageCounter())
				.append(" ")
				.append(newTranslate("wytr.tooltip.nextPage"))
				.append(newTranslate("wytr.tooltip.previousPage"));
	}

	private Text getPageCounter() {
		Matcher m = pageCounterPattern.matcher(getSiblings().get(getSiblings().size() - 4).getString());
		boolean ignore = m.find();
		String n1 = m.group(1);
		String n2 = m.group(2);
		return newTranslate("wytr.tooltip.pageCounter", n1, n2).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
	}

	private List<Text> parseDialog() {
		DialogParser parser = new DialogParser();
		for(Text text : getSiblings().subList(2, getSiblings().size() - 5)) {
			if(text.getString().contains("• Dialogue")) {
				parser.flushNarration();
				parser.runParser();
				parser.addToResult(text);
				continue;
			}
			if(npcDialogPattern.matcher(text.getString()).find()) {
				parser.flushNarration();
				parser.runParser();
				parser.addToParser(text);
				continue;
			}
			if(parser.isNarration(text)) {
				parser.runParser();
				parser.addToParser(text);
				parser.tryNarrationParsing();
				continue;
			}
			parser.addToParser(text);
		}
		parser.flushNarration();
		parser.runParser();
		return parser.getResult();
	}

	private class DialogParser {

		private final List<Text> storage = new ArrayList<>();
		private final List<Text> parseTarget = new ArrayList<>();
		private final List<Text> narration = new ArrayList<>();

		private List<Text> getResult() {
			List<Text> result = new ArrayList<>();
			for(Text text : storage) {
				if(text.getString().matches("• Dialogue Start")) {
					result.add(newTranslate(parentKey + ".start").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
					continue;
				}
				else if(text.getString().matches("• Dialogue End")) {
					result.add(newTranslate(parentKey + ".end").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
					continue;
				}
				else if(npcDialogPattern.matcher(text.getString()).find()) {
					text = translateNpcDialog(text);
				}
				else {
					text = translateNarration(text);
				}
				result.addAll(wrapLine(text, 180));
			}
			return result;
		}

		private void addToResult(Text text) {
			storage.add(text);
		}

		private void addToParser(Text text) {
			parseTarget.add(text);
		}

		private void runParser() {
			if(parseTarget.isEmpty()) return;
			storage.add(mergeTextStyleSide(parseTarget.toArray(Text[]::new)));
			parseTarget.clear();
		}

		private void tryNarrationParsing() {
			narration.addAll(parseTarget);
			parseTarget.clear();
			Text tmpText = mergeTextStyleSide(narration.toArray(Text[]::new));
			String hash = DigestUtils.sha1Hex(tmpText.getString());
			if(WTS.checkTranslationDoNotRegister("wytr.narration." + hash)
					|| WTS.checkTranslationDoNotRegister("wytr.narration." + hash + "_1")) {
				storage.add(tmpText);
				narration.clear();
			}
		}

		private void flushNarration() {
			storage.addAll(narration);
			narration.clear();
		}

		private boolean isNarration(Text text) {
			for(Text sibling : text.getSiblings()) {
				Style style = sibling.getStyle();
				if(style.isItalic() && style.getColor() != null && style.getColor().getName().equals("gray")) {
					return true;
				}
			}
			return false;
		}

		private Text translateNpcDialog(Text text) {
			MutableText reformed = text.getSiblings().getFirst().copy();
			for(int i = 1; i < text.getSiblings().size(); i++) {
				reformed.append(text.getSiblings().get(i));
			}
			return new NpcDialog(reformed, ChatType.DIALOG_NORMAL.getRegex()).setNoTranslationAddiction().text();
		}

		private Text translateNarration(Text text) {
			MutableText reformed;
			if(text.getSiblings().size() == 1) {
				reformed = text.getSiblings().getFirst().copy();
			}
			else {
				reformed = Text.empty();
				for(int i = 0; i < text.getSiblings().size(); i++) {
					reformed.append(text.getSiblings().get(i));
				}
			}
			return new Narration(reformed, null).setNoTranslationAddiction().text();
		}
	}
}
