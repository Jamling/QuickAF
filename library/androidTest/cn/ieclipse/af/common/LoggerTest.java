package cn.ieclipse.af.common;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(AndroidJUnit4.class)
public class LoggerTest {
    @Test
    public void test() {
        Logger logger = LoggerFactory.getLogger(getClass());


        logger.trace("simple msg");
        logger.trace("now is {}", 111);
        logger.trace("{} love {} and {}", "a", "b", "c");

    }
}
