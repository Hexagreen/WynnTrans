package net.hexagreen.wynntrans.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class NpcDialog extends WynnChatText{
    private final String playername;
    private final String dialogIdx;
    private final String dialogLen;
    private final String npcName;
    private final String keyName;
    private final String valName;
    private final String hash;
    protected final String pKeyDialog;

    NpcDialog(Text text, Pattern regex) {
        super(text, regex);
        this.playername = MinecraftClient.getInstance().player.getName().getString();
        this.dialogIdx = matcher.group(1) + ".";
        this.dialogLen = matcher.group(2) + ".";
        this.valName = getContentLiteral(0).replace(":", "");
        this.npcName = valName.replace(" ", "").replace(".", "");
        this.keyName = parentKey + "name." + npcName;
        this.hash = DigestUtils.sha1Hex(text.getString().replace(playername, "%1$s")).substring(0, 8);
        this.pKeyDialog = parentKey + "dialog." + npcName + "." + dialogLen + dialogIdx + hash;
    }

    public static NpcDialog of(Text text, Pattern regex) {
        return new NpcDialog(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey;
    }

    @Override
    protected void build() {
        resultText = MutableText.of(inputText.getContent()).setStyle(inputText.getStyle());

        Text t0 = inputText.getSiblings().get(0);
        if(WTS.checkTranslationExist(keyName, valName)) {
            resultText.append(newTranslate(keyName).setStyle(t0.getStyle()));
            resultText.append(Text.literal(": ").setStyle(t0.getStyle()));
        }
        else {
            resultText.append(t0);
        }

        if(inputText.getSiblings().size() == 2) {
            String valDialog = getContentLiteral(1).replace(playername, "%1$s");

            Text t1 = inputText.getSiblings().get(1);
            if(WTS.checkTranslationExist(pKeyDialog, valDialog)) {
                if(valDialog.contains("%1$s")) {
                    resultText.append(newTranslate(pKeyDialog, playername).setStyle(t1.getStyle()));
                }
                else {
                    resultText.append(newTranslate(pKeyDialog).setStyle(t1.getStyle()));
                }
            }
            else {
                resultText.append(t1);
            }
        }
        else {
            for(int index = 1; inputText.getSiblings().size() > index; index++) {
                String keyDialog = pKeyDialog + "_" + index;
                String valDialog = getContentLiteral(index).replace(playername, "%1$s");

                Text ti = inputText.getSiblings().get(index);
                if(WTS.checkTranslationExist(keyDialog, valDialog)) {
                    if(valDialog.contains("%1$s")) {
                        resultText.append(newTranslate(keyDialog, playername).setStyle(ti.getStyle()));
                    }
                    else {
                        resultText.append(newTranslate(keyDialog).setStyle(ti.getStyle()));
                    }
                }
                else {
                    resultText.append(ti);
                }
            }
        }
    }
}
