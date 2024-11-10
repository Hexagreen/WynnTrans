package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class NpcDialog extends WynnChatText {
    protected final String keyDialog;
    private final Object playerName;
    private final String clientPlayerName;
    private final String keyName;
    private final String valName;
    private boolean addiction = true;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n?\\[(\\d+)/(\\d+)] .+:").matcher(text.getString()).find();
    }

    @SuppressWarnings("DataFlowIssue")
    public NpcDialog(Text text) {
        super(removeCustomNicknameFromDialog(text), Pattern.compile("^\\n?\\[(\\d+)/(\\d+)] .+:"));
        this.clientPlayerName = MinecraftClient.getInstance().player.getName().getString();
        this.playerName = removedCustomNickname == null ? this.clientPlayerName : removedCustomNickname;
        this.valName = getContentString(0).replace(": ", "");
        String npcName = normalizeStringForKey(valName);
        this.keyName = parentKey + "name." + npcName;
        String dialogIdx = matcher.group(1) + ".";
        String dialogLen = matcher.group(2) + ".";
        String hash = DigestUtils.sha1Hex(inputText.getString().replace(clientPlayerName, "%1$s")).substring(0, 8);
        this.keyDialog = parentKey + "dialog." + npcName + "." + dialogLen + dialogIdx + hash;
    }

    public NpcDialog setNoTranslationAddiction() {
        this.addiction = false;
        return this;
    }

    @Override
    protected String setParentKey() {
        return rootKey;
    }

    @Override
    protected void build() {
        if(inputText.getContent().equals(PlainTextContent.EMPTY)) {
            processMalformedDialog();
            return;
        }

        resultText = MutableText.of(inputText.getContent()).setStyle(inputText.getStyle());

        Text t0 = getSibling(0);
        if(checkTranslationExist(keyName, valName)) {
            resultText.append(Text.translatable(keyName).setStyle(t0.getStyle()));
            resultText.append(Text.literal(": ").setStyle(t0.getStyle()));
        }
        else {
            resultText.append(t0);
        }

        if(getSiblings().size() == 2) {
            String valDialog = getContentString(1).replace(clientPlayerName, "%1$s");

            Text t1 = getSibling(1);
            if(checkTranslationExist(keyDialog, valDialog)) {
                if(valDialog.contains("%1$s")) {
                    resultText.append(Text.translatable(keyDialog, playerName).setStyle(t1.getStyle()));
                }
                else {
                    resultText.append(Text.translatable(keyDialog).setStyle(t1.getStyle()));
                }
            }
            else {
                resultText.append(t1);
            }
        }
        else {
            for(int index = 1; getSiblings().size() > index; index++) {
                String keyDialog = this.keyDialog + "_" + index;
                String valDialog = getContentString(index).replace(clientPlayerName, "%1$s");

                Text ti = getSibling(index);
                if(checkTranslationExist(keyDialog, valDialog)) {
                    if(valDialog.contains("%1$s")) {
                        resultText.append(Text.translatable(keyDialog, playerName).setStyle(ti.getStyle()));
                    }
                    else {
                        resultText.append(Text.translatable(keyDialog).setStyle(ti.getStyle()));
                    }
                }
                else {
                    resultText.append(ti);
                }
            }
        }
    }

    private void processMalformedDialog() {
        MutableText corrected = getSiblings().getFirst().copy();
        for(int i = 1; getSiblings().size() > i; i++) {
            corrected.append(getSibling(i));
        }
        resultText = new NpcDialog(corrected).text();
    }

    private boolean checkTranslationExist(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
        else return WTS.checkTranslationDoNotRegister(key);
    }
}
