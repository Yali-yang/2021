package com.xunce.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    private String filepath;

    public static Properties property = new Properties();

    public PropertyReader(String filepath) {
        this.filepath = filepath;
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);) {
            property.load(in);
        } catch (Exception e) {
            logger.error("读取配置文件失败！", e);
        }
    }

    public String get(String key) {
        return property.getProperty(key);
    }

    public Integer getInteger(String key) {
        try {
            String value = get(key);
            return null == value ? null : Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getLong(String key) {
        try {
            String value = get(key);
            return null == value ? null : Long.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        try {
            String value = get(key);
            return null == value ? null : Boolean.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Double getDouble(String key) {
        try {
            String value = get(key);
            return null == value ? null : Double.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public String get(String key, String defaultValue) {
        String value = property.getProperty(key);
        return null == value ? defaultValue : value;
    }

    public Integer getInteger(String key, Integer defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Integer.valueOf(value).intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Integer.valueOf(value).intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Long getLong(String key, Long defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Long.valueOf(value).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Long.valueOf(value).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Boolean.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Double getDouble(String key, Double defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Double.valueOf(value).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : Double.valueOf(value).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String[] getArray(String key, String splitKey) {
        try {
            String value = get(key);
            return null == value ? null : value.split(splitKey);
        } catch (Exception e) {
            return null;
        }
    }

    public String[] getArray(String key, String splitKey, String[] defaultValue) {
        try {
            String value = get(key);
            return null == value ? defaultValue : value.split(splitKey);
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
