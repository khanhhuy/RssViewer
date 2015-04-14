package assignment.rssviewer.utils;

import android.util.SparseBooleanArray;
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
}
