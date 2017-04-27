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
@Table(name = "grade")
public class Grade implements Serializable {
    
    private static final long serialVersionUID = -8976200526604051847L;
    
    private static final int PASS_GRADE = 60;
    
    @Column(name = "_id", id = true)
    private long id;
    
    @Column(name = "_sid")
    private long sid;
    
    @Column(name = "_cid")
    private long cid;
    
    @Column(name = "_score")
    private float score;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getSid() {
        return sid;
    }
    
    public void setSid(long sid) {
        this.sid = sid;
    }
    
    public long getCid() {
        return cid;
    }
    
    public void setCid(long cid) {
        this.cid = cid;
    }
    
    public float getScore() {
        return score;
    }
    
    public void setScore(float score) {
        this.score = score;
    }
    
    public boolean isPass() {
        return this.score >= PASS_GRADE;
    }
}
