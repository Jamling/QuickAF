package cn.ieclipse.af.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;

public class AndroidLogFactory implements ILoggerFactory {
    private final ConcurrentMap<String, Logger> mLoggerMap = new ConcurrentHashMap<>();

    @Override
    public Logger getLogger(String name) {
        synchronized (mLoggerMap) {
            Logger logger = null;
            if (!mLoggerMap.containsKey(name)) {
                logger = new Logger(name);
                mLoggerMap.put(name, logger);
            } else {
                logger = mLoggerMap.get(name);
            }
            return logger;
        }
    }
}
