package cn.ieclipse.af.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description
 *
 * @author Jamling
 */
public class PinYinUtilsTest {
    @Test
    public void convert() throws Exception {
        String actual = PinYinUtils.convert("中");
        Assert.assertEquals("zhong", actual);

        actual = PinYinUtils.convert("中国");
        Assert.assertEquals("zhong", actual);
    }

    @Test
    public void getSelling() throws Exception {
        String actual = PinYinUtils.getSelling("中国");
        Assert.assertEquals("zhongguo", actual);

        actual = PinYinUtils.getSelling("I love中国abc");
        Assert.assertEquals("I lovezhongguoabc", actual);

        actual = PinYinUtils.getSelling("啊 1601 阿 1602 吖 6325 嗄 6436 腌 7571 锕 7925");
        Assert.assertEquals("a 1601 a 1602 吖 6325 嗄 6436 腌 7571 锕 7925", actual);

        actual = PinYinUtils.getSelling("芈");
        Assert.assertEquals("mi", actual);
    }

    @Test
    public void testGetPinYin() {
        String actual = PinYinUtils.getPinYin("中");
        Assert.assertEquals("zhong", actual);

        actual = PinYinUtils.getPinYin("中国");
        Assert.assertEquals("zhongguo", actual);

        actual = PinYinUtils.getPinYin("I love中国abc座");
        Assert.assertEquals("I lovezhongguoabczuo", actual);

        actual = PinYinUtils.getPinYin("啊 1601 阿 1602 吖 6325 嗄 6436 腌 7571 锕 7925");
        Assert.assertEquals("a 1601 a 1602 吖 6325 嗄 6436 腌 7571 锕 7925", actual);

        actual = PinYinUtils.getPinYin("芈");
        Assert.assertEquals("芈", actual);
    }

    @Test
    public void test() {
        PinYinUtils.test();
    }
}