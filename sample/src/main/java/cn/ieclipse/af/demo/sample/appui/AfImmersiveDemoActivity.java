/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.appui;

/**
 * 类/接口描述
 *
 * @author Jamling
 */
public class AfImmersiveDemoActivity extends AfDemoActivity {
    
    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        setImmersiveMode(true);
    }
    
    @Override
    protected int getStatusBarColor() {
        return 0x66ff9966;
    }
}
