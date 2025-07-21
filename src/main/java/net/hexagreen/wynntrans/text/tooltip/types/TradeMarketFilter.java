package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.enums.Identifications;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class TradeMarketFilter extends WynnTooltipText implements ITooltipSplitter {
    private static final Text icon = Text.literal("\uE000")
            .setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")).withColor(Formatting.WHITE));
    private static final Text ADD = Text.empty().append(icon).append(" ")
            .append(Text.translatable("wytr.func.clickToAdd").formatted(Formatting.GREEN));

    private static final Text REMOVE = Text.empty().append(icon).append(" ")
            .append(Text.translatable("wytr.func.clickToRemove").formatted(Formatting.GREEN));

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE010")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public static Text findFilterName(Text target) {
        String trimmed = target.getString().trim();
        if(Items.match(trimmed) == null) return target;
        return new TradeMarketFilter(textToList(Text.literal(trimmed).setStyle(target.getStyle()))).text().getFirst();
    }

    public TradeMarketFilter(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        BiConsumer<List<Text>, List<Object>> runner = switch(Items.match(getSibling(0).getString())) {
            case NAME -> this::buildName;
            case LEVEL -> this::buildLevel;
            case RARITY -> this::buildRarity;
            case PROFESSION -> this::buildProfession;
            case TIER -> this::buildTier;
            case TYPE -> this::buildType;
            case GROUP -> this::buildGroup;
            case DROP -> this::buildDrop;
            case ID -> this::buildID;
        };
        buildFilter(runner);
    }

    private void buildFilter(BiConsumer<List<Text>, List<Object>> loopStep) {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        List<Object> args = new ArrayList<>();
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.contains("Click to add")) {
                resultText.append(ADD);
            }
            else if(_string.contains("Click to remove")) {
                resultText.append(REMOVE);
            }
            else loopStep.accept(seg, args);
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void buildName(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.equals("Name Contains")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.name").setStyle(style));
        }
        else if(_string.equals("The name of the item")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.name.desc").formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else if(_string.contains("Current: ")) {
            Text query = seg.getFirst().getSiblings().getLast();
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.name.current", query).formatted(Formatting.GRAY));
        }
        else seg.forEach(resultText::append);
    }

    private void buildLevel(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches("Level .+ Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String level = _string.replaceAll("Level | Only", "");
            args.addAll(List.of(level.split("-")));
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.level", level).setStyle(style));
        }
        else if(_string.equals("Only display items with combat")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.level.desc", args.getFirst(), args.getLast()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildRarity(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Items Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String rarity = ItemRarity.getRarity((_string.replaceAll(" Items Only", ""))).getString();
            args.add(rarity);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.rarity", rarity).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.rarity.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildProfession(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Skill Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            Text skill = Profession.getProfession(seg.getFirst().getSiblings().getFirst().getString().charAt(0)).getTextWithIcon();
            args.add(skill);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.profession", skill).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.profession.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildTier(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches("Tier \\d Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String num = seg.getFirst().getString().replaceAll("\\D", "");
            args.add(num);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.tier", num).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.tier.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildType(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Type Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String type = EquipmentAndConsumables.getText(seg.getFirst().getString().replaceFirst(" Type Only", "")).getString();
            args.add(type);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.type", type).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.type.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildGroup(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Only$")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String _group = seg.getFirst().getString().split(" ")[0].replaceFirst("s$", "").toLowerCase(Locale.ENGLISH);
            String group = Text.translatable("wytr.tooltip.tradeMarket.filter.group." + _group).getString();
            args.add(group);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.group", group).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.group.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildDrop(List<Text> seg, List<Object> args) {
        boolean never = seg.getLast().getString().contains("Never");
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Drops Only$")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            if(never) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.neverDrop").setStyle(style));
                return;
            }
            String _drop = seg.getFirst().getString().split(" ")[0].toLowerCase(Locale.ENGLISH);
            String drop = Text.translatable("wytr.tooltip.tradeMarket.filter.drop." + _drop).getString();
            args.add(drop);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.drop", drop).setStyle(style));
        }
        else if(_string.contains("Only display ")) {
            if(never) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.neverDrop.desc").formatted(Formatting.GRAY));
                return;
            }
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.drop.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private void buildID(List<Text> seg, List<Object> args) {
        String _string = seg.getFirst().getString();
        if(_string.matches(".+ Identification Only")) {
            Style style = seg.getFirst().getSiblings().getLast().getStyle();
            String id = Identifications.findIdentification(seg.getFirst().getString().replaceFirst(" Identification Only", ""))
                    .getTranslatedText().getString();
            args.add(id);
            resultText.append(Text.translatable("wytr.tooltip.tradeMarket.filter.id", id).setStyle(style));
        }
        else if(_string.contains("Must have ")) {
            wrapLine(Text.translatable("wytr.tooltip.tradeMarket.filter.id.desc", args.getFirst()).formatted(Formatting.GRAY), 150)
                    .forEach(resultText::append);
        }
        else seg.forEach(resultText::append);
    }

    private enum Items {
        NAME("Name Contains"),
        LEVEL("Level .+ Only"),
        RARITY("^(?!Other).+ Items Only$"),
        PROFESSION(".+ Skill Only"),
        TIER("Tier \\d Only"),
        TYPE(".+ Type Only"),
        GROUP("(Unrefined Materials|Refined Materials|Ingredients|Tools|Powders|Other Items) Only"),
        DROP(".+ Drops Only"),
        ID(".+ Identification Only");

        private final String styled;

        static Items match(String string) {
            return Arrays.stream(values()).parallel()
                    .filter(i -> string.matches(i.styled))
                    .findAny().orElse(null);
        }

        Items(String styled) {
            this.styled = styled;
        }
    }
}
