package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentBookProgress extends WynnTooltipText implements ISpaceProvider {

    private final boolean isSecretDiscovery;

    public static boolean typeChecker(List<Text> text) {
        return text.getFirst().getString().matches(".+ Progress$")
                && text.getLast().getString().matches(".+\\d+ of \\d+ completed");
    }

    public ContentBookProgress(List<Text> text) {
        super(colorCodedToStyledBatch(text));
        this.isSecretDiscovery = getSibling(2).getString().equals("Categories");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "tooltip.contentProgress";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = new SimpleTooltip(getSiblings().subList(0, 3)).textAsMutable();
        int tooltipLength = getSiblings().size();
        translateAndAlignText(getSiblings().subList(3, tooltipLength - 3));

        Text progressBar = getProgressBar(tooltipLength - 2);
        Text completionCount = getCompletionCount();

        int width = getLongestWidth(resultText.getSiblings(), progressBar);
        resultText.append(" ")
                .append(getCenterIndent(progressBar, width).append(progressBar))
                .append(getCenterIndent(completionCount, width).append(completionCount));
    }

    private void translateAndAlignText(List<Text> list) {
        appendAlignment(list.parallelStream().map(isSecretDiscovery ? this::translateDiscoveryAreaCategory : this::translateLevelRangeCategory).toList());
    }

    private List<Text> translateLevelRangeCategory(Text text) {
        MutableText text1 = Text.empty();
        MutableText text2 = Text.empty();
        List<Text> siblings = text.getSiblings();

        Matcher matcher = Pattern.compile("Lv\\. (\\d+) to (\\d+)").matcher(siblings.get(1).getString());
        boolean ignore = matcher.find();
        text1.append(siblings.getFirst())
                .append(Text.translatable(parentKey + ".lvRange", matcher.group(1), matcher.group(2)).setStyle(siblings.get(1).getStyle()));
        for(int i = 2; i < siblings.size(); i++) {
            text2.append(siblings.get(i));
        }
        List<Text> textList = new ArrayList<>();
        textList.add(text1);
        textList.add(text2);
        return textList;
    }

    private List<Text> translateDiscoveryAreaCategory(Text text) {
        MutableText text1 = Text.empty();
        MutableText text2 = Text.empty();
        List<Text> siblings = text.getSiblings();

        String discoveryAreaKey = rootKey + "discovery.area." + normalizeStringForKey(siblings.get(1).getString());
        text1.append(siblings.getFirst())
                .append(Text.translatable(discoveryAreaKey).setStyle(siblings.get(1).getStyle()));
        for(int i = 2; i < siblings.size(); i++) {
            text2.append(siblings.get(i));
        }
        List<Text> textList = new ArrayList<>();
        textList.add(text1);
        textList.add(text2);
        return textList;
    }

    private void appendAlignment(List<List<Text>> list) {
        int max = -1;
        for(List<Text> bundle : list) {
            max = Math.max(max, MinecraftClient.getInstance().textRenderer.getWidth(bundle.getFirst()));
        }

        for(List<Text> bundle : list) {
            Text front = bundle.getFirst();
            Text back = bundle.getLast();
            Text alignment = getRearSpace(front, max);
            Text merged = Text.empty().append(front).append(alignment).append(back);
            resultText.append(merged);
        }
    }

    private Text getProgressBar(int index) {
        MutableText result = Text.empty();
        List<Text> siblings = getSibling(index).getSiblings();
        for(int i = 1; i < siblings.size(); i++) {
            result.append(siblings.get(i));
        }
        return result;
    }

    private Text getCompletionCount() {
        Text completionCount = getSibling(getSiblings().size() - 1).getSiblings().getLast();
        Matcher matcher = Pattern.compile("(\\d+) of (\\d+) completed").matcher(completionCount.getString());
        boolean ignore = matcher.find();
        return Text.translatable(parentKey + ".completionCount", matcher.group(1), matcher.group(2)).setStyle(completionCount.getStyle());
    }
}
