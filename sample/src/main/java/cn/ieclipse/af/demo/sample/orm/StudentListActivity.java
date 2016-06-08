/*
 * Copyright (C) 2015-2016 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.af.demo.sample.orm;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.sample.orm.bean.Student;
import cn.ieclipse.aorm.Criteria;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016/2/15.
 */
public class StudentListActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {
        
    private ListView mListView;
    private TextView mEmptyView;
    private StudentAdapter mAdapter;
    
    private View mAdd;
    
    @Override
    protected int getContentLayout() {
        return android.R.layout.list_content;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        ImageView iv = new ImageView(this);
        iv.setImageResource(android.R.drawable.ic_menu_add);
        mTitleBar.addRight(iv);
        mAdd = iv;
        setOnClickListener(mAdd);
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new StudentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mEmptyView = new TextView(this);
        mEmptyView.setText("No student found, click + to add");
        mListView.setEmptyView(mEmptyView);
    }
    
    @Override
    protected void initData() {
        super.initData();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Student s = mAdapter.getItem(position);
        startActivity(StudentDetailActivity.create(this, s));
    }
    
    @Override
    public void onClick(View v) {
        if (v == mAdd) {
            startActivity(StudentDetailActivity.create(this, null));
        }
        super.onClick(v);
    }
    
    @SuppressWarnings("unchecked")
    private void loadStudents() {
        List<Student> students = ExampleContentProvider.getSession()
                .list(Criteria.create(Student.class));
        mAdapter.setDataList(students);
        mAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }
    
    private class StudentAdapter extends AfBaseAdapter<Student> {
        
        @Override
        public int getLayout() {
            return R.layout.sample_list_item_student;
        }
        
        @Override
        public void onUpdateView(View view, int position) {
            Student s = getItem(position);
            TextView tvIndex = (TextView) view.findViewById(R.id.tv_index);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tvAge = (TextView) view.findViewById(R.id.tv_age);
            
            // TextView tvAddress = (TextView)
            // view.findViewById(R.id.tv_address);
            tvIndex.setText(String.valueOf(s.getId()));
            tvName.setText(String.valueOf(s.getName()));
            tvPhone.setText(String.valueOf(s.getPhone()));
            tvAge.setText(String.valueOf(s.getAge()));
        }
    }
    
    public static Intent create(Context context) {
        Intent intent = new Intent(context, StudentListActivity.class);
        return intent;
    }
}
