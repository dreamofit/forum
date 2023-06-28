package cn.ihoway.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.mybatis.caches.redis.RedisConfig;

final class RedisConfigurationBuilder {
    private static final RedisConfigurationBuilder INSTANCE = new RedisConfigurationBuilder();
    private static final String SYSTEM_PROPERTY_REDIS_PROPERTIES_FILENAME = "redis.properties.filename";
    private static final String REDIS_RESOURCE = "redis.properties";
    private final String redisPropertiesFilename = System.getProperty("redis.properties.filename", "redis.properties");

    private RedisConfigurationBuilder() {
    }

    public static RedisConfigurationBuilder getInstance() {
        return INSTANCE;
    }

    public RedisConfig parseConfiguration() {
        return this.parseConfiguration(this.getClass().getClassLoader());
    }

    public RedisConfig parseConfiguration(ClassLoader classLoader) {
        Properties config = new Properties();
        InputStream input = classLoader.getResourceAsStream(this.redisPropertiesFilename);
        if (input != null) {
            try {
                config.load(input);
            } catch (IOException var12) {
                throw new RuntimeException("An error occurred while reading classpath property '" + this.redisPropertiesFilename + "', see nested exceptions", var12);
            } finally {
                try {
                    input.close();
                } catch (IOException var11) {
                }

            }
        }

        RedisConfig jedisConfig = new RedisConfig();
        this.setConfigProperties(config, jedisConfig);
        return jedisConfig;
    }

    private void setConfigProperties(Properties properties, RedisConfig jedisConfig) {
        if (properties != null) {
            MetaObject metaCache = SystemMetaObject.forObject(jedisConfig);
            Iterator i$ = properties.entrySet().iterator();

            while(true) {
                while(true) {
                    String name;
                    String value;
                    do {
                        if (!i$.hasNext()) {
                            return;
                        }

                        Entry<Object, Object> entry = (Entry)i$.next();
                        name = (String)entry.getKey();
                        value = (String)entry.getValue();
                    } while(!metaCache.hasSetter(name));

                    Class<?> type = metaCache.getSetterType(name);
                    if (String.class == type) {
                        metaCache.setValue(name, value);
                    } else if (Integer.TYPE != type && Integer.class != type) {
                        if (Long.TYPE != type && Long.class != type) {
                            if (Short.TYPE != type && Short.class != type) {
                                if (Byte.TYPE != type && Byte.class != type) {
                                    if (Float.TYPE != type && Float.class != type) {
                                        if (Boolean.TYPE != type && Boolean.class != type) {
                                            if (Double.TYPE != type && Double.class != type) {
                                                throw new CacheException("Unsupported property type: '" + name + "' of type " + type);
                                            }

                                            metaCache.setValue(name, Double.valueOf(value));
                                        } else {
                                            metaCache.setValue(name, Boolean.valueOf(value));
                                        }
                                    } else {
                                        metaCache.setValue(name, Float.valueOf(value));
                                    }
                                } else {
                                    metaCache.setValue(name, Byte.valueOf(value));
                                }
                            } else {
                                metaCache.setValue(name, Short.valueOf(value));
                            }
                        } else {
                            metaCache.setValue(name, Long.valueOf(value));
                        }
                    } else {
                        metaCache.setValue(name, Integer.valueOf(value));
                    }
                }
            }
        }
    }
}
