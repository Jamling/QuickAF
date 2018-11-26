package cn.ieclipse.af.view.recycle;

import org.junit.Test;

import java.util.Arrays;

/**
 * Description
 *
 * @author Jamling
 */
public class GridDividerItemDecorationTest {

    private int[] getItemOffset(int spanCount, int dw, int itemPosition) {
        int left;
        int right;
        int bottom;
        int eachWidth = (spanCount - 1) * dw / spanCount;// avg divider
        int dl = dw - eachWidth;// divider offset

        left = itemPosition % spanCount * dl;
        right = eachWidth - left;
        return new int[]{left, right};
    }

    @Test
    public void getItemOffsets() {
        int spanCount = 5;
        int dw = 100;
        int itemPosition = 0;

        for (int i = 0; i < spanCount; i++) {
            int[] p = getItemOffset(spanCount, dw, i);
            System.out.println(Arrays.toString(p));
        }
    }
}
