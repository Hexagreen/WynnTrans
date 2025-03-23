package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface ITooltipSplitter {
    Text WRONG_SPLITTER = Text.literal("§7");
    Text SPLITTER = Text.literal(" ");

    static List<Text> correctSplitter(List<Text> texts) {
        return texts.stream()
                .map(text -> text.getString().equals(WRONG_SPLITTER.getString()) || text.getString().isEmpty() ? SPLITTER : text)
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
