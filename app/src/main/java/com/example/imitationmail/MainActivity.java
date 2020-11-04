package com.example.imitationmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends ListActivity {

    TextView debug;
    //fake data
    public static String [] names = {"Eduria.com","Chris Abad","Tuto.com","support","Matt from Ionic","Udemy Instructor",
    "GitHub","Google","Entopy","Dabia","Jesus","Gawr Gura"};
    public static String [] titles = {"$19 Only(First 10 spots) - Bestselling...","Help make Campaign Monitor better",
            "8h de formation gratuite et les nouvea...","Societe Ovh: suivi de vos services - hp...","New " +
            "Ionic Creator Is Here!","Early Bird Discount on new ML course: 48...",
            "Discover what’s new at GitHub","Security Alert!","Idk Man These titles are hard","Rlly Running outta ideas here",
             "My Bible!","a"};
    public static String [] times = {"1:00 am","0:00 pm","11:11 am","6:13 am","12:30 pm", "9:52 am", "6:10 am","4:50 pm","12:00 pm","7:00 pm","9:20 am","3:10 pm"};
    public static String [] contents = {"Are you looking to Learn Web Designin...","Let us know your thoughts! No Images...",
    "Photoshop, SEO, Blender, CSS, WordPre...","SAS OVH - http://www.ovh 2 rue K...","Announcing the all-new Creator, build...",
    "There is only 4...","We’re constantly learning, buil...","academia.edu has been granted access...","Smth Smth smth smth",
    "bla bla bla bla bleh","Im sory if this is offensive...","Im drunk but singer :3..."};

    List database = new DATABASE().dbList;

    public static Integer [] avatar = {R.drawable.e,R.drawable.c,R.drawable.t,R.drawable.s,R.drawable.m,R.drawable.u,
    R.drawable.g,R.drawable.g2,R.drawable.e,R.drawable.d,R.drawable.j,R.drawable.g};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debug = (TextView) findViewById(R.id.debug);


        CustomBaseAdapter adapter = new CustomBaseAdapter(this,database,
                R.layout.custom_row_icon_label);
//        CustomIconLabelAdapter adapter = new CustomIconLabelAdapter(this,
//                R.layout.custom_row_icon_label,
//                names, titles, contents, times,avatar);
// bind intrinsic ListView to custom adapter
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String text = " Position: " + position;
        debug.setText(text);
    }

}

class CustomBaseAdapter extends BaseAdapter {
    Context context;
    int layoutToBeInflated;
    List<DATABASE.DbRecord> dbList;

    public CustomBaseAdapter(Context context, List<DATABASE.DbRecord>
            databaseList, int resource) {
        this.context = context;
        this.dbList = databaseList;
        layoutToBeInflated = resource;
    }

    @Override
    public int getCount() {
        return dbList.size();
    }

    @Override
    public DATABASE.DbRecord getItem(int position) {
        return dbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        View row = convertView;
        if(row==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_row_icon_label, null);

            holder = new MyViewHolder();

            holder.name = (TextView) row.findViewById(R.id.name);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.content = (TextView) row.findViewById(R.id.content);
            holder.time = (TextView) row.findViewById(R.id.time);
            holder.avatar = (ImageView) row.findViewById(R.id.icon);
            holder.favorite = (ImageView) row.findViewById(R.id.favorite);

            row.setTag(holder);

        }else
            holder = (MyViewHolder) row.getTag();

        DATABASE.DbRecord dbRec = getItem(position);
        holder.title.setText(dbRec.title);
        holder.content.setText(dbRec.content);
        holder.name.setText(dbRec.name);
        holder.time.setText(dbRec.time);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbRec.previousView!=null){
                    DATABASE.DbRecord previousItem =getItem(dbRec.previousPosition);
                    previousItem.clicked1 = false;
                }
                dbRec.check=!dbRec.check;
                dbRec.clicked1 = true;
                dbRec.previousView = v;
                dbRec.previousPosition = position;
                notifyDataSetChanged();
            }
            });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbRec.previousView!=null){
                    DATABASE.DbRecord previousItem =getItem(dbRec.previousPosition);
                    previousItem.clicked2 = false;
                }
                dbRec.favor=!dbRec.favor;
                dbRec.clicked2 = true;
                dbRec.previousView = v;
                dbRec.previousPosition = position;
                notifyDataSetChanged();
            }
        });

        if(!dbRec.clicked1){
            holder.avatar.setImageResource(dbRec.avatar);
        }else{
            if(!dbRec.check)
                holder.avatar.setImageResource(R.drawable.check);
            else
                holder.avatar.setImageResource(dbRec.avatar);
        }
        if(!dbRec.clicked2)
            holder.favorite.setImageResource(dbRec.favorite);
        else{
            if(!dbRec.favor)
                holder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
            else
                holder.favorite.setImageResource(dbRec.favorite);
        }
        return row;
    }
}



