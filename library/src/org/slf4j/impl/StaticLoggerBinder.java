package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import cn.ieclipse.af.common.AndroidLogFactory;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    /**
     * The unique instance of this class.
     */
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    /**
     * Return the singleton of this class.
     *
     * @return the StaticLoggerBinder singleton
     */
    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return new AndroidLogFactory();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return AndroidLogFactory.class.getName();
    }
}
