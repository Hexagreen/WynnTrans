package net.hexagreen.wynntrans.text;

import com.wynntils.core.components.Managers;
import com.wynntils.features.tooltips.ItemStatInfoFeature;
import com.wynntils.utils.mc.ComponentUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.reflect.Method;

public interface IWynntilsFeature {
    static MutableText coloringPerfectItem(Text text) {
        if(WynnTrans.wynntilsLoaded) {
            try {
                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.perfect.get()) throw new Exception();
                Class<?> obfTextClass = ComponentUtils.class;
                Method toStringMethod = obfTextClass.getMethod("makeRainbowStyle", String.class);
                Object result = toStringMethod.invoke(null, text.getString());
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }

    static MutableText coloringDefectiveItem(Text text) {
        if(WynnTrans.wynntilsLoaded) {
            try {
                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.defective.get()) throw new Exception();
                Class<?> obfTextClass = ComponentUtils.class;
                Method toStringMethod = obfTextClass.getMethod("makeObfuscated", String.class, float.class, float.class);
                Object result = toStringMethod.invoke(null, text.getString(), info.obfuscationChanceStart.get(), info.obfuscationChanceEnd.get());
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }
}
