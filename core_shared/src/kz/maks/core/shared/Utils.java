package kz.maks.core.shared;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import kz.maks.core.shared.models.HasId;
import kz.maks.core.shared.models.HasName;
import kz.maks.core.shared.models.HasTitle;
import kz.maks.core.shared.models.ITreeNode;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Maksat on 05.01.2016.
 */
public class Utils {
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final int ONE_HOUR = 1000 * 60 * 60;
    public static final int ONE_DAY = ONE_HOUR * 24;
    public static final int ONE_WEEK = ONE_DAY * 7;
    public static final int ONE_MONTH = ONE_DAY * 30;

    public static boolean isDecimalType(Class<?> clazz) {
        return Double.class.isAssignableFrom(clazz) || BigDecimal.class.isAssignableFrom(clazz);
    }

    public static HasTitle findByTitle(HasTitle[] hasTitles, String title) {
        for (HasTitle hasTitle : hasTitles) {
            if (hasTitle.getTitle().equals(title))
                return hasTitle;
        }
        throw new IllegalArgumentException("getTitle = " + title);
    }

    public static String getterName(String fieldName) {
        String setterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return setterName;
    }

    public static String setterName(String fieldName) {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return setterName;
    }

    public static Method getMethod(Class clazz, final String methodName) {
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, new Predicate<Method>() {
            @Override
            public boolean apply(Method input) {
                return input.getName().equals(methodName);
            }
        });
        return (Method) methods.toArray()[0];
    }

    public static Field getField(Class clazz, final String fieldName) {
        Set<Field> fields = ReflectionUtils.getAllFields(clazz, new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                return input.getName().equals(fieldName);
            }
        });
        return (Field) fields.toArray()[0];
    }

    public static String getTreePath(ITreeNode treeNode) {
        List<Long> ids = new ArrayList<>();
        addTreePath(treeNode, ids);
        return Joiner.on(", ").join(ids);
    }

    private static void addTreePath(ITreeNode treeNode, List<Long> ids) {
        ids.add(treeNode.getId());

        if (treeNode.getParent() != null) {
            addTreePath(treeNode.getParent(), ids);
        }
    }

    public static Object invokeMethod(Object obj, Class<?> clazz, String methodName, Object... args) {
        try {
            Method method = getMethod(clazz, methodName);
            return method.invoke(obj, args);

        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long extractId(HasId hasId) {
        return hasId != null ? hasId.getId() : null;
    }

    public static String extractTitle(HasTitle hasTitle) {
        return hasTitle != null ? hasTitle.getTitle() : null;
    }

    public static <T extends HasTitle> List<String> extractTitles(List<T> hasTitles) {
        List<String> titles = new ArrayList<>();

        for (T hasTitle : hasTitles) {
            titles.add(hasTitle.getTitle());
        }

        return titles;
    }

    public static String extractEnumName(Enum enumeration) {
        return enumeration != null ? enumeration.name() : null;
    }

    public static String extractName(HasName hasName) {
        return hasName != null ? hasName.name() : null;
    }

    public static <T extends HasName> List<String> extractNames(List<T> hasNames) {
        List<String> names = new ArrayList<>();

        for (T hasName : hasNames) {
            names.add(hasName.name());
        }

        return names;
    }

    public static <T extends HasId> List<Long> extractIds(List<T> hasIds) {
        List<Long> ids = new ArrayList<>();

        for (T hasId : hasIds) {
            ids.add(hasId.getId());
        }

        return ids;
    }

    public static <T extends Exception> void execute(int attemptsCount, Class<T> exceptionClass, Callable callable) {
        while (attemptsCount-- > 0) {
            try {
                callable.call();
                return;

            } catch (Exception e) {
                if (exceptionClass.isAssignableFrom(e.getClass())) {
                    e.printStackTrace();
                    continue;
                }
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Execution attempts elapsed");
    }

    public static boolean hasField(Class clazz, final String fieldName) {
        return !ReflectionUtils.getAllFields(clazz, new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                return input.getName().equals(fieldName);
            }
        }).isEmpty();
    }

    public static long getDateDifference(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date1.getTime() - date2.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static String getMacAddress() throws UnknownHostException, SocketException {
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        byte[] mac = networkInterface.getHardwareAddress();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        return sb.toString();
    }

    public static boolean isNullOrZero(Integer number) {
        return number == null || number == 0;
    }

    public static boolean isNullOrZero(Double number) {
        return number == null || number == 0;
    }

    public static Date beginningOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
