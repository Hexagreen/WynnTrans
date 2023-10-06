package net.hexagreen.wynntrans.texts;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class NpcDialog extends FocusText{
    String playername;
    String dialogIdx;
    String dialogLen;
    String npcName;
    String keyName;
    String valName;
    String pKeyDialog;
    String hash;
    String mode;

    NpcDialog(MutableText text, Pattern regex) {
        super(text, regex);
        this.playername = MinecraftClient.getInstance().player.getName().getString();
        this.dialogIdx = matcher.group(1) + ".";
        this.dialogLen = matcher.group(2) + ".";
        this.valName = getLiteralContent(0).replace(":", "");
        this.npcName = valName.replace(" ", "").replace(".", "");
        this.keyName = parentKey + "name." + npcName;
        this.hash = DigestUtils.sha1Hex(text.getString().replace(playername, "%1$s")).substring(0, 8);
        this.pKeyDialog = parentKey + "dialog." + npcName + "." + dialogLen + dialogIdx + hash;
    }

    public static NpcDialog of(Text text, Pattern regex) {
        NpcDialog npcDialog = new NpcDialog((MutableText) text, regex);
        npcDialog.mode = "normal";
        return npcDialog;
    }

    /**
     * Create {@code NpcDialog} object from given text with selected mode.
     * @param text Source text
     * @param regex Matched text pattern
     * @param mode One of these; {@code "confirmless", "confirmable", "selection"}
     * @return New {@code NpcDialog} object
     */
    public static NpcDialog of(Text text, Pattern regex, String mode) {
        NpcDialog npcDialog = new NpcDialog((MutableText) text, regex);
        npcDialog.mode = mode;
        return npcDialog;
    }

    @Override
    protected String setParentKey() {
        return rootKey;
    }

    @Override
    protected void build() {
        switch (mode) {
            case "normal" -> buildLine();
            case "confirmless" -> {
                buildLine(); setToConfirmless();
            }
            case "confirmable" -> {
                buildLine(); setToPressShift();
            }
            case "selection" -> {
                buildLine(); setToSelectOption(pKeyDialog);
            }
            default -> {
                resultText = inputText;
                LOGGER.warn("[WynnTrans] Failed to translate follow text. (No such mode: {})", mode);
            }
        }
    }

    @Override
    public void print() {
        parentKey = setParentKey();
        build();
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
        MinecraftClient.getInstance().player.sendMessage(resultText);
    }

    private void buildLine() {
        MutableText original;
        if(!mode.equals("normal")) original = inputText;
        else original = (MutableText) inputText.getSiblings().get(2);

        resultText = MutableText.of(original.getContent());

        if(WTS.checkTranslationExist(keyName, valName)) {
            resultText.append(getTranslate(keyName).setStyle(original.getSiblings().get(0).getStyle()));
            resultText.append(Text.literal(": ").setStyle(original.getSiblings().get(0).getStyle()));
        }
        else {
            resultText.append(original.getSiblings().get(0));
        }

        if(original.getSiblings().size() == 2) {
            String valDialog = getLiteralContent(1).replace(playername, "%1$s");
            if(WTS.checkTranslationExist(pKeyDialog, valDialog)) {
                if(valDialog.contains("%1$s")) {
                    resultText.append(getTranslate(pKeyDialog, playername).setStyle(original.getSiblings().get(1).getStyle()));
                }
                else {
                    resultText.append(getTranslate(pKeyDialog).setStyle(original.getSiblings().get(1).getStyle()));
                }
            }
            else {
                resultText.append(original.getSiblings().get(1));
            }
        }
        else {
            for(int index = 1; original.getSiblings().size() > index; index++) {
                String keyDialog = pKeyDialog + "_" + index;
                String valDialog = getLiteralContent(index).replace(playername, "%1$s");
                if(WTS.checkTranslationExist(keyDialog, valDialog)) {
                    if(valDialog.contains("%1$s")) {
                        resultText.append(getTranslate(keyDialog, playername).setStyle(original.getSiblings().get(index).getStyle()));
                    }
                    else {
                        resultText.append(getTranslate(keyDialog).setStyle(original.getSiblings().get(index).getStyle()));
                    }
                }
                else {
                    resultText.append(original.getSiblings().get(index));
                }
            }
        }
    }
}
