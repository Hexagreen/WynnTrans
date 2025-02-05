package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ILootrun extends ISpaceProvider {
    default MutableText translateEffect(String effect) {
        Matcher matcher = Pattern.compile("\\[(\\+\\d+%?) (.+)]").matcher(effect);
        if(matcher.find()) {
            String amount = matcher.group(1);
            String effectName = matcher.group(2);
            String keyEffect = "wytr.lootrun.effect." + effectName.replaceAll(" ", "");
            Text effectText;
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyEffect, effectName)) {
                effectText = Text.translatable(keyEffect);
            }
            else effectText = Text.literal(effectName);

            return Text.translatable("wytr.lootrun.effect", amount, effectText);
        }
        return Text.literal(effect);
    }

    default List<List<Text>> getTextChunks(List<Text> texts) {
        List<List<Text>> result = new ArrayList<>();
        List<Text> chunk = new ArrayList<>();

        for(Text text : texts) {
            if(text.getString().isEmpty()) {
                if(!chunk.isEmpty()) {
                    result.add(chunk);
                    chunk = new ArrayList<>();
                }
                continue;
            }
            chunk.add(text);
        }
        if(!chunk.isEmpty()) result.add(chunk);

        return result;
    }

    default Text[] mergeTextFromBiSection(List<Text> texts) {
        MutableText text1 = Text.empty();
        MutableText text2 = Text.empty();

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for(Text text : texts) {
            AtomicInteger i = new AtomicInteger();
            text.visit((s, t) -> {
                if(t.matches("§.")) return Optional.empty();
                if(s.getFont().equals(Identifier.of("minecraft:space"))) {
                    i.getAndIncrement();
                    return Optional.empty();
                }
                if(i.get() == 1) {
                    if(text1.getSiblings().isEmpty()) text1.append(Text.literal(t).setStyle(s));
                    else sb1.append(t);
                }
                else if(i.get() == 2) {
                    if(text2.getSiblings().isEmpty()) text2.append(Text.literal(t).setStyle(s));
                    else sb2.append(t);
                }
                return Optional.empty();
            }, Style.EMPTY);
        }
        text1.append(Text.literal(sb1.toString()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        text2.append(Text.literal(sb2.toString()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        return new MutableText[]{text1, text2};
    }

    default List<Text> placeTextToBiSection(List<List<Text>> modifiedTextLists) {
        List<List<Text>> textLists = new ArrayList<>(modifiedTextLists);
        List<Text> result = new ArrayList<>();

        while(textLists.size() >= 2) {
            List<Text> text1 = textLists.removeFirst();
            List<Text> text2 = textLists.removeFirst();
            int longest = Math.max(text1.size(), text2.size());
            for(int i = 0; i < longest; i++) {
                Text elem1 = text1.size() > i ? text1.get(i) : Text.empty();
                Text elem2 = text2.size() > i ? text2.get(i) : Text.empty();
                result.add(twoColumnAlign(elem1, elem2));
            }
            if(!textLists.isEmpty()) result.add(Text.empty());
        }
        if(!textLists.isEmpty()) {
            List<Text> text = textLists.removeFirst();
            for(Text value : text) {
                result.add(centerAlign(value));
            }
        }

        return result;
    }
}
