package net.hexagreen.wynntrans.text;

import com.wynntils.core.components.Managers;
import com.wynntils.core.text.StyledText;
import com.wynntils.features.tooltips.ItemStatInfoFeature;
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
    private static boolean cacheDone = false;

    private static boolean cacheClassAndMethod() {
        if(!WynnTrans.wynntilsLoaded) return false;
        if(cacheDone) return true;
        Class<?> chatPacketReceivedEvent = cachedClasses.computeIfAbsent("eventClass", k -> {
            try {
                return Class.forName("com.wynntils.mc.event.ChatPacketReceivedEvent$System");
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

        Class<?> componentUtils = cachedClasses.computeIfAbsent("componentUtils", k -> {
            try {
                return Class.forName("com.wynntils.utils.mc.ComponentUtils");
            }
            catch(ClassNotFoundException e) {
                return null;
            }
        });
        if(componentUtils == null) return false;


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

        Method makeRainbowStyle = (Method) cachedMethods.computeIfAbsent("makeRainbowStyle", k -> {
            try {
                return componentUtils.getMethod("makeRainbowStyle", String.class, boolean.class);
            }
            catch(NoSuchMethodException e) {
                return null;
            }
        });
        if(makeRainbowStyle == null) return false;

        Method makeObfuscated = (Method) cachedMethods.computeIfAbsent("makeObfuscated", k -> {
            try {
                return componentUtils.getMethod("makeObfuscated", String.class, float.class, float.class);
            }
            catch(NoSuchMethodException e) {
                return null;
            }
        });
        if(makeObfuscated == null) return false;

        Method getComponent = (Method) cachedMethods.computeIfAbsent("getComponent", k -> {
            try {
                return StyledText.class.getMethod("getComponent");
            }
            catch(NoSuchMethodException e) {
                return null;
            }
        });
        if(getComponent == null) return false;

        Method fromComponent = (Method) cachedMethods.computeIfAbsent("fromComponent", k -> {
            try {
                return StyledText.class.getMethod("fromComponent", Class.forName("net.minecraft.class_2561"));
            }
            catch(NoSuchMethodException | ClassNotFoundException e) {
                return null;
            }
        });
        if(fromComponent == null) return false;

        cacheDone = true;
        return true;
    }

    public static WynntilsEventMessage tossToWynntilsTextHandleEvent(Text text) {
        try {
            if(!cacheClassAndMethod()) return WynntilsEventMessage.NOT_HANDLED;
            Constructor<?> eventConstructor = (Constructor<?>) cachedMethods.get("createEvent");
            Method postEvent = (Method) cachedMethods.get("post");
            Method isCanceled = (Method) cachedMethods.get("isCanceled");
            Method isMessageChanged = (Method) cachedMethods.get("isMessageChanged");
            Method getMessage = (Method) cachedMethods.get("getMessage");

            Object wynntilsEvent = eventConstructor.newInstance(text);
            postEvent.invoke(null, wynntilsEvent);
            if((Boolean) isCanceled.invoke(wynntilsEvent)) return WynntilsEventMessage.CANCELED;
            if((Boolean) isMessageChanged.invoke(wynntilsEvent)) {
                WynnTransText.transportMessage((Text) getMessage.invoke(wynntilsEvent));
                return WynntilsEventMessage.MODIFIED;
            }
            return WynntilsEventMessage.CONFIRMED;
        }
        catch(Exception ignore) {
            return WynntilsEventMessage.NOT_HANDLED;
        }
    }

    public static MutableText coloringPerfectItem(Text text) {
        if(cacheClassAndMethod()) {
            try {
                Method rainbow = (Method) cachedMethods.get("makeRainbowStyle");

                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.perfect.get()) throw new Exception();
                Object result = rainbow.invoke(null, text.getString(), true);
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }

    public static MutableText coloringDefectiveItem(Text text) {
        if(cacheClassAndMethod()) {
            try {
                Method obfuscated = (Method) cachedMethods.get("makeObfuscated");

                ItemStatInfoFeature info = Managers.Feature.getFeatureInstance(ItemStatInfoFeature.class);
                if(!info.defective.get()) throw new Exception();
                Object result = obfuscated.invoke(null, text.getString(), info.obfuscationChanceStart.get(), info.obfuscationChanceEnd.get());
                return (MutableText) result;
            }
            catch(Exception ignore) {
            }
        }
        return text.copy();
    }

    public static MutableText getTextFromWynntilsText(Object text) {
        if(cacheClassAndMethod()) {
            try {
                if(text instanceof StyledText wynntilsText) {
                    Method getComponent = (Method) cachedMethods.get("getComponent");
                    return (MutableText) getComponent.invoke(wynntilsText);
                }
            }
            catch(Exception ignore) {
            }
        }
        return Text.empty();
    }

    public static StyledText getWynntilsTextFromText(Text text) {
        if(cacheClassAndMethod()) {
            try {
                Method fromComponent = (Method) cachedMethods.get("fromComponent");
                Text ordered = WynnTransText.colorCodedToStyled(text);
                return (StyledText) fromComponent.invoke(null, ordered.copy());
            }
            catch(Exception ignore) {
            }
        }
        return StyledText.EMPTY;
    }

    public enum WynntilsEventMessage {
        CONFIRMED, CANCELED, MODIFIED, NOT_HANDLED
    }
}
