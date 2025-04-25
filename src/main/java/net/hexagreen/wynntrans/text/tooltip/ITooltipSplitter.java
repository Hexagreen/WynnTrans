package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface ITooltipSplitter {
    String WRONG_SPLITTER = "§7 ?";
    Text SPLITTER = Text.literal(" ");

    static List<Text> correctSplitter(List<Text> texts) {
        return correctSplitter(texts, WRONG_SPLITTER);
    }

    static List<Text> correctSplitter(List<Text> texts, String wrongSplitterAsRegex) {
        return texts.stream()
                .map(text -> text.getString().matches(wrongSplitterAsRegex) || text.getString().isEmpty() ? SPLITTER : text)
                .toList();
    }

    default void splitTooltipToSegments(List<Text> siblings, List<List<Text>> segmentStorage) {
        List<Text> segment = new ArrayList<>();

        for(Text text : siblings) {
            if(text.getString().equals(SPLITTER.getString())) {
                if(segment.isEmpty()) continue;
                segmentStorage.add(segment);
                segment = new ArrayList<>();
            }
            else {
                segment.add(text);
            }
        }
        if(!segment.isEmpty()) segmentStorage.add(segment);
    }
}
