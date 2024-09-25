package net.hexagreen.wynntrans.text.tooltip;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.text.tooltip.types.ContentBookFilter;
import net.hexagreen.wynntrans.text.tooltip.types.DialogHistory;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public enum TooltipType {
	DIALOG_HISTORY(DialogHistory.class, DialogHistory::typeChecker),
	CONTENT_BOOK_FILTER(ContentBookFilter.class, ContentBookFilter::typeChecker),

	NO_TYPE(SimpleTooltip.class, null);
	private final Class<? extends WynnTooltipText> wtt;
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
		try {
			return find.wtt.cast(find.wtt.getConstructor(List.class).newInstance(text)).text();
		} catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LogUtils.getLogger().warn("TooltipType.findAndRun has thrown exception.", e);
			return text;
		}
	}

	TooltipType(Class<? extends WynnTooltipText> wdt, Predicate<List<Text>> typeChecker) {
		this.wtt = wdt;
		this.typeChecker = typeChecker;
	}
}
