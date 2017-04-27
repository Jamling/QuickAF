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
package cn.ieclipse.af.demo.sample.orm.bean;

import java.io.Serializable;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

/**
 * @author Jamling
 * 
 */
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 8010508999597447226L;

    @Column(name = "_id", id = true)
    private long id;

    @Column(name = "_name")
    private String name;

    @Column(name = "_age")
    private int age;

    @Column(name = "_phone")
    private String phone;

    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
