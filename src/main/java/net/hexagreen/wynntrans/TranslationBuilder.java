package net.hexagreen.wynntrans;

import net.hexagreen.wynntrans.enums.ChatType;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationBuilder {
    private static final String rootKey = "wytr.";
    private static final String dirFuncional = "func.";
    private static final String dirNormal = "normalText.";

    public WynnTransText buildShoutTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyShout = rootKey + dirFuncional + "Shout";
        Matcher m = ChatType.SHOUT.getRegex().matcher(text.getSiblings().get(0).getString());
        if(!m.find()) return wText;
        String name = m.group(1);
        String server = m.group(2);

        wText.getSiblingByIndex(0).setTranslateContent(keyShout, name, server);

        return wText;
    }

    public WynnTransText buildEventInfoTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyBase = rootKey + "eventInfo";
        String tEInfoBody = text.getString();
        String hash = DigestUtils.sha1Hex(tEInfoBody).substring(0, 8);
        String valEName = getContentLiteral(text, 1);
        String hash2 = DigestUtils.sha1Hex(valEName).substring(0,4);
        String keyEName = keyBase + "EventName" + hash2;

        wText.getSiblingByIndex(0).setTranslateContent(keyBase);

        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyEName, valEName)) {
            wText.getSiblingByIndex(1).setTranslateContent(keyEName);
        }

        for (int index = 2; text.getSiblings().size() > index; index++) {
            String keySibling = keyBase + "." + hash + "_" + index;
            String valSibling = getContentLiteral(text, index);
            if(valSibling.isEmpty()) continue;
            if (WynnTrans.wynnTranslationStorage.checkTranslationExist(keySibling, valSibling)) {
                wText.getSiblingByIndex(index).setTranslateContent(keySibling);
            }
        }

        return wText;
    }

    public WynnTransText buildThanksTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        WynnTransText playerName = getPlayerName(text, 1);
        playerName.addStyle(Style.EMPTY.withColor(Formatting.AQUA));
        String keyThanks = rootKey + dirFuncional + "BombThanks";

        wText.getSiblingByIndex(0).setTranslateContent(keyThanks + "_1_pre");
        wText.setSiblingByIndex(playerName,1);
        wText.getSiblingByIndex(2).setTranslateContent(keyThanks + "_1_suf");
        wText.getSiblingByIndex(3).setTranslateContent(keyThanks + "_2");

        return wText;
    }

    public WynnTransText buildThankyouTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.THANK_YOU.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        WynnTransText playerName;
        if(text.getSiblings().size() == 2) {
            playerName = getPlayerName(text, 1);
        }
        else {
            playerName = WynnTransText.of(Text.of(m.group(1)));
        }
        playerName.addStyle(Style.EMPTY.withColor(Formatting.AQUA));
        String keyThankYou = rootKey + dirFuncional + "ThankYou";

        wText.getSiblingByIndex(0).setTranslateContent(keyThankYou + "_pre");
        wText.addSibling(playerName);
        wText.addTranslateSibling(keyThankYou + "_suf");
        wText.getSiblingByIndex(2).addStyle(wText.getSiblingByIndex(0).getStyle());

        return wText;
    }

    public WynnTransText buildCrateGetTranslation(Text text){
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.CRATE_GET.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String keyCrateGet = rootKey + dirFuncional + "CrateReward";
        WynnTransText playerName;
        if(text.getSiblings().size() > 5) {
            wText.getSiblingByIndex(2).setTranslateContent(keyCrateGet + "_1");
            wText.getSiblingByIndex(4).setTranslateContent(keyCrateGet + "_2");
        }
        else {
            playerName = WynnTransText.of(Text.of(m.group(1)));
            wText.addSibling(0, playerName);
            wText.getSiblingByIndex(1).setTranslateContent(keyCrateGet + "_1");
            wText.getSiblingByIndex(3).setTranslateContent(keyCrateGet + "_2");
        }

        return wText;
    }

    public WynnTransText buildDisguiseTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyDisguise = rootKey + dirFuncional + "Disguise";

        wText.getSiblingByIndex(1).setTranslateContent(keyDisguise + "_1");
        wText.getSiblingByIndex(3).setTranslateContent(keyDisguise + "_2");

        return wText;
    }

    public WynnTransText buildSpeedboostTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        Matcher m = ChatType.SPEEDBOOST.getRegex().matcher(text.getString());
        if(!m.find()) return wText;
        String duration = m.group(1);
        String keySpeedboost = rootKey + dirFuncional + "Speedboost";

        wText.getSiblingByIndex(0).setTranslateContent(keySpeedboost, duration);
        wText.getSiblingByIndex(1).setTranslateContent(keySpeedboost + "_suf");
        wText.addTranslateSibling(0, keySpeedboost + "_pre");
        wText.getSiblingByIndex(0).addStyle(Style.EMPTY.withColor(Formatting.GRAY));

        return wText;
    }

    public WynnTransText buildResistanceTranslation(Text text) {
        WynnTransText wText = WynnTransText.of(text);
        String keyResistance = rootKey + dirFuncional + "Resistance";
        int lastIndex = text.getSiblings().size() - 1;

        wText.getSiblingByIndex(1).setTranslateContent(keyResistance + "_1");
        wText.getSiblingByIndex(2).setTranslateContent(keyResistance + "_2");
        wText.getSiblingByIndex(lastIndex).setTranslateContent(keyResistance + "_5");
        if(lastIndex == 5) {
            wText.getSiblingByIndex(3).setTranslateContent(keyResistance + "_3");
            wText.getSiblingByIndex(4).setTranslateContent(keyResistance + "_4");
        }

        return wText;
    }

    /**
     * Get content of target sibling refer by index as string
     *
     * @param text Target text
     * @param index Target sibling index
     * @return Some string when content is not empty, blank string otherwise
     */
    private String getContentLiteral(Text text, int index) {
        TextContent content = text.getSiblings().get(index).getContent();
        if("empty".equals(content.toString())) return "";
        return ((LiteralTextContent) content).string();
    }

    /**
     * Get player name from target sibling refer by index as string
     *
     * @param text Target text
     * @param index Target sibling index
     * @return Player name Text
     */
    private WynnTransText getPlayerName(Text text, int index) {
        String name = getContentLiteral(text, index);
        if("".equals(name)) {
            return WynnTransText.of(text.getSiblings().get(index).getSiblings().get(0));
        }
        else {
            return WynnTransText.of(text.getSiblings().get(index));
        }
    }
}
