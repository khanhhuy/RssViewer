package assignment.rssviewer.utils;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewHelper
{
    @SuppressWarnings("unchecked")
    public static <T> List<T> getSelectedItems(Class<T> itemClass, ListView listView)
    {
        List<T> selectedItems = new ArrayList<>();
        if (listView != null)
        {
            SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
            for (int i = 0; i < listView.getCount(); i++)
            {
                if (checkedPositions.valueAt(i))
                {
                    int pos = checkedPositions.keyAt(i);
                    T item = (T) listView.getAdapter().getItem(pos);
                    selectedItems.add(item);
                }
                else break;
            }
        }

        return selectedItems;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFirstSelectedItem(Class<T> itemClass, ListView listView)
    {
        if (listView != null)
        {
            SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
            int pos = -1;
            for (int i = 0; i < listView.getCount(); i++)
                if (checkedPositions.get(i))
                {
                    pos = i;
                    break;
                }

            if (pos >= 0)
            {
                return (T) listView.getAdapter().getItem(pos);
            }
        }
        return null;
    }

    public static enum Status
    {
        LOADING, EMPTY, NORMAL
    }

    public static class SupportWidget
    {
        private View parent, busyIndicator, emptyText;
        private ListView listView;

        public SupportWidget(ListView listView, View parent, View busyIndicator, View emptyText)
        {
            this.parent = parent;
            this.busyIndicator = busyIndicator;
            this.emptyText = emptyText;
            this.listView = listView;
        }

        public void toggleStatus(Status status)
        {
            switch (status)
            {
                case LOADING:
                    setVisibility(parent, View.VISIBLE);
                    setVisibility(busyIndicator, View.VISIBLE);
                    setVisibility(emptyText, View.INVISIBLE);
                    setVisibility(listView, View.GONE);
                    break;
                case EMPTY:
                    setVisibility(parent, View.VISIBLE);
                    setVisibility(busyIndicator, View.INVISIBLE);
                    setVisibility(emptyText, View.VISIBLE);
                    setVisibility(listView, View.GONE);
                    break;
                case NORMAL:
                    setVisibility(parent, View.GONE);
                    setVisibility(busyIndicator, View.GONE);
                    setVisibility(emptyText, View.GONE);
                    setVisibility(listView, View.VISIBLE);
            }
        }

        private void setVisibility(View view, int visibility)
        {
            if (view != null)
                view.setVisibility(visibility);
        }
    }
}
