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

/**
 * 
 */
package cn.ieclipse.af.demo.sample.orm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.orm.bean.Grade;
import cn.ieclipse.af.demo.sample.orm.bean.Student;
import cn.ieclipse.aorm.Criteria;
import cn.ieclipse.aorm.Restrictions;

/**
 * @author Jamling
 *         
 */
public class StudentDetailActivity extends DetailActivity {
    private Student student;
    
    private EditText etName;
    private EditText etPhone;
    private EditText etAge;
    private EditText etAddress;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_student_detail;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        etIndex = (EditText) findViewById(R.id.et_no);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAge = (EditText) findViewById(R.id.et_age);
        etAddress = (EditText) findViewById(R.id.et_address);
        
        session = ExampleContentProvider.getSession();
        student = session.get(Student.class, id);
        if (student != null) {
            etAddress.setText(student.getAddress());
            etName.setText(student.getName());
            etIndex.setText(String.valueOf(student.getId()));
            etAge.setText(String.valueOf(student.getAge()));
            etPhone.setText(String.valueOf(student.getPhone()));
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#delete()
     */
    @Override
    protected boolean delete() {
        int count = session.deleteById(Student.class, id);
        Criteria criteria = Criteria.create(Grade.class)
                .add(Restrictions.eq("sid", id));
        int num = session.delete(criteria);
        String text = getString(R.string.sample_delete_grade_info, String.valueOf(num),
                student.getName());
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return count > 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see cn.ieclipse.aorm.example.DetailActivity#edit()
     */
    @Override
    protected boolean edit() {
        boolean ret = false;
        if (student != null) {
            student.setAddress(etAddress.getText().toString());
            int age = 0;
            try {
                age = Integer.parseInt(etAge.getText().toString());
            } catch (NumberFormatException e) {
                age = 0;
            }
            student.setAge(age);
            student.setPhone(etPhone.getText().toString());
            student.setName(etName.getText().toString());
            int count = session.update(student);
            if (count > 0) {
                ret = true;
            }
        }
        return ret;
    }
    
    @Override
    protected boolean save() {
        boolean ret = false;
        Student student = new Student();
        student.setAddress(etAddress.getText().toString());
        int age = 0;
        try {
            age = Integer.parseInt(etAge.getText().toString());
        } catch (NumberFormatException e) {
            age = 0;
        }
        student.setAge(age);
        student.setPhone(etPhone.getText().toString());
        student.setName(etName.getText().toString());
        id = session.insert(student);
        if (id > 0) {
            ret = true;
            this.student = student;
        }
        return ret;
    }

    public static Intent create(Context context, Student s) {
        Intent intent = new Intent(context, StudentDetailActivity.class);

        intent.putExtra(EXTRA_ID, s == null ? 0L : s.getId());
        intent.putExtra(EXTRA_DATA, s);
        return intent;
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        student = (Student) bundle.getSerializable(EXTRA_DATA);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_DATA, student);
        super.onSaveInstanceState(outState);
    }
}
