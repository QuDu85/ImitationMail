package com.example.imitationmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    //fake data
    String [] names = {"Eduria.com","Chris Abad","Tuto.com","support","Matt from Ionic","Udemy Instructor",
    "GitHub","Google","Entopy","Dabia","Jesus","Gawr Gura"};
    String [] titles = {"$19 Only(First 10 spots) - Bestselling...","Help make Campaign Monitor better",
            "8h de formation gratuite et les nouvea...","Societe Ovh: suivi de vos services - hp...","New " +
            "Ionic Creator Is Here!","Early Bird Discount on new ML course: 48...",
            "Discover what’s new at GitHub","Security Alert!","Idk Man These titles are hard","Rlly Running outta ideas here",
             "My Bible!","a"};
    String [] times = {"1:00 am","0:00 pm","11:11 am","6:13 am","12:30 pm", "9:52 am", "6:10 am","4:50 pm","12:00 pm","7:00 pm","9:20 am","3:10 pm"};
    String [] contents = {"Are you looking to Learn Web Designin...","Let us know your thoughts! No Images...",
    "Photoshop, SEO, Blender, CSS, WordPre...","SAS OVH - http://www.ovh 2 rue K...","Announcing the all-new Creator, build...",
    "There is only 4...","We’re constantly learning, buil...","academia.edu has been granted access...","Smth Smth smth smth",
    "bla bla bla bla bleh","Im sory if this is offensive...","Im drunk but singer :3..."};

    Integer [] avatar = {R.drawable.e,R.drawable.c,R.drawable.t,R.drawable.s,R.drawable.m,R.drawable.u,
    R.drawable.g,R.drawable.g2,R.drawable.e,R.drawable.d,R.drawable.j,R.drawable.g};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomIconLabelAdapter adapter = new CustomIconLabelAdapter(this,
                R.layout.custom_row_icon_label,
                names, titles, contents, times,avatar);
// bind intrinsic ListView to custom adapter
        setListAdapter(adapter);
    }
}

class CustomIconLabelAdapter extends ArrayAdapter<String> {
    Context context;
    Integer[] avatar;
    String[] names;
    String[] titles;
    String[] contents;
    String [] times;
    public CustomIconLabelAdapter( Context context, int layoutToBeInflated,
                                   String[] names, String[] titles, String[] contents, String [] times,
                                   Integer[] avatar) {
        super(context, R.layout.custom_row_icon_label, names);
        this.context = context;
        this.avatar = avatar;
        this.names = names;
        this.titles = titles;
        this.contents=contents;
        this.times = times;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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


        holder.title.setText(titles[position]);
        holder.content.setText(contents[position]);
        holder.name.setText(names[position]);
        if(holder.isCheck==0)
            holder.avatar.setImageResource(avatar[position]);
        holder.time.setText(times[position]);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.isCheck==0){
                    holder.avatar.setImageResource(R.drawable.check);
                    holder.isCheck=1;
                }else{
                    holder.avatar.setImageResource(avatar[position]);
                    holder.isCheck=0;
                }
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.isFavorite==0){
                    holder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
                    holder.isFavorite=1;
                }else{
                    holder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
                    holder.isFavorite=0;
                }

            }
        });

        return (row);
    }
}

