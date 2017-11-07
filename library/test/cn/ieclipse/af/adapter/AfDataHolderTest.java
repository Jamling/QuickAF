package cn.ieclipse.af.adapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Description
 *
 * @author Jamling
 */
public class AfDataHolderTest {
    AfDataHolder<Integer> holder;
    @Before
    public void setUp() throws Exception {
        holder = new AfDataHolder<>();
    }

    @Test
    public void setDataCheck() throws Exception {
        Assert.assertEquals(holder.getDataCheck(), AfDataHolder.CHECK_NONE);
        holder.setDataCheck(AfDataHolder.CHECK_BOTH);
        Assert.assertEquals(holder.getDataCheck(), AfDataHolder.CHECK_BOTH);
    }

    @Test
    public void clear() throws Exception {
        holder.add(1);
        holder.clear();
        Assert.assertEquals(true, holder.getDataList().isEmpty());
    }

    @Test
    public void addAll() throws Exception {

    }

    @Test
    public void addAll2Top() throws Exception {

    }

    @Test
    public void add2Top() throws Exception {

    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void getIndexOf() throws Exception {
        Assert.assertEquals(-1, holder.getIndexOf(1));
        holder.setDataList(null);
        Assert.assertEquals(-1, holder.getIndexOf(1));
    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void getCount() throws Exception {

    }

    @Test
    public void getItem() throws Exception {

    }

    @Test
    public void getDataList() throws Exception {

    }

    @Test
    public void setDataList() throws Exception {

    }
}