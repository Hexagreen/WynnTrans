package net.hexagreen.wynntrans.text.sign;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.commons.codec.digest.DigestUtils;

public class WynnSign {

	protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
	private static final String rootKey = "wytr.sign.";
	private final Text[] message;

	public static WynnSign get(Text[] text) {
		return new WynnSign(text);
	}

	public static WynnSign get(SignText signText) {
		return new WynnSign(signText.getMessages(false));
	}

	private WynnSign(Text[] text) {
		this.message = text;
	}

	public Text[] translate() {
		translateLine();
		return message;
	}

	private void translateLine() {
		for(int i = 0; 4 > i; i++) {
			if(message[i].equals(Text.empty())) continue;

			String hash = DigestUtils.sha1Hex(message[i].getString()).substring(0, 24);
			String keySign = rootKey + hash;

			MutableText result;
			if(WTS.checkTranslationDoNotRegister(keySign)) {
				result = Text.translatable(keySign).setStyle(message[i].getStyle());
			}
			else {
				result = message[i].copyContentOnly();
			}

			int sIndex = 1;
			for(Text sibling : message[i].getSiblings()) {
				if(WTS.checkTranslationDoNotRegister(keySign + "_" + sIndex)) {
					result.append(Text.translatable(keySign + "_" + sIndex).setStyle(sibling.getStyle()));
				}
				else {
					result.append(sibling);
				}
				sIndex++;
			}
			message[i] = result;
		}
	}

	public void registerTranslation() {
		for(int i = 0; 4 > i; i++) {
			if(message[i].equals(Text.empty())) continue;
			try {
				if(message[i].getContent() instanceof TranslatableTextContent) return;
			} catch(IndexOutOfBoundsException ignore) {
			}

			String hash = DigestUtils.sha1Hex(message[i].getString()).substring(0, 24);
			String keySign = rootKey + hash;
			String valSign = message[i].copyContentOnly().getString();

			if(!valSign.isEmpty()) {
				WTS.checkTranslationExist(keySign, valSign);
			}

			int sIndex = 1;
			for(Text sibling : message[i].getSiblings()) {
				String valSignSibling = sibling.getString();
				if(valSignSibling.isEmpty()) continue;
				WTS.checkTranslationExist(keySign + "_" + sIndex, valSignSibling);
				sIndex++;
			}
		}
		//noinspection DataFlowIssue
		MinecraftClient.getInstance().player.sendMessage(Text.translatable("wytr.command.signTranslationRegistered"));
	}

}
