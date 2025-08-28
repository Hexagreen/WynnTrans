package net.hexagreen.wynntrans.text.title;

import net.hexagreen.wynntrans.text.title.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public enum TitleType {
    EMERALD_POUCH(EmeraldPouch::new, EmeraldPouch::typeChecker, false),
    INGREDIENT_POUCH(IngredientPouch::new, IngredientPouch::typeChecker, false),
    POTION_BUNDLE(PotionBundle::new, PotionBundle::typeChecker, false),
    AREA_UNLOCKED(AreaUnlocked::new, AreaUnlocked::typeChecker, false),
    DISCOVERY_FOUND(DiscoveryFound::new, DiscoveryFound::typeChecker, false),

    PROXY_TITLE(ProxySimpleTitle::new, ProxySimpleTitle::typeChecker, true),
    PROXY_SUBTITLE(ProxySimpleTitle::new, ProxySimpleTitle::typeChecker, false),
    NO_TYPE(SimpleTitle::new, null, true);


    private static final List<TitleType> CACHED_CHECKER_TITLE =
            Arrays.stream(TitleType.values())
                    .filter(e -> e.typeChecker != null)
                    .filter(e -> e.isTitle)
                    .toList();
    private static final List<TitleType> CACHED_CHECKER_SUBTITLE =
            Arrays.stream(TitleType.values())
                    .filter(e -> e.typeChecker != null)
                    .filter(e -> !e.isTitle)
                    .toList();
    private final Function<Text, WynnTitleText> wtt;
    private final Predicate<Text> typeChecker;
    private final boolean isTitle;

    private static TitleType findType(Text text, boolean isTitle) {
        if(isTitle) {
            for(TitleType c : CACHED_CHECKER_TITLE) {
                if(c.typeChecker.test(text)) return c;
            }
        }
        else {
            for(TitleType c : CACHED_CHECKER_SUBTITLE) {
                if(c.typeChecker.test(text)) return c;
            }
        }
        return NO_TYPE;
    }

    public static Text findAndRun(Text text, boolean isTitle) {
        TitleType find = findType(text, isTitle);
        if(find == null) return text;
        return find.wtt.apply(text).text();
    }

    TitleType(Function<Text, WynnTitleText> wtt, Predicate<Text> typeChecker, boolean isTitle) {
        this.wtt = wtt;
        this.typeChecker = typeChecker;
        this.isTitle = isTitle;
    }
}
