package com.felix.unbiz.json.rpc.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.felix.unbiz.json.rpc.constant.JsonCommonConstant;

/**
 * ClassName: ClassHtmlUtil <br/>
 * Function: servlet打印html页面的工具类
 *
 * @author wangxujin
 */
public final class ClassHtmlUtils {

    public static final String CLASS_URL_PATH = "get_class_defination";

    private static final String OPEN_TD = "<td>";
    private static final String OPEN_TR = "<tr>";
    private static final String OPEN_GENERIC = "&lt;";
    private static final String CLOSE_TD = "</td>";
    private static final String CLOSE_TR = "</tr>";
    private static final String CLOSE_GENERIC = "&gt;";
    private static final String COMMA = ",";
    private static final String EMPTY_STR = "";
    private static final String ARRAY_BRACKET = "[]";

    private ClassHtmlUtils() {
    }

    @SuppressWarnings("rawtypes")
    public static String getServiceHtml(Class clazz) {
        StringBuilder result = new StringBuilder();

        result.append("<table border=\"1\">");
        result.append("<tr><th>Return Type</th><th>Method</th><th>Arguments</th></tr>");

        for (Method m : clazz.getMethods()) {
            result.append(OPEN_TR);
            result.append(OPEN_TD).append(getTypeInfo(m.getGenericReturnType(), m.getReturnType()))
                    .append(CLOSE_TD);
            result.append(OPEN_TD).append(m.getName()).append(CLOSE_TD);
            int paramSize = m.getParameterTypes().length;
            if (paramSize == 0) {
                result.append(OPEN_TD).append(EMPTY_STR).append(CLOSE_TD);
            } else {
                result.append(OPEN_TD);
                for (int i = 0; i < paramSize; i++) {
                    result.append(
                            getTypeInfo(m.getGenericParameterTypes()[i], m.getParameterTypes()[i]))
                            .append(COMMA);
                }
                result.deleteCharAt(result.length() - 1);
                result.append(CLOSE_TD);
            }
            result.append(CLOSE_TR);
        }

        result.append("</table>");
        return result.toString();
    }

    @SuppressWarnings("rawtypes")
    private static String getTypeInfo(Type type, Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        if (clazz.isPrimitive()) {
            result.append(clazz.getName());
            return result.toString();
        }

        if (clazz.isArray()) {
            result.append(getAppendName(clazz.getComponentType().getName())).append(ARRAY_BRACKET);
        } else {
            result.append(getAppendName(clazz.getName()));
        }

        if (type instanceof ParameterizedType) { // 处理泛型类型
            ParameterizedType parameterizedType = (ParameterizedType) type;
            result.append(OPEN_GENERIC);
            Type[] types = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < types.length; i++) {
                Class gc = getGenericClass(parameterizedType, i);
                result.append(getAppendName(gc.getName()));
                result.append(COMMA);
            }
            result.deleteCharAt(result.length() - 1);
            result.append(CLOSE_GENERIC);
        }
        return result.toString();
    }

    private static boolean isCustomizedClass(String rawName) {
        if (rawName == null) {
            return false;
        } else if (rawName.startsWith("java.") || rawName.startsWith("javax.")
                || rawName.startsWith("org.") || rawName.startsWith("com.sun")) {
            return false;
        }
        return true;
    }

    private static String getAppendName(String name) {
        if (isCustomizedClass(name)) {
            return "<a href=\"" + JsonCommonConstant.TRANSPORT_URL_BASE_PATH + CLASS_URL_PATH
                    + "?class=" + name + "\">" + name + "</a>";
        } else {
            return name;
        }
    }

    @SuppressWarnings("rawtypes")
    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else { // class本身也是type，强制转型
            return (Class) type;
        }
    }

    @SuppressWarnings("rawtypes")
    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else if (genericClass instanceof WildcardType) { // 处理泛型擦拭对象
            return new Object().getClass();
        } else {
            return (Class) genericClass;
        }
    }

}
