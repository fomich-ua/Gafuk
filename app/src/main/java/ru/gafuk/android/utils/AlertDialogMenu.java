package ru.gafuk.android.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Александр on 24.10.2017.
 */

public class AlertDialogMenu<T, E> {

    private List<MenuItem> items = new ArrayList<>();

    public void addItem(CharSequence title, OnClickListener<T, E> listener) {
        items.add(new MenuItem(title, listener));
    }

    public void addItem(int index, CharSequence title, OnClickListener<T, E> listener) {
        if (index < 0) index = 0;
        if (index > items.size()) index = items.size() - 1;

        items.add(index, new MenuItem(title, listener));
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void addItems(Collection<MenuItem> items) {
        this.items.addAll(items);
    }

    public List<MenuItem> getItems(){
        return items;
    }

    public MenuItem get(int index) {
        return items.get(index);
    }

    public boolean contains(CharSequence title) {
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).title.equals(title))
                return true;
        return false;
    }

    public void remove(int i) {
        items.remove(i);
    }

    public void clear() {
        items.clear();
    }

    public int containsIndex(CharSequence title) {
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).title.equals(title))
                return i;
        return -1;
    }

    public void changeTitle(int i, CharSequence title) {
        items.get(i).setTitle(title);
    }

    public CharSequence[] getTitles() {
        CharSequence[] result = new CharSequence[items.size()];
        for (int i = 0; i < items.size(); i++)
            result[i] = items.get(i).title;
        return result;
    }

    public void onClick(int i, T context, E data){
        items.get(i).onClick(context, data);
    }

    public class MenuItem implements OnClickListener<T, E>{
        private OnClickListener<T, E> onClickListener;
        private CharSequence title;

        public MenuItem(CharSequence title, OnClickListener<T, E> onClickListener) {
            this.onClickListener = onClickListener;
            this.title = title;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        @Override
        public void onClick(T context, E data) {
            if (onClickListener!=null){
                onClickListener.onClick(context, data);
            }
        }
    }

    public interface OnClickListener<T, E>{
        void onClick(T context, E data);
    }
}
