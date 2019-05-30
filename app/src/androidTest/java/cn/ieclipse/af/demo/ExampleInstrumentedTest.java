package cn.ieclipse.af.demo;


import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testPackageName() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Assert.assertEquals("cn.ieclipse.af.demo", appContext.getPackageName());
    }


}