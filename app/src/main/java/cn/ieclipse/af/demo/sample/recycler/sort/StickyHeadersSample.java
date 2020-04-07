package cn.ieclipse.af.demo.sample.recycler.sort;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeader;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.adapter.delegate.AdapterDelegate;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.SideBar;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshRecyclerHelper;
import cn.ieclipse.util.PinYinUtils;

/**
 * 使用这个库 https://github.com/bgogetap/StickyHeaders/ 相对来说侵入少一些,
 * 要求RecyclerView的父View必须为FrameLayout或CooditorLayout
 */
public class StickyHeadersSample extends SampleBaseFragment {

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_recycler_sort;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private SideBar mSideBar;
    private TextView mUserDialog;
    private RefreshLayout mRefreshLayout;
    private RefreshRecyclerHelper<ContactModel.HouseItemInfo> mRefreshHelper;

    ContactModel contactModel;
    MyAdapter adapter;

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mSideBar = view.findViewById(R.id.contact_sidebar);
        mUserDialog = view.findViewById(R.id.contact_dialog);
        mSideBar.setTextView(mUserDialog);
        mSideBar.setFocusBarBackground(R.drawable.sidebar_background);

        mRefreshLayout = view.findViewById(R.id.refresh);
        mRefreshLayout.setMode(RefreshLayout.REFRESH_MODE_NONE);
        mRefreshHelper =
                new RefreshRecyclerHelper(mRefreshLayout) {
                    @Override
                    public RecyclerView getRecyclerView() {
                        return refreshLayout.getContentView().findViewById(R.id.rv);
                    }
                };

        adapter = new MyAdapter();

        // step 1, set StickyLayoutManager
        mRefreshHelper
                .getRecyclerView()
                .setLayoutManager(new StickyLayoutManager(getActivity(), adapter));
        // set Adapter
        mRefreshHelper.setAdapter(adapter);

        Gson gson = new GsonBuilder().create();
        contactModel = gson.fromJson(DataList.tempData, ContactModel.class);
        onLoadList(contactModel.lipro);

        mSideBar.setOnTouchingLetterChangedListener(
                new SideBar.OnTouchingLetterChangedListener() {

                    @Override
                    public void onTouchingLetterChanged(String s) {
                        int position = getPositionForSection(s.charAt(0));
                        if (position != -1) {
                            // mRefreshHelper.getRecyclerView().scrollToPosition(position);
                            mRefreshHelper.scrollToPosition(position);
                        }
                    }
                });
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            String sortStr = adapter.getDataList().get(i).getSpelling();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public void onLoadList(List<ContactModel.HouseItemInfo> houseList) {
        if (houseList != null && !houseList.isEmpty()) {
            for (ContactModel.HouseItemInfo at : houseList) {
                if (at.title.length() > 0) {
                    // 解析出对应的拼音
                    String a = PinYinUtils.getPinYin(at.title).toUpperCase(Locale.US);
                    at.setSpelling(a);
                    // 获得第一个字符
                    if (a.length() > 1) {
                        a = a.substring(0, 1);
                    }
                    // 匹配A-Z就设置进去
                    if (a.matches("[A-Z]")) {
                        at.setInitial(a);
                    } else {
                        at.setInitial("#");
                    }
                }
            }
            Collections.sort(houseList);

            // Step 2 列表中把头部数据插入到数据列表中
            List<ContactModel.HouseItemInfo> targetList = new ArrayList<>(houseList.size());
            List<String> headers = new ArrayList<>();
            for (ContactModel.HouseItemInfo item : houseList) {
                if (headers.contains(item.getInitial())) {
                    targetList.add(item);
                } else {
                    targetList.add(new HeaderInfo(item));
                    targetList.add(item);
                    headers.add(item.getInitial());
                }
            }

            mRefreshHelper.onLoadFinish(targetList);
        }
    }

    public class MyAdapter extends AfRecyclerAdapter<ContactModel.HouseItemInfo>
            implements StickyHeaderHandler {

        @Override
        public List<?> getAdapterData() {
            return getDataList();
        }

        MyAdapter() {
            registerDelegate(0, new HDelegate());
            registerDelegate(1, new NDelegate());
            setOnItemClickListener(
                    new OnItemClickListener() {
                        @Override
                        public void onItemClick(
                                AfRecyclerAdapter adapter, View view, int position) {
                            ContactModel.HouseItemInfo info =
                                    (ContactModel.HouseItemInfo) adapter.getItem(position);
                            if (info != null) {
                                if (info instanceof HeaderInfo) {
                                    DialogUtils.showToast(
                                            getContext(),
                                            String.format("%s : %s", position, info.getInitial()));
                                } else {
                                    DialogUtils.showToast(
                                            getContext(),
                                            String.format("%s : %s", position, info.title));
                                }
                            }
                        }
                    });
        }
    }

    public static class HDelegate extends MyDelegate {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
            RecyclerView.ViewHolder vh = super.onCreateViewHolder(parent);
            vh.itemView.setBackgroundColor(0xffcccccc);
            return vh;
        }

        @Override
        public void onUpdateView(
                RecyclerView.ViewHolder holder, ContactModel.HouseItemInfo info, int position) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tv1.setText(info.getInitial());
        }

        @Override
        public boolean isForViewType(ContactModel.HouseItemInfo info, int position) {
            return info instanceof StickyHeader;
        }
    }

    public static class NDelegate extends MyDelegate {}

    public static class MyDelegate extends AdapterDelegate<ContactModel.HouseItemInfo> {

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(
                RecyclerView.ViewHolder holder, ContactModel.HouseItemInfo info, int position) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tv1.setText(info.title);
        }

        @Override
        public Class<? extends RecyclerView.ViewHolder> getViewHolderClass() {
            return MyViewHolder.class;
        }
    }

    public static class MyViewHolder extends AfViewHolder {

        public TextView tv1;

        public MyViewHolder(View itemView) {
            super(itemView);
            // set item selector
            // itemView.setBackgroundResource(android.R.drawable.list_selector_background);
            tv1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public static class HeaderInfo extends ContactModel.HouseItemInfo implements StickyHeader {
        public HeaderInfo(ContactModel.HouseItemInfo src) {
            this.title = src.title;
            this.id = src.id;
            this.setInitial(src.getInitial());
            this.setSpelling(src.getSpelling());
        }
    }
}
