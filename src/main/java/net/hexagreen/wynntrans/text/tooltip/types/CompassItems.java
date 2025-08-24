package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.*;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.types.SyndicatePromotion;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class CompassItems extends WynnTooltipText implements ITooltipSplitter, ISpaceProvider {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE003")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public CompassItems(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        switch(Items.match(getSibling(0).getString())) {
            case ABILITY_TREE -> buildAbilityTree();
            case SKILL_CRYSTAL -> buildSkillCrystal();
            case UPGRADE_SKILL -> buildUpgradeSkill();
            case DAILY_REWARD -> buildDailyReward();
            case RECRUIT -> buildRecruit();
            case PLAYER_INFO -> buildPlayerInfo();
            case MASTERY_TOME -> buildMasteryTome();
            case COMBAT -> buildCombat();
            case PROFESSION -> buildProfession();
            case SHOP -> buildShop();
            case GUILD -> buildGuild();
        }
    }

    private void buildAbilityTree() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Ability Tree")) {
                resultText.append(Text.translatable("wytr.tooltip.compassItems.abilityTree")
                        .setStyle(getSibling(0).getSiblings().getFirst().getStyle()));
                wrapLine(Text.translatable("wytr.tooltip.compassItems.abilityTree.desc").setStyle(GRAY), 300)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Click to Open")) {
                resultText.append(Text.empty().append(seg.getFirst().getSiblings().getFirst()).append(" ")
                        .append(Text.translatable("wytr.func.clickToOpen").setStyle(Style.EMPTY.withColor(Formatting.GREEN))));
            }
            else {
                new AbilityTreeOrb(seg).text().forEach(resultText::append);
            }
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildSkillCrystal() {
        resultText.append(Text.translatable("wytr.tooltip.compassItems.skillCrystal")
                .setStyle(getSibling(0).getSiblings().getFirst().getStyle()));
        if(!getSibling(1).getString().isBlank()) {
            wrapLine(Text.translatable("wytr.tooltip.compassItems.skillCrystal.warn").setStyle(GRAY), 300)
                    .forEach(resultText::append);
            resultText.append(SPLITTER);
            resultText.append(Text.translatable("wytr.func.clickAgainToConfirm").formatted(Formatting.GREEN));
            return;
        }

        Text unusedPoint = getSibling(2).getSiblings().get(1);

        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.compassItems.skillCrystal.desc", unusedPoint).setStyle(GRAY), 300)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        if(getSiblings().size() == 6) {
            Text line = Text.empty().append(getSibling(-1).getSiblings().getFirst()).append(" ")
                    .append(Text.translatable("wytr.tooltip.compassItems.skillCrystal.reset").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
            resultText.append(line);
        }
        else {
            wrapLine(Text.translatable("wytr.tooltip.compassItems.skillCrystal.resetLocked").setStyle(Style.EMPTY.withColor(Formatting.RED)), 300)
                    .forEach(resultText::append);
        }
    }

    private void buildUpgradeSkill() {
        Text _skill = getSibling(0).getSiblings().get(3);
        CharacterSkill skill = CharacterSkill.getSkill(_skill.getString().replaceFirst("^. ", ""));
        Text stat = skill.getStatWithIcon().setStyle(_skill.getStyle());
        Text element = Text.empty().setStyle(_skill.getStyle()).append(skill.getIcon().copy()).append(" ")
                .append(skill.getRelativeElement().getElement());

        if(getSibling(1).getString().equals("Removing Skill Points may")) {
            resultText.append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill", stat)
                    .setStyle(getSibling(0).getSiblings().getLast().getStyle()));
            wrapLine(Text.translatable("wytr.tooltip.compassItems.skillCrystal.warn").setStyle(GRAY), 300)
                    .forEach(resultText::append);
            resultText.append(SPLITTER);
            resultText.append(Text.translatable("wytr.func.clickAgainToConfirm").formatted(Formatting.GREEN));
            return;
        }

        Deque<Deque<Text>> segments = new ArrayDeque<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);
        Text tooltipWidthFixer = Text.translatable("wytr.tooltip.compassItems.upgradeSkill.tooltipWidthFixer");
        int tooltipWidth = ISpaceProvider.TEXT_RENDERER.getWidth(tooltipWidthFixer);

        segments.removeFirst().forEach(s -> {
            if(s.getString().matches("^.Upgrade your .+")) {
                resultText.append(centerAlign(Text.translatable("wytr.tooltip.compassItems.upgradeSkill", stat)
                        .setStyle(s.getSiblings().getLast().getStyle()), tooltipWidth));
            }
            else if(s.getString().isBlank()) resultText.append(tooltipWidthFixer);
            else if(s.getString().contains("Now")) {
                Text _now = s.getSiblings().get(1);
                Text _next = s.getSiblings().getLast();
                resultText.append(twoColumnAlign(
                        Text.translatable("wytr.tooltip.compassItems.upgradeSkill.now").setStyle(_now.getStyle()),
                        Text.translatable("wytr.tooltip.compassItems.upgradeSkill.next").setStyle(_next.getStyle()),
                        tooltipWidth
                ));
            }
            else if(s.getString().contains("%")) {
                Text v1 = s.getSiblings().get(1);
                Text v2 = s.getSiblings().getLast();
                if(s.getString().contains(">>>>>>")) {
                    resultText.append(twoColumnAlignWithSplitter(v1, v2, s.getSiblings().get(3), tooltipWidth));
                }
                else {
                    resultText.append(twoColumnAlign(v1, v2, tooltipWidth));
                }
            }
            else if(s.getString().contains("point")) {
                Text _v1 = s.getSiblings().get(1);
                Text _v2 = s.getSiblings().getLast();
                String v1 = _v1.getString().replaceFirst(" points?", "");
                String v2 = _v2.getString().replaceFirst(" points?", "");
                resultText.append(twoColumnAlign(
                        Text.translatable("wytr.tooltip.compassItems.upgradeSkill.point", v1).setStyle(_v1.getStyle()),
                        Text.translatable("wytr.tooltip.compassItems.upgradeSkill.point", v2).setStyle(_v2.getStyle()),
                        tooltipWidth
                ));
            }
            else if(s.getString().contains("Modified")) {
                resultText.append(centerAlign(Text.empty().append(s.getSiblings().get(1))
                                .append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.modified")
                                        .setStyle(s.getSiblings().get(2).getStyle())),
                        tooltipWidth
                ));
            }
        });
        resultText.append(SPLITTER);
        segments.removeFirst();
        wrapLine(Text.translatable("wytr.tooltip.compassItems.upgradeSkill." + skill.name().toLowerCase(Locale.ENGLISH), element).setStyle(GRAY), tooltipWidth)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        if(segments.getFirst().getFirst().getString().contains("Spell Cost Reduction: ")) {
            Text reduction = segments.removeFirst().getFirst().getSiblings().getLast();
            resultText.append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.spellCostReduction", reduction));
            resultText.append(SPLITTER);
        }
        segments.removeFirst().forEach(s -> {
            Text icon = s.getSiblings().getFirst();
            if(s.getString().contains("Left-Click")) {
                Text line = Text.empty().append(icon).append(" ")
                        .append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.up")
                                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                resultText.append(line);
            }
            else if(s.getString().contains("Right-Click")) {
                Text line = Text.empty().append(icon).append(" ")
                        .append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.down")
                                .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                resultText.append(line);
            }
            else if(s.getString().contains("Hold Shift")) {
                Text line = Text.empty().append(icon).append(" ")
                        .append(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.bulk")
                                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
                resultText.append(line);
            }
            else if(s.getString().contains("You need")) {
                wrapLine(Text.translatable("wytr.tooltip.compassItems.upgradeSkill.warn").setStyle(Style.EMPTY.withColor(Formatting.RED)), tooltipWidth)
                        .forEach(resultText::append);
            }
        });
        if(!segments.isEmpty()) segments.forEach(s -> s.forEach(resultText::append));
    }

    private void buildDailyReward() {
        Text _streak = getSibling(4).getSiblings().getLast();
        Text streak = Text.literal(_streak.getString().replaceAll("\\D", "")).setStyle(_streak.getStyle());

        MutableText temp = Text.empty();
        temp.append(Text.translatable("wytr.tooltip.compassItems.dailyReward").setStyle(getSibling(0).getSiblings().getFirst().getStyle()));
        temp.append(Text.translatable("wytr.tooltip.compassItems.dailyReward.desc").setStyle(GRAY));
        temp.append(Text.literal(" "));
        temp.append(Text.translatable("wytr.tooltip.compassItems.dailyReward.streak", streak).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
        temp.append(Text.literal(" "));
        if(getSibling(-1).getString().contains("Available")) {
            Text time = ITime.translateTime(getSibling(-1).getString().replaceFirst("Available in ", ""));
            temp.append(Text.translatable("wytr.tooltip.compassItems.dailyReward.refresh", time).setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }
        else {
            MutableText line = Text.empty()
                    .append(getSibling(-1).getSiblings().getFirst()).append(" ")
                    .append(Text.translatable("wytr.tooltip.compassItems.dailyReward.claim").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            temp.append(line);
        }
        temp.getSiblings().forEach(t -> wrapLine(t, 150).forEach(resultText::append));
    }

    private void buildRecruit() {
        Text friends = getSibling(4).getSiblings().getLast();

        List<Text> temp = new ArrayList<>();
        temp.add(Text.translatable("wytr.tooltip.compassItems.recruit").setStyle(getSibling(0).getSiblings().getFirst().getStyle()));
        temp.add(Text.translatable("wytr.tooltip.compassItems.recruit.desc").setStyle(GRAY));
        temp.add(Text.literal(" "));
        temp.add(Text.translatable("wytr.tooltip.compassItems.recruit.count", friends).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)));
        temp.add(Text.literal(" "));
        temp.add(Text.empty().append(getSibling(-1).getSiblings().getFirst()).append(" ")
                .append(Text.translatable("wytr.func.clickToOpen").setStyle(Style.EMPTY.withColor(Formatting.GREEN))));
        temp.forEach(t -> wrapLine(t, 150).forEach(resultText::append));
    }

    private void buildPlayerInfo() {
        Deque<Text> lines = new ArrayDeque<>(getSiblings());
        Deque<Text> results = new ArrayDeque<>();

        results.add(lines.removeFirst());
        results.add(lines.removeFirst());

        if(lines.getFirst().getString().contains("Total Lv: ")) {
            Text _totalLevel = lines.removeFirst().getSiblings().getLast();
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.totalLevel", _totalLevel)
                    .setStyle(GRAY));
        }

        if(lines.getFirst().getString().contains("Combat Lv: ")) {
            Text _combatLevel = lines.removeFirst().getSiblings().getLast();
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.combatLevel", _combatLevel)
                    .setStyle(GRAY));
        }

        if(lines.getFirst().getString().contains("Class: ")) {
            Text _class = CharacterClass.getClassName(lines.removeFirst().getSiblings().getLast().getString())
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.class", _class)
                    .setStyle(GRAY));
        }

        if(lines.getFirst().getString().contains("Quests: ")) {
            Text _quest = lines.removeFirst().getSiblings().getLast();
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.quest", _quest)
                    .setStyle(GRAY));
        }

        if(lines.getFirst().getString().contains("XP: ")) {
            Text _exp = lines.removeFirst().getSiblings().getLast();
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.exp", _exp)
                    .setStyle(GRAY));
        }

        if(lines.isEmpty()) return;
        results.add(lines.removeFirst());

        if(lines.getFirst().getString().equals("Identifications: ")) {
            lines.removeFirst();
            results.add(Text.translatable("wytr.tooltip.compassItems.playerInfo.ids")
                    .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
        }

        while(!lines.isEmpty() && lines.getFirst().getString().matches("^- .+?: .+")) {
            Text _idLine = lines.removeFirst();
            results.add(Text.empty().setStyle(GRAY)
                    .append(_idLine.getSiblings().getFirst().copy())
                    .append(Identifications.findIdentification(
                            _idLine.getSiblings().get(1).getString().replaceFirst(": ", "")).getTranslatedText())
                    .append(": ")
                    .append(IdentificationTooltip.extractIDValue(_idLine.getSiblings().getLast())));
        }

        if(lines.isEmpty()) return;
        results.add(lines.removeFirst());

        if(lines.getFirst().getString().contains("Page ")) {
            String page = lines.removeFirst().getString().replaceFirst(".+Page ", "");
            MutableText alignedPage = Text.translatable("wytr.tooltip.compassItems.playerInfo.page", page)
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            results.add(alignedPage);
            List<Text> _page = lines.removeFirst().getSiblings();
            Text pageIcon = siblingsToText(_page.subList(1, _page.size()));
            results.add(pageIcon);
        }

        int longestWidth = getLongestWidth(results, results.getFirst().copy().append("    "));

        Deque<Text> tail = new ArrayDeque<>();
        tail.addFirst(results.removeLast());
        tail.addFirst(results.removeLast());

        MutableText alignedHead = centerAlign(results.removeFirst(), longestWidth);
        resultText.append(alignedHead.append(alignedHead.getSiblings().getFirst()));
        results.forEach(resultText::append);
        tail.forEach(t -> resultText.append(centerAlign(t, longestWidth)));

        if(!lines.isEmpty()) lines.forEach(resultText::append);
    }

    private void buildMasteryTome() {
        MutableText req = Text.literal(getSibling(4).getString().substring(0, 2))
                .setStyle(getSibling(4).getSiblings().getFirst().getStyle())
                .append(Text.translatable("wytr.requirement.quest", Text.translatable("wytr.quest.RealmofLightI-TheWormHoles")));
        if(getSibling(-1).getString().contains("Lv.")) {
            Text level = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
                    .append("[")
                    .append(Text.translatable("wytr.unit.level", getSibling(-1).getString().replaceAll("\\D", "")))
                    .append("]");
            req.append(" ").append(level);
        }

        List<Text> temp = new ArrayList<>();
        temp.add(Text.translatable("wytr.tooltip.compassItems.tome").setStyle(getSibling(0).getSiblings().getFirst().getStyle()));
        temp.add(Text.translatable("wytr.tooltip.compassItems.tome.desc").setStyle(GRAY));
        temp.add(Text.literal(" "));
        temp.add(req);
        temp.forEach(t -> wrapLine(t, 150).forEach(resultText::append));
    }

    private void buildCombat() {
        List<Deque<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);
        List<Text> dump = new ArrayList<>();
        Deque<Integer> alignTarget = new ArrayDeque<>();

        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            Text _header = segments.get(i).getFirst();
            if(i == 0) {
                dump.add(Text.translatable("wytr.tooltip.compassItems.combatInfo")
                        .setStyle(_header.getSiblings().getFirst().getStyle()));
            }
            else if(_header.getString().contains("❤ Health: ")) {
                segments.get(i).forEach(t -> {
                    Text value = t.getSiblings().getLast();
                    Text label = t.getSiblings().getFirst();
                    Text line;
                    if(label.getString().contains("Effective")) {
                        line = Text.literal("❤ ").setStyle(label.getStyle())
                                .append(Text.translatable("wytr.tooltip.compassItems.combatInfo.effectiveHealth", value));
                    }
                    else {
                        line = Text.literal("❤ ").setStyle(label.getStyle())
                                .append(Text.translatable("wytr.id.Health"))
                                .append(": ")
                                .append(value);
                    }
                    dump.add(line);
                });
            }
            else if(_header.getString().contains("Elemental Defences")) {
                dump.add(Text.translatable("wytr.id.ElementalDefence")
                        .setStyle(segments.get(i).removeFirst().getSiblings().getFirst().getStyle()));
                segments.get(i).forEach(t -> {
                    Text value = t.getSiblings().getLast();
                    String[] split = t.getString().replaceFirst("^- ", "").split(" ");
                    Elements _elem = Elements.findElement(split[1]);
                    Text elem = _elem.getElementWithIcon();
                    Text line = Text.empty().setStyle(GRAY)
                            .append(Text.literal("- ").setStyle(t.getSiblings().getFirst().getStyle()))
                            .append(Text.translatable("wytr.tooltip.baseStat.defence", elem).setStyle(GRAY))
                            .append(": ")
                            .append(value);
                    dump.add(line);
                });
            }
            else if(_header.getString().contains("Main Attack Damage") || _header.getString().contains("Spell Damage")) {
                if(_header.getString().contains("Main Attack Damage")) {
                    dump.add(Text.translatable("wytr.id.MainAttackDamage")
                            .setStyle(segments.get(i).removeFirst().getSiblings().getFirst().getStyle()));
                }
                else {
                    dump.add(_header.getSiblings().getFirst().copy()
                            .append(Text.translatable("wytr.id.SpellDamage")
                                    .setStyle(segments.get(i).removeFirst().getSiblings().getLast().getStyle())
                            )
                    );
                }
                segments.get(i).forEach(t -> {
                    Text value = t.getSiblings().getLast();
                    if(t.getString().contains("Average DPS")) {
                        Text line = t.getSiblings().getFirst().copy()
                                .append(Text.translatable("wytr.tooltip.compassItems.combatInfo.dps", value)
                                        .setStyle(GRAY));
                        dump.add(line);
                    }
                    else if(t.getString().contains("overall buff")) {
                        String scale = t.getString().replaceFirst(".*\\((-?\\d+\\.\\d+%).*", "$1");
                        dump.add(Text.translatable("wytr.tooltip.compassItems.combatInfo.loadoutScale", scale)
                                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
                        alignTarget.add(dump.size() - 1);
                    }
                    else {
                        String[] split = t.getString().replaceFirst("^- ", "").split(" ");
                        Elements _elem = Elements.findElement(split[1]);
                        Text elem = _elem.getElementWithIcon();
                        Text line = Text.empty().setStyle(GRAY)
                                .append(Text.literal("- ").setStyle(t.getSiblings().getFirst().getStyle()))
                                .append(Text.translatable("wytr.tooltip.baseStat.damage", elem))
                                .append(": ")
                                .append(value);
                        dump.add(line);
                    }
                });
            }
            else if(segments.get(i).getLast().getString().contains("« ")) {
                Text weapon = new ItemName(_header.getSiblings().get(1))
                        .noAddictionMode().returnNullMode().textAsMutable()
                        .setStyle(_header.getSiblings().get(1).getStyle());
                if(weapon == null) weapon = _header.getSiblings().get(1);
                dump.add(weapon);
                alignTarget.add(dump.size() - 1);

                List<Text> _page = segments.get(i).getLast().getSiblings();
                Text page = siblingsToText(_page.subList(1, _page.size()));
                dump.add(page);
                alignTarget.add(dump.size() - 1);
            }
            else {
                dump.addAll(segments.get(i));
            }
            if(i != segmentsSize - 1) dump.add(Text.literal(" "));
        }

        int maxLength = alignTarget.stream()
                .map(i -> ISpaceProvider.TEXT_RENDERER.getWidth(dump.get(i)))
                .max(Integer::compareTo).orElse(150);

        alignTarget.add(-1);

        for(int i = 0, dumpSize = dump.size(); i < dumpSize; i++) {
            if(i == alignTarget.getFirst()) {
                alignTarget.removeFirst();
                MutableText aligned = centerAlign(dump.get(i), maxLength + 10);
                resultText.append(aligned.append(aligned.getSiblings().getFirst()));
            }
            else {
                resultText.append(dump.get(i));
            }
        }
    }

    private void buildProfession() {
        List<Deque<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);

        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            Text _header = segments.get(i).getFirst();
            if(i == 0) {
                resultText.append(Text.translatable("wytr.tooltip.compassItems.professionInfo")
                        .setStyle(_header.getSiblings().getFirst().getStyle()));
                wrapLine(Text.translatable("wytr.tooltip.compassItems.professionInfo.desc")
                        .setStyle(GRAY), 150).forEach(resultText::append);
            }
            else if(_header.getString().equals("✮ Silverbull Syndicates:")) {
                resultText.append(Text.translatable("wytr.tooltip.compassItems.professionInfo.silverbull")
                        .setStyle(_header.getSiblings().getFirst().getStyle()));
                segments.get(i).removeFirst();
                segments.get(i).forEach(t -> {
                    Text bar = t.getSiblings().getFirst();
                    Text percent = t.getSiblings().getLast();
                    String[] _level = t.getSiblings().get(1).getString().split(" ", 2);
                    String division = _level[0];
                    String k = "wytr.syndicate.division." + division.toLowerCase(Locale.ENGLISH);
                    WTS.checkTranslationExist(k, division);
                    Text divisionName = Text.translatableWithFallback(k, division).setStyle(GRAY);
                    Text level = SyndicatePromotion.getTranslatedRankName(_level[1], Style.EMPTY).setStyle(GRAY);
                    resultText.append(Text.empty().append(bar).append(divisionName).append(" ").append(level).append(percent));
                });
            }
            else if(_header.getString().contains("ing Skills:")) {
                Text title = segments.get(i).removeFirst().getSiblings().getFirst();
                String type;
                if(title.getString().contains("Gathering")) type = "gathering";
                else if(title.getString().contains("Crafting")) type = "crafting";
                else {
                    resultText.append(title);
                    segments.get(i).forEach(resultText::append);
                    return;
                }
                resultText.append(Text.translatable("wytr.tooltip.compassItems.professionInfo." + type)
                        .setStyle(title.getStyle()));

                segments.get(i).forEach(t -> {
                    Text bar = t.getSiblings().getFirst();
                    Text percent = t.getSiblings().getLast();
                    String tmp = t.getSiblings().get(1).getString();
                    Profession skill = Profession.getProfession(tmp.charAt(0));
                    String _level = tmp.replaceFirst(".+Lv\\. (\\d+).+", "$1");
                    Text level = Text.translatable("wytr.unit.level", _level);
                    Text icon = skill.getIcon();
                    Text skillName = skill.getText();
                    resultText.append(Text.empty().setStyle(GRAY)
                            .append(bar).append(icon).append(" ")
                            .append(level).append(" ").append(skillName).append(percent));
                });
            }
            else {
                segments.get(i).forEach(resultText::append);
            }
            if(i != segmentsSize - 1) resultText.append(Text.literal(" "));
        }
    }

    private void buildShop() {
        List<Text> temp = new ArrayList<>();
        temp.add(Text.translatable("wytr.tooltip.compassItems.shop")
                .setStyle(getSibling(0).getSiblings().getFirst().getStyle().withBold(false)));
        temp.add(Text.translatable("wytr.tooltip.compassItems.shop.desc").setStyle(GRAY));
        temp.add(Text.literal(" "));
        Text click = Text.empty().append(getSibling(-1).getSiblings().getFirst()).append(" ")
                .append(Text.translatable("wytr.func.clickToOpen").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        temp.add(click);
        temp.forEach(t -> wrapLine(t, 150).forEach(resultText::append));
    }

    private void buildGuild() {
        getSiblings().forEach(resultText::append);
    }

    private enum Items {
        ABILITY_TREE("§b§lAbility Tree", "Ability Tree"),
        SKILL_CRYSTAL("§2§lSkill Crystal", "Skill Crystal"),
        UPGRADE_SKILL(".§dUpgrade your §[2ebcf]. .+§d skill", ".Upgrade your . .+ skill"),
        DAILY_REWARD("§6§lDaily Reward", "Daily Reward"),
        RECRUIT("§a§lRecruit a Friend", "Recruit a Friend"),
        PLAYER_INFO("§f§l.+'s? Info", ".+'s? Info"),
        MASTERY_TOME("§5§lMastery Tomes", "Mastery Tomes"),
        COMBAT("§c§lCombat Information", "Combat Information"),
        PROFESSION("§a§lProfession Info", "Profession Info"),
        SHOP("Store & Wardrobe", "Store & Wardrobe"),
        GUILD("§b§lView Your Guild", "View Your Guild");

        private final String original;
        private final String styled;

        static Items match(String string) {
            if(string.contains(WynnTrans.wynnPlayerName)) return PLAYER_INFO;
            return Arrays.stream(values()).parallel()
                    .filter(i -> string.matches(i.original) || string.matches(i.styled))
                    .findAny().orElse(null);
        }

        Items(String original, String styled) {
            this.original = original;
            this.styled = styled;
        }
    }
}