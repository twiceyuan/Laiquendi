package xyz.rasp.laiquendi.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twiceYuan on 2017/3/22.
 * <p>
 * 参数解析器
 */
@SuppressWarnings("WeakerAccess")
public class Params {

    private Map<String, String> mParams;

    private Params(Map<String, String> params) {
        mParams = params;
    }

    public static Params parse(String paramsString) {
        Map<String, String> params = new HashMap<>();
        String[] paramPairs = paramsString.split(",");
        for (String pair : paramPairs) {
            try {
                String[] keyValue = pair.split(":");
                params.put(keyValue[0].trim(), keyValue[1].trim());
            } catch (Exception ignored) {
            }
        }
        return new Params(params);
    }

    public String getString(String key, String defaultValue) {
        String value = mParams.get(key);
        return value == null ? defaultValue : value;
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(mParams.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(mParams.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue) {
        try {
            return Float.parseFloat(mParams.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }
}
