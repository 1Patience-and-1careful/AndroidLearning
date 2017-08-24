package com.hanlyjiang.learnandroid.data;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author hanlyjiang on 2017/5/26-下午11:05.
 * @version 1.0
 */

public class MockDatasource {

    private List<Admin> admins = new ArrayList<>();

    private String[] ADMINS = new String[]{
       "天河区",
       "天河区1",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
       "天河区",
    };

    public MockDatasource() {
        Admin admin = null;
        for(int i = 0; i < 10; i++){
            admin = new Admin(ADMINS[i],i);
            admins.add(admin);
        }
    }

    public Admin queryAdmin(Location gps){
        int which = new Random().nextInt(10);
        return admins.get(which);
    }


}
