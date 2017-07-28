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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import cn.ieclipse.af.demo.sample.orm.bean.Course;
import cn.ieclipse.af.demo.sample.orm.bean.Grade;
import cn.ieclipse.af.demo.sample.orm.bean.Student;
import cn.ieclipse.aorm.Aorm;
import cn.ieclipse.aorm.Session;

/**
 * @author Jamling
 * 
 */
public class ExampleContentProvider extends ContentProvider {
    
    public static final String AUTH = "cn.ieclipse.af.demo.sample.aorm.example.provider";
    public static final Uri URI = Uri.parse("content://" + AUTH);
    private SQLiteOpenHelper mOpenHelper;
    private static Session session;
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#query(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[],
     * java.lang.String)
     */
    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
            String arg4) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static Session getSession() {
        return session;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new SQLiteOpenHelper(this.getContext(), "example.db",
                null, 1) {
            public void onCreate(SQLiteDatabase db) {
                String sql = "";
                // method 1: define DDL sql
//                sql = "create TABLE grade (  ";
//                sql += "_id Integer Primary key autoincrement, ";
//                sql += "_sid Integer, ";
//                sql += "_cid Integer, ";
//                sql += "_score Float)";
//                db.execSQL(sql);
//                sql = "create TABLE student (  ";
//                sql += "_id Integer Primary key autoincrement, ";
//                sql += "_name String, ";
//                sql += "_age Integer, ";
//                sql += "_phone String)";
//                db.execSQL(sql);
//                sql = "create TABLE course (  ";
//                sql += "_id Integer Primary key autoincrement, ";
//                sql += "_name String)";
//                db.execSQL(sql);
                
                // method 2: use AORM to generate DDL sql
                // sql = Aorm.generateCreateDDL(Grade.class);
                // db.execSQL(sql);
                
                // method 3: use AORM to create table
                Aorm.createTable(db, Grade.class);
                Aorm.createTable(db, Student.class);
                Aorm.createTable(db, Course.class);
            }
            
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                    int newVersion) {

                Aorm.updateTable(db, Grade.class);
                Aorm.updateTable(db, Student.class);
                Aorm.updateTable(db, Course.class);
            }
        };
        session = new Session(mOpenHelper, getContext().getContentResolver());
        return true;
    }
    
}
