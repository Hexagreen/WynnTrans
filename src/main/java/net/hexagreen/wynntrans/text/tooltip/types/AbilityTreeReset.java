package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class AbilityTreeReset extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("\uE001").matcher(WynnTrans.currentScreen).find()) return false;
        return texts.getFirst().getString().matches("§[8a]§l(Empty Socket|Waiting for Shards|Click to Confirm)");
    }

    public AbilityTreeReset(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.abilityTree.resetPage";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text _head = getSibling(0).getSiblings().getFirst();
        String k = switch(_head.getString()) {
            case "Empty Socket" -> translationKey + ".emptySocket";
            case "Waiting for Shards" -> translationKey + ".waitingShard";
            case "Click to Confirm" -> translationKey + ".resetReady";
            default -> "";
        };
        if(k.isEmpty()) {
            resultText = inputText;
            return;
        }

        resultText.append(Text.translatable(k).setStyle(_head.getStyle()));
        wrapLine(Text.translatable(k + ".desc").setStyle(GRAY), 150).forEach(resultText::append);
        resultText.append(" ");

        Deque<Text> _tail = new ArrayDeque<>(getSibling(-1).getSiblings());
        MutableText translated = Text.empty().append(_tail.removeFirst());
        String k1 = switch(_tail.removeFirst().getString().replaceAll("[^A-z]", "")) {
            case "AvailableShards" -> translationKey + ".shardExist";
            case "RequiredShards" -> translationKey + ".shardReq";
            default -> "";
        };
        if(k1.isEmpty()) {
            resultText.append(getSibling(-1));
            return;
        }

        translated.append(Text.translatable(k1).setStyle(GRAY));
        _tail.forEach(translated::append);
        resultText.append(translated);
    }
}
