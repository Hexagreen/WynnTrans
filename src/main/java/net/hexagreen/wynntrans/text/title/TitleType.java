package net.hexagreen.wynntrans.text.title;

import net.hexagreen.wynntrans.text.title.types.CrateRewardTier;
import net.hexagreen.wynntrans.text.title.types.EmeraldPouch;
import net.hexagreen.wynntrans.text.title.types.IngredientPouch;
import net.hexagreen.wynntrans.text.title.types.SimpleTitle;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum TitleType {
    EMERALD_POUCH(EmeraldPouch::new, EmeraldPouch::typeChecker, true),
    INGREDIENT_POUCH(IngredientPouch::new, IngredientPouch::typeChecker, true),
    CRATE_REWARD_TIER(CrateRewardTier::new, CrateRewardTier::typeChecker, false),

    PROXY_TITLE(ProxySimpleTitle::new, ProxySimpleTitle::typeChecker, true),
    PROXY_SUBTITLE(ProxySimpleTitle::new, ProxySimpleTitle::typeChecker, false),
    NO_TYPE(SimpleTitle::new, null, true);

    private final Function<Text, WynnTitleText> wtt;
    private final Predicate<Text> typeChecker;
    private final boolean isTitle;

    private static TitleType findType(Text text, boolean isTitle) {
        return Arrays.stream(values())
                .filter(titleType -> Objects.nonNull(titleType.typeChecker))
                .filter(titleType -> titleType.isTitle == isTitle)
                .filter(titleType -> titleType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
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
