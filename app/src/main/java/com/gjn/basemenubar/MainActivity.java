package com.gjn.basemenubar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.gjn.basemenubarlibrary.MenuBar;
import com.gjn.basemenubarlibrary.MenuBarItem;
import com.gjn.library.BaseRecyclerAdapter;
import com.gjn.library.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);

        List<MenuBarItem> list = new ArrayList<>();

        MenuBarItem item;

        for (int i = 0; i < 12; i++) {
            item = new MenuBarItem();
            item.setName("标签" + i);
            if (i % 2 == 0) {
                item.setImg(R.mipmap.ic_launcher);
            } else {
                item.setImg(R.mipmap.ic_launcher_round);
            }
            list.add(item);
        }

        MenuBar<MenuBarItem> bar = new MenuBar<MenuBarItem>(this, viewPager, list) {
            @Override
            protected RecyclerView.Adapter createPageRecyclerViewAdapter(Context context, List<MenuBarItem> barItems) {
                return new BaseRecyclerAdapter<MenuBarItem>(context, R.layout.item, barItems) {
                    @Override
                    public void bindData(RecyclerViewHolder recyclerViewHolder, MenuBarItem menuBarItem, int i) {
                        recyclerViewHolder.setTextViewText(R.id.tv, menuBarItem.getName());
                        recyclerViewHolder.getImageView(R.id.img).setImageDrawable(getResources().getDrawable((Integer) menuBarItem.getImg()));
                    }
                };
            }
        };

        bar.create();
    }
}
