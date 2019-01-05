package org.xutils.http;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Created by wyouflf on 16/1/23.
 */
/*package*/ final class RequestParamsHelper {

    private static final ClassLoader BOOT_CL = String.class.getClassLoader();

    private RequestParamsHelper() {
    }

    /*package*/ interface ParseKVListener {
        void onParseKV(String name, Object value);
    }

    /*package*/
    static void parseKV(Object entity, Class<?> type, ParseKVListener listener) {
        if (entity == null || type == null || type == RequestParams.class || type == Object.class) {
            return;
        } else {
            ClassLoader cl = type.getClassLoader();
            if (cl == null || cl == BOOT_CL) {
                return;
            }
        }

        Field[] fields = type.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (!Modifier.isTransient(field.getModifiers())
                        && field.getType() != Parcelable.Creator.class) {
                    field.setAccessible(true);
                    try {
                        String name = field.getName();
                        Object value = field.get(entity);
                        if (value != null) {
                            listener.onParseKV(name, value);
                        }
                    } catch (IllegalAccessException ex) {
                        LogUtil.e(ex.getMessage(), ex);
                    }
                }
            }
        }

        parseKV(entity, type.getSuperclass(), listener);
    }

    /*package*/
    static Object parseJSONObject(Object value) throws JSONException {
        if (value == null) return null;

        Object result = value;
        Class<?> cls = value.getClass();
        if (cls.isArray()) {
            JSONArray array = new JSONArray();
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                array.put(parseJSONObject(Array.get(value, i)));
            }
            result = array;
        } else if (value instanceof List) {
            JSONArray array = new JSONArray();
            List<?> list = (List<?>) value;
            for (Object item : list) {
                array.put(parseJSONObject(item));
            }
            result = array;
        } else if (value instanceof Map) {
            final JSONObject jo = new JSONObject();
            Map<?, ?> map = (Map<?, ?>) value;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                if (k != null && v != null) {
                    jo.put(String.valueOf(k), parseJSONObject(v));
                }
            }
            result = jo;
        } else {
            ClassLoader cl = cls.getClassLoader();
            if (cl != null && cl != BOOT_CL) {
                final JSONObject jo = new JSONObject();
                parseKV(value, cls, new ParseKVListener() {
                    @Override
                    public void onParseKV(String name, Object value) {
                        try {
                            value = parseJSONObject(value);
                            jo.put(name, value);
                        } catch (JSONException ex) {
                            throw new IllegalArgumentException("parse RequestParams to json failed", ex);
                        }
                    }
                });
                result = jo;
            }
        }

        return result;
    }

}
