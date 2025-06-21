package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.tooltip.types.IdentificationTooltip;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public abstract class AbstractWynnItem extends WynnTooltipText implements ITradableItem {
    protected Consumer<Text> textAppender;
    protected Consumer<Text> optionalTextAppender;

    public AbstractWynnItem(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
        this.textAppender = resultText::append;
        this.optionalTextAppender = resultText::append;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, textAppender, this::buildStep);
    }

    protected abstract void buildStep(List<Text> seg, int step, final int lastIndex);

    protected void translateReqSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            String[] headAndNum = t.getSiblings().getLast().getString().split(": ");
            Text icon = t.getSiblings().getFirst();
            Text reformedIcon = Text.literal(icon.getString().replaceFirst(" ", "")).setStyle(icon.getStyle());
            Text body;
            if(headAndNum[0].contains("Class Req")) {
                String className = headAndNum[1].split("/")[0].toLowerCase(Locale.ENGLISH);
                Text reqClass = Text.translatable("wytr.class." + className)
                        .append("/").append(Text.translatable("wytr.class." + className + "_alt"));
                body = Text.translatable("wytr.requirement.class", reqClass);
            }
            else if(headAndNum[0].contains("Combat Lv. Min")) {
                body = Text.translatable("wytr.requirement.combat", headAndNum[1]);
            }
            else if(headAndNum[0].matches(" (Strength|Dexterity|Intelligence|Defence|Agility) Min")) {
                Text reqSkill = CharacterSkill.getSkill(headAndNum[0].split(" ")[1]).getStat().copyContentOnly();
                body = Text.translatable("wytr.requirement.skill", reqSkill, headAndNum[1]);
            }
            else if(headAndNum[0].contains("Quest Req")) {
                String questVal = headAndNum[1];
                String questKey = "wytr.quest." + normalizeStringForKey(questVal);
                WTS.checkTranslationExist(questKey, questVal);
                Text questName = Text.translatableWithFallback(questKey, questVal);
                body = Text.translatable("wytr.requirement.quest", questName);
            }
            else if(headAndNum[0].contains("ing Lv. Min")) {
                Profession _profession = Profession.getProfession(t.getSiblings().get(2).getString().charAt(0));
                Text profession = _profession.getIcon().setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(" ")
                        .append(_profession.getText().setStyle(GRAY));
                String level = headAndNum[1];
                body = Text.translatable("wytr.requirement.profession", profession, level);
            }
            else {
                dump.add(t);
                return;
            }

            Text line = Text.empty().setStyle(GRAY)
                    .append(reformedIcon).append(" ").append(body);
            dump.add(line);
        });

        dump.forEach(textAppender);
    }

    protected void translateIDSection(List<Text> texts) {
        List<Text> translatedIDs = new IdentificationTooltip(texts).text();
        translatedIDs.forEach(t -> {
            if(t.getContent().toString().equals("empty")) {
                textAppender.accept(t);
            }
            else {
                optionalTextAppender.accept(t);
            }
        });
    }
}
