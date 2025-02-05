package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.text.tooltip.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum TooltipType {
    DIALOG_HISTORY(DialogHistory::new, DialogHistory::typeChecker),
    CONTENT_BOOK_FILTER_AND_SORT(ContentBookFilterAndSort::new, ContentBookFilterAndSort::typeChecker),
    CONTENT_BOOK_CONTENT_PROGRESS(ContentBookProgress::new, ContentBookProgress::typeChecker),
    CONTENT_BOOK_CONTENTS(ContentBookNodes::new, ContentBookNodes::typeChecker),
    ITEM_NAME(ItemName::new, ItemName::typeChecker),

    NO_TYPE(SimpleTooltip::new, null);

    private final Function<List<Text>, WynnTooltipText> wtt;
    private final Predicate<List<Text>> typeChecker;

    private static TooltipType findType(List<Text> text) {
        return Arrays.stream(values())
                .filter(tooltipType -> Objects.nonNull(tooltipType.typeChecker))
                .filter(tooltipType -> tooltipType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static List<Text> findAndRun(List<Text> text) {
        TooltipType find = findType(text);
        if(find == null) return text;
        return find.wtt.apply(text).text();
    }

    TooltipType(Function<List<Text>, WynnTooltipText> wtt, Predicate<List<Text>> typeChecker) {
        this.wtt = wtt;
        this.typeChecker = typeChecker;
    }
}
