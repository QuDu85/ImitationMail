package com.example.imitationmail;

import android.app.LauncherActivity;
import android.view.View;

import java.util.ArrayList;

public class DATABASE {
    public class DbRecord{
        public String name,title,content,time;
        public Integer avatar,favorite;
        public boolean clicked1,clicked2;
        public int previousPosition;
        public boolean check=true;
        public boolean favor=true;
        public View previousView;
        public DbRecord(String name, String title, String content, String time, Integer img1, Integer img2) {
            this.name = name;
            this.title = title;
            this.content = content;
            this.time = time;
            this.avatar = img1;
            this.favorite = img2;
            clicked1=false;
            clicked2=false;
        }

    }

    public static ArrayList <DbRecord> dbList = new ArrayList<DbRecord>();

    public DATABASE(){
        for(int i=0; i<MainActivity.names.length; i++){
            dbList.add(new DbRecord(MainActivity.names[i],MainActivity.titles[i],
                    MainActivity.contents[i],MainActivity.times[i],MainActivity.avatar[i],
                    android.R.drawable.btn_star_big_off));
        }
    }
}
