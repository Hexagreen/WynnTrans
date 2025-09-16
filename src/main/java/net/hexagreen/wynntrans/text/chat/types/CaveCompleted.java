package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveCompleted extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_EXP = Pattern.compile("^\\+(\\d+) Experience Points$");
    private static final Pattern REGEX_EME = Pattern.compile("^ +- \\+(\\d+) Emeralds$");
    private final String keyCaveName;
    private final String valCaveName;

    public CaveCompleted(Text text) {
        super(text);
        this.valCaveName = getSibling(2).getSiblings().get(1).getString();
        this.keyCaveName = translationKey + normalizeStringForKey(valCaveName);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "cave.";
    }

    @Override
    protected void build() {
        MutableText titleText = Text.translatable(rootKey + "func.caveCompleted").setStyle(getSibling(1).getSiblings().getFirst().getStyle());
        resultText = Text.empty().append("\n")
                .append(centerAlign(titleText))
                .append("\n");

        Text t0;
        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            t0 = Text.translatable(keyCaveName).setStyle(getSibling(2).getSiblings().get(1).getStyle());
        }
        else {
            t0 = getSibling(2).getSiblings().get(1);
        }

        resultText.append(centerAlign(t0)).append("\n\n").append(Text.translatable(rootKey + "func.reward")
                .setStyle(getSibling(4).getSiblings().getFirst().getStyle())).append("\n");


        for(int i = 5; getSiblings().size() > i; i++) {
            Matcher m1 = REGEX_EXP.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m1.find()) {
                resultText.append(getSibling(i).getSiblings().get(0)).append(Text.translatable(rootKey + "func.reward.experience", m1.group(1))
                        .setStyle(getSibling(i).getSiblings().get(1).getStyle())).append("\n");
                continue;
            }
            Matcher m2 = REGEX_EME.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m2.find()) {
                resultText.append(getSibling(i).getSiblings().getFirst()).append(Text.translatable(rootKey + "func.reward.emerald", m2.group(1))
                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append("\n");
                continue;
            }

            Deque<Text> siblings = new ArrayDeque<>(getSibling(i).getSiblings());
            MutableText line = Text.empty().setStyle(GRAY);
            List<Text> mergeTarget = new ArrayList<>();
            String valExclusiveReward = getSibling(i).getString().replaceFirst(".+\\+", "+");
            Iterator<Text> it = siblings.iterator();

            line.append(siblings.removeFirst());

            while(it.hasNext()) {
                Text elem = it.next();
                String elemStr = elem.getString();

                if(elemStr.matches("\\+\\d+ ")) {
                    line.append(elem);
                    valExclusiveReward = valExclusiveReward.replaceFirst("^\\+\\d+ ", "");
                    it.remove();
                    break;
                }
                else if(valExclusiveReward.matches("^\\+\\d+ .+")) {
                    String[] split = valExclusiveReward.split(" ", 2);
                    String num = split[0];
                    String rewardName = split[1];
                    line.append(num + " ");
                    mergeTarget.add(Text.literal(rewardName).setStyle(elem.getStyle()));
                    valExclusiveReward = valExclusiveReward.replaceFirst("^\\+\\d+ ", "");
                    it.remove();
                    break;
                }
                else if(elemStr.matches("^\\+.+")) {
                    String rewardName = elemStr.replaceFirst("^\\+", "");
                    line.append("+");
                    mergeTarget.add(Text.literal(rewardName).setStyle(elem.getStyle()));
                    valExclusiveReward = valExclusiveReward.replaceFirst("^\\+", "");
                    it.remove();
                    break;
                }
                else {
                    mergeTarget.add(elem);
                }
            }

            mergeTarget.addAll(siblings);
            Text itemName = new ItemName(siblingsToText(mergeTarget)).returnNullMode().textAsMutable();
            if(itemName != null) {
                line.append(itemName).append("\n");
                resultText.append(line);
                continue;
            }

            String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
            for(int k = 0, size = mergeTarget.size(); k < size; k++) {
                String subKey = size == 1 ? "" : "." + i + 1;
                String keyExclusiveReward = keyCaveName + ".reward." + hash + subKey;
                Style style = mergeTarget.get(i).getStyle();

                if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                    line.append(Text.translatable(keyExclusiveReward).setStyle(style)).append("\n");
                }
                else {
                    resultText.append(getSibling(i)).append("\n");
                    return;
                }
            }
            resultText.append(line);
        }
    }
}
