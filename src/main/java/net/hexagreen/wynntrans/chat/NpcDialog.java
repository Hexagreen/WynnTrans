package net.hexagreen.wynntrans.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class NpcDialog extends WynnChatText{
    private final Object playername;
    private final String pNameString;
    private final String keyName;
    private final String valName;
    protected final String pKeyDialog;

    protected NpcDialog(Text text, Pattern regex) {
        super(removeCustomNickname(text), regex);
        this.pNameString = MinecraftClient.getInstance().player.getName().getString();
        this.playername = removedCustomNickname == null ? this.pNameString : removedCustomNickname;
        this.valName = getContentLiteral(0).replace(":", "");
        String npcName = valName.replace(" ", "").replace(".", "");
        this.keyName = parentKey + "name." + npcName;
        String dialogIdx = matcher.group(1) + ".";
        String dialogLen = matcher.group(2) + ".";
        String hash = DigestUtils.sha1Hex(inputText.getString().replace(pNameString, "%1$s")).substring(0, 8);
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
            String valDialog = getContentLiteral(1).replace(pNameString, "%1$s");

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
                String valDialog = getContentLiteral(index).replace(pNameString, "%1$s");

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
