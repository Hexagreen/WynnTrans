package net.hexagreen.wynntrans.text;

import com.wynntils.core.components.Managers;
import com.wynntils.features.tooltips.ItemStatInfoFeature;
import com.wynntils.utils.mc.ComponentUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WynntilsCompatible {
    private static final Map<String, Class<?>> cachedClasses = new HashMap<>();
    private static final Map<String, Executable> cachedMethods = new HashMap<>();

    public static boolean tossToWynntilsTextHandleEvent(Text printLine) {
        try {
            Class<?> chatPacketReceivedEvent = cachedClasses.computeIfAbsent("eventClass", k -> {
                try {
                    return Class.forName("com.wynntils.mc.event.ChatPacketReceivedEvent");
                }
                catch(ClassNotFoundException e) {
                    return null;
                }
            });
            if(chatPacketReceivedEvent == null) return false;

            Class<?> mixinHelper = cachedClasses.computeIfAbsent("helper", k -> {
                try {
                    return Class.forName("com.wynntils.core.events.MixinHelper");
                }
                catch(ClassNotFoundException e) {
                    return null;
                }
            });
            if(mixinHelper == null) return false;


            Constructor<?> eventConstructor = (Constructor<?>) cachedMethods.computeIfAbsent("createEvent", k -> {
                for(Constructor<?> ec : chatPacketReceivedEvent.getDeclaredConstructors()) {
                    Class<?>[] params = ec.getParameterTypes();
                    if(params.length == 1 && Text.class.isAssignableFrom(params[0])) {
                        return ec;
                    }
                }
                return null;
            });
            if(eventConstructor == null) return false;

            Method isCanceled = (Method) cachedMethods.computeIfAbsent("isCanceled", k -> {
                try {
                    return chatPacketReceivedEvent.getMethod("isCanceled");
                }
                catch(NoSuchMethodException e) {
                    return null;
                }
            });
            if(isCanceled == null) return false;

            Method isMessageChanged = (Method) cachedMethods.computeIfAbsent("isMessageChanged", k -> {
                try {
                    return chatPacketReceivedEvent.getMethod("isMessageChanged");
                }
                catch(NoSuchMethodException e) {
                    return null;
                }
            });
            if(isMessageChanged == null) return false;

            Method getMessage = (Method) cachedMethods.computeIfAbsent("getMessage", k -> {
                try {
                    return chatPacketReceivedEvent.getMethod("getMessage");
                }
                catch(NoSuchMethodException e) {
                    return null;
                }
            });
            if(getMessage == null) return false;

            Method postEvent = (Method) cachedMethods.computeIfAbsent("post", k -> {
                try {
                    return mixinHelper.getMethod("post", Class.forName("net.neoforged.bus.api.Event"));
                }
                catch(NoSuchMethodException | ClassNotFoundException e) {
                    return null;
                }
            });
            if(postEvent == null) return false;

            Object wynntilsEvent = eventConstructor.newInstance(printLine);
            postEvent.invoke(null, wynntilsEvent);
            if((Boolean) isCanceled.invoke(wynntilsEvent)) return true;
            if((Boolean) isMessageChanged.invoke(wynntilsEvent)) {
                WynnTransText.transportMessage((Text) getMessage.invoke(wynntilsEvent));
                return true;
            }
        }
        catch(Exception ignore) {
        }
        return false;
    }

    public static MutableText coloringPerfectItem(Text text) {
        if(WynnTrans.wynntilsLoaded) {
            try {
                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.perfect.get()) throw new Exception();
                Class<?> componentUtils = ComponentUtils.class;
                Method rainbow = componentUtils.getMethod("makeRainbowStyle", String.class);
                Object result = rainbow.invoke(null, text.getString());
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }

    public static MutableText coloringDefectiveItem(Text text) {
        if(WynnTrans.wynntilsLoaded) {
            try {
                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.defective.get()) throw new Exception();
                Class<?> componentUtils = ComponentUtils.class;
                Method toStringMethod = componentUtils.getMethod("makeObfuscated", String.class, float.class, float.class);
                Object result = toStringMethod.invoke(null, text.getString(), info.obfuscationChanceStart.get(), info.obfuscationChanceEnd.get());
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }
}
