package com.gjn.basemenubarlibrary;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjn on 2018/6/6.
 */

public abstract class MenuBar<T extends MenuBarItem> {
    private static final String TAG = "MenuBar";
    private Context context;
    private ViewPager viewPager;
    private List<T> items;

    private int row = 2;
    private int col = 4;
    private int size;
    private int page;
    private int maker;
    private int Width;
    private int Height;

    public MenuBar(Context context, ViewPager viewPager, List<T> items,
                   int row, int col) {
        this.context = context;
        this.viewPager = viewPager;
        this.items = items == null ? new ArrayList<T>() : items;
        this.row = row;
        this.col = col;
    }

    public MenuBar(Context context, ViewPager viewPager, List<T> items) {
        this.context = context;
        this.viewPager = viewPager;
        this.items = items == null ? new ArrayList<T>() : items;
    }

    private void init() {
        maker = 0;
        Width = 0;
        Height = 0;
        size = row * col;
        page = (int) Math.ceil(items.size() * 1.0 / size);
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int getRow() {
        return row;
    }

    public MenuBar setRow(int row) {
        if (row <= 0) {
            row = 1;
        }
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public MenuBar setCol(int col) {
        if (col <= 0) {
            col = 1;
        }
        this.col = col;
        return this;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public MenuBar setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public MenuBar setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void updataMenuBar() {
        updataMenuBar(viewPager);
    }

    public void updataMenuBar(List<T> items) {
        updataMenuBar(items, viewPager, row, col);
    }

    public void updataMenuBar(ViewPager viewPager) {
        updataMenuBar(items, viewPager, row, col);
    }

    public void updataMenuBar(int row, int col) {
        updataMenuBar(items, viewPager, row, col);
    }

    public void updataMenuBar(List<T> items, ViewPager viewPager, int row, int col) {
        this.items = items;
        this.viewPager = viewPager;
        this.row = row;
        this.col = col;
        create();
    }

    public void create() {
        if (items.size() == 0) {
            Log.e(TAG, "items is null.");
            return;
        }

        //初始化数据，并且计算页数
        init();
        List<View> views = new ArrayList<>();
        //绘制每一页
        for (int i = 0; i < page; i++) {
            List<T> barItems = new ArrayList<>();
            int len;
            //每页分配col*row个item，直到最后一页全部显示
            if (i == page - 1) {
                len = items.size();
            } else {
                len = size + maker;
            }
            for (int j = maker; j < len; j++) {
                barItems.add(items.get(j));
                maker++;
            }
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new GridLayoutManager(context, col));
            RecyclerView.Adapter adapter = createPageRecyclerViewAdapter(context, barItems);
            if (adapter == null) {
                throw new NullPointerException("PageRecyclerViewAdapter is null.");
            }
            recyclerView.setAdapter(adapter);
            views.add(recyclerView);
            getPageMaxWH(recyclerView);
        }
        viewPager.setAdapter(new MenuBarAdapter(views));
    }

    private void getPageMaxWH(RecyclerView recyclerView) {
        recyclerView.measure(0, 0);
        int w = recyclerView.getMeasuredWidth();
        int h = recyclerView.getMeasuredHeight();
        w = w / col;
        h = h / row;
        if (w > Width) {
            Width = w;
        }
        if (h > Height) {
            Height = h;
        }
    }

    protected abstract RecyclerView.Adapter createPageRecyclerViewAdapter(Context context, List<T> barItems);

    private class MenuBarAdapter extends PagerAdapter {
        private List<View> views;

        public MenuBarAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
