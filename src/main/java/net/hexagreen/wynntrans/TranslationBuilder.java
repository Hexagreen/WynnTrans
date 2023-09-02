package net.hexagreen.wynntrans;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationBuilder {
    private static final String rootKey = "wytr.";
    private static final String dirFuncional = "func.";
    private static final String dirNpcName = "name.";
    private static final String dirDialog = "dialog.";
    private static final String dirNarration = "narration.";
    private static final String dirSelection = "selOpt.";
    private static final String dirQuest = "quest.";
    private static final Pattern dialogCounter = Pattern.compile("^\\[([0-9])+/([0-9])+] $");

    private final MinecraftClient client;

    public TranslationBuilder() {
        this.client = MinecraftClient.getInstance();
    }

    /**
     * Build a new NPC-Dialog-Form text contains {@link net.minecraft.text.TranslatableTextContent} from parameter.
     * <p><div class="fabric"><table border=1>
     * <caption>NPC Dialog Example</caption>
     * <tr>
     *  <th><b>Fields</b></th>&emsp;<th><b>Contains</b></th>
     * </tr>
     * <tr>
     *  <td>context</td>&emsp;<td><U>[1/2] </U></td>
     * </tr>
     * <tr>
     *  <td>siblings[0]</td>&emsp;<td><U>Tasim: </U></td>
     * </tr>
     * <tr>
     *  <td>siblings[n]</td>&emsp;<td><U>Hello, Bro.</U></td>
     * </tr>
     * </table></div></p>
     * @param text Original text object
     * @return Edited text that contains translation key
     */
    public WynnTransText buildNPCDialogTranslation(Text text) {
        String playername = client.player.getName().getString();
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = dialogCounter.matcher(((LiteralTextContent) text.getContent()).string());
        m.find();
        String dialogIdx = m.group(1) + ".";
        String dialogLen = m.group(2) + ".";
        String valName = ((LiteralTextContent)text.getSiblings().get(0).getContent()).string().replace(": ","");
        String npcName = valName.replace(" ", "").replace(".", "");
        String keyName = rootKey + dirNpcName + npcName;
        String keyDialogBase = rootKey + dirDialog + npcName + "." + dialogLen + dialogIdx;
        String tDialog = text.getString().replace(playername, "%s");
        String hash = DigestUtils.sha1Hex(tDialog).substring(0, 8);
        WynnTransText colon = WynnTransText.of(Text.literal(": ").setStyle(text.getSiblings().get(0).getStyle()));


        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyName, valName)) {
            wText.getSiblingByIndex(0).setContent(keyName);
            wText.getSiblingByIndex(0).addSiblings(colon);
        }

        if(text.getSiblings().size() == 2){
            String keyDialog = keyDialogBase + hash;
            String valueDialog = ((LiteralTextContent) text.getSiblings().get(1).getContent()).string().replace(playername, "%s");
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyDialog, valueDialog)) {
                if(((LiteralTextContent) text.getSiblings().get(1).getContent()).string().contains(playername)) {
                    wText.getSiblingByIndex(1).setContent(keyDialog, playername);
                }
                else {
                    wText.getSiblingByIndex(1).setContent(keyDialog);
                }
            }
        }
        else {
            for(int index = 1; text.getSiblings().size() > index; index++) {
                String keyDialog = keyDialogBase + hash + "_" + index;
                String valueDialog = ((LiteralTextContent) text.getSiblings().get(index).getContent()).string().replace(playername, "%s");
                if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyDialog, valueDialog)) {
                    if(((LiteralTextContent) text.getSiblings().get(index).getContent()).string().contains(playername)) {
                        wText.getSiblingByIndex(index).setContent(keyDialog, playername);
                    }
                    else {
                        wText.getSiblingByIndex(index).setContent(keyDialog);
                    }
                }
            }
        }
        return wText;
    }

    /**
     * Build new selection dialog from glued dialog.
     * @param text Fully concatenated selection dialog text
     * @return Edited formed text that contains translation keys
     */
    public WynnTransText buildNPCSelectionTranslation(Text text) {
        String playername = client.player.getName().getString();
        WynnTransText wText = WynnTransText.of(text);
        int firstSelectionIdx = 6;
        int tooltipIndex = 10;
        for(int i = text.getSiblings().size() - 1; i > 0; i--) {
            if(ChatType.SELECTION_END.match(text, i)) {
                tooltipIndex = i;
                break;
            }
        }
        int lastSelectionIdx = tooltipIndex - 4;

        Text parantText = text.getSiblings().get(2);
        Matcher m = dialogCounter.matcher(((LiteralTextContent) parantText.getContent()).string());
        m.find();
        String dialogIdx = m.group(1) + ".";
        String dialogLen = m.group(2) + ".";
        String valName = ((LiteralTextContent)parantText.getSiblings().get(0).getContent()).string().replace(": ","");
        String npcName = valName.replace(" ", "").replace(".", "");
        String keyDialogBase = rootKey + dirDialog + npcName + "." + dialogLen + dialogIdx;
        String parantDialog = parantText.getString().replace(playername, "%s");
        String parantHash = DigestUtils.sha1Hex(parantDialog).substring(0, 8);
        String keySelOptBase = keyDialogBase + parantHash + "." + dirSelection;

        wText.setSiblingByIndex(2, WynnTrans.translationBuilder.buildNPCDialogTranslation(text.getSiblings().get(2)));
        wText.setSiblingByIndex(tooltipIndex, WynnTrans.translationBuilder.buildSelectOptionContinue(text.getSiblings().get(tooltipIndex)));

        for(int i = firstSelectionIdx; i <= lastSelectionIdx; i = i + 2) {
            Text origin = text.getSiblings().get(i).getSiblings().get(2);
            wText.getSiblingByIndex(i).setSiblingByIndex(2, buildNPCSelectionSibling(origin, keySelOptBase));
        }
        return wText;
    }

    private WynnTransText buildNPCSelectionSibling(Text text, String keyBase) {
        WynnTransText wText = WynnTransText.of(text);
        String hash = DigestUtils.sha1Hex(text.getString()).substring(0, 4);
        String keyOption = keyBase + hash;
        String valOption = ((LiteralTextContent) text.getContent()).string();

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyOption, valOption)) {
            wText.setContent(keyOption);
        }

        return wText;
    }

    public WynnTransText buildNarrationTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyNarrationBase = rootKey + dirNarration;
        String tNarration = text.getString();
        String hash = DigestUtils.sha1Hex(tNarration);

        if(text.getSiblings().size() == 0){
            String keyNarration = keyNarrationBase + hash;
            String valNarration = ((LiteralTextContent) text.getContent()).string();
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarration, valNarration)) {
                wText.setContent(keyNarration);
            }
        }
        else {
            String keyNarrContent = keyNarrationBase + hash + "_1";
            String valNarrContent = ((LiteralTextContent) text.getContent()).string();
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarrContent, valNarrContent)) {
                wText.setContent(keyNarrContent);
            }
            for(int index = 0; text.getSiblings().size() > index; index++) {
                String keyNarrSibling = keyNarrationBase + hash + "_" + (index + 2);
                String valNarrSibling = ((LiteralTextContent) text.getSiblings().get(index).getContent()).string();
                if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNarrSibling, valNarrSibling)) {
                    wText.getSiblingByIndex(index).setContent(keyNarrSibling);
                }
            }
        }
        return wText;
    }

    public WynnTransText buildNewQuestTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyNewQuest = rootKey + dirFuncional + "NewQuest";
        String valNewQuest = ((LiteralTextContent) text.getContent()).string();
        String valQuestName = ((LiteralTextContent) text.getSiblings().get(0).getContent()).string();
        String tQuestName = valQuestName.replace(" ", "");
        String keyQuestName = rootKey + dirQuest + tQuestName;

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyNewQuest, valNewQuest)) {
            wText.setContent(keyNewQuest);
        }

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyQuestName, valQuestName)) {
            wText.getSiblingByIndex(0).setContent(keyQuestName);
        }
        return wText;
    }

    public WynnTransText buildPressShiftContinue(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + dirFuncional + "PressShift";
        String keyIndent = keyBase + "_indent";
        String valIndent = ((LiteralTextContent) text.getContent()).string();

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyIndent, valIndent)) {
            wText.setContent(keyIndent);
        }

        for (int index = 0; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "_" + index;
            String valSibling = ((LiteralTextContent) text.getSiblings().get(index).getContent()).string();
            if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keySibling, valSibling)) {
                wText.getSiblingByIndex(index).setContent(keySibling);
            }
        }
        return wText;
    }

    public WynnTransText buildSelectOptionContinue(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + dirFuncional + "SelectOption";
        String keyIndent = keyBase + "_indent";
        String valIndent = ((LiteralTextContent) text.getContent()).string();

        if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keyIndent, valIndent)) {
            wText.setContent(keyIndent);
        }

        for (int index = 0; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "_" + index;
            String valSibling = ((LiteralTextContent) text.getSiblings().get(index).getContent()).string();
            if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keySibling, valSibling)) {
                wText.getSiblingByIndex(index).setContent(keySibling);
            }
        }
        return wText;
    }
}
