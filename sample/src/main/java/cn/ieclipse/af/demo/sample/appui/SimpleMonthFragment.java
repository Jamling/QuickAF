package cn.ieclipse.af.demo.sample.appui;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.view.SimpleMonthView;

/**
 *
 */
public class SimpleMonthFragment extends BaseFragment {

    private SimpleMonthView mMonthView;
    private TextView mDayAndMonthTxt;

    private Calendar mCurrentBookDay, mToday;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_datepicker;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mMonthView = (SimpleMonthView)view.findViewById(R.id.monthView);

        mMonthView.setOnDayClickListener(new SimpleMonthView.OnDayClickListener() {
            @Override
            public void onDayClick(SimpleMonthView view, Calendar day) {
                mCurrentBookDay = day;
            }
        });

        view.findViewById(R.id.btnPrevMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = mToday.get(Calendar.MONTH);
                int year = mToday.get(Calendar.YEAR);

                int monthDest = mCurrentBookDay.get(Calendar.MONTH);
                int yearDest = mCurrentBookDay.get(Calendar.YEAR);

                if (yearDest > year || (yearDest == year && monthDest > month)) {
                    mCurrentBookDay.add(Calendar.MONTH, -1);
                    updateMonthView();
                }
            }
        });

        view.findViewById(R.id.btnNextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentBookDay.add(Calendar.MONTH, 1);
                updateMonthView();
            }
        });

        mDayAndMonthTxt = (TextView) view.findViewById(R.id.dayAndMonthTxt);

    }

    @Override
    protected void initData() {
        super.initData();

        mToday = Calendar.getInstance();
        //mToday.add(Calendar.DAY_OF_MONTH, 1);
        mCurrentBookDay = Calendar.getInstance();
        mCurrentBookDay.add(Calendar.DAY_OF_MONTH, 1);

        updateMonthView();
    }



    private void updateMonthView() {

        int month = mToday.get(Calendar.MONTH);
        int year = mToday.get(Calendar.YEAR);
        //int hour = mToday.get(Calendar.HOUR_OF_DAY);

        int destDay = mCurrentBookDay.get(Calendar.DAY_OF_MONTH);
        int destMonth = mCurrentBookDay.get(Calendar.MONTH);
        int destYear = mCurrentBookDay.get(Calendar.YEAR);
        int daysInMonth = SimpleMonthView.getDaysInMonth(destMonth, destYear);

        int startDay = 1;
        if (month == destMonth && year == destYear) {
            startDay = mToday.get(Calendar.DAY_OF_MONTH);
            if (destDay < startDay) {
                destDay = startDay;
            }
            if (destDay > daysInMonth) {
                destDay = daysInMonth;
            }
        }

        mMonthView.setMonthParams(destDay, destMonth, destYear,
            mCurrentBookDay.getFirstDayOfWeek(), startDay, daysInMonth);
        mDayAndMonthTxt.setText(mMonthView.getMonthAndYearString());
    }


}
