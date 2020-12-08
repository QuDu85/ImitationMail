package com.example.imitationmail;

import androidx.annotation.MainThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    List database = new DATABASE().dbList;
    List favorite = new ArrayList<DATABASE.DbRecord>();
    List search = new ArrayList<DATABASE.DbRecord>();
    CustomBaseAdapter adapter;
    CustomBaseAdapter Mainadapter = new CustomBaseAdapter(this,database,
                R.layout.custom_row_icon_label);
    String searchTarget;
    boolean favoriteOnly=false;

    public Context getContext(){
        return this;
    }

    public void setCurrentAdapter(CustomBaseAdapter adapter){
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setLongClickable(true);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inbox");
        actionBar.setDisplayShowCustomEnabled(true);

        setCurrentAdapter(Mainadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_bar,menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("TAG", "Search with keyword: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search.clear();
                searchTarget=newText.toUpperCase();
                for(int i=0; i< database.size(); i++){
                    if(((DATABASE.DbRecord) database.get(i)).name.toUpperCase().contains(searchTarget)
                            || ((DATABASE.DbRecord) database.get(i)).title.toUpperCase().contains(searchTarget))
                        search.add(database.get(i));
                }
                CustomBaseAdapter search_adapter = new CustomBaseAdapter(getContext(),search,
                        R.layout.custom_row_icon_label);
                setCurrentAdapter(search_adapter);
                Log.v("TAG", "Keyword: " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.show_favorite){
            favorite.clear();
            for(int i=0; i< database.size(); i++){
                if(!((DATABASE.DbRecord) database.get(i)).favor)
                    favorite.add((DATABASE.DbRecord) database.get(i));
            }
            CustomBaseAdapter favorite_adapter = new CustomBaseAdapter(this,favorite,
                    R.layout.custom_row_icon_label);
            favoriteOnly=!favoriteOnly;
            if(favoriteOnly){
                setCurrentAdapter(favorite_adapter);
                item.setTitle("Show all");
            }
            else{
                setCurrentAdapter(Mainadapter);
                item.setTitle("Show favorite");
            }
            return true;
        }
        return false;
    }
}

class CustomBaseAdapter extends BaseAdapter {
    Context context;
    int layoutToBeInflated;
    List<DATABASE.DbRecord> dbList;

    public void popUpContext(TextView t, DATABASE.DbRecord currentData, CustomBaseAdapter adapter){
        t.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.reply) {
                            Log.v("TAG", "Reply action");
                            String[] emailReceiverList = {currentData.name};
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("vnd.android.cursor.dir/email");
                            intent.putExtra(Intent.EXTRA_EMAIL, emailReceiverList);
                            context.startActivity(Intent.createChooser(intent,
                                    "To complete action choose:"));

                        } else if (id == R.id.delete) {
                            Log.v("TAG", "Delete action");
                            adapter.dbList.remove(currentData);
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    };

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
            LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
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

        popUpContext(holder.title, dbRec, this);
        popUpContext(holder.name, dbRec,this);
        popUpContext(holder.content, dbRec,this);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbRec.previousView!=null && dbRec.previousPosition!=-1){
                    DATABASE.DbRecord previousItem =getItem(dbRec.previousPosition);
                    Log.v("Pre pos: ", ""+dbRec.previousPosition);
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
                if(dbRec.previousView!=null && dbRec.previousPosition!=-1){
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



