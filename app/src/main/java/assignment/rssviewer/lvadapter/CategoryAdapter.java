package assignment.rssviewer.lvadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.model.Category;

/**
 * Created by Prozacs on 26/03/2015.
 */
public class CategoryAdapter extends ArrayAdapter<Category>
{
    public CategoryAdapter(Context context, List<Category> categories)
    {
        super(context, android.R.layout.simple_list_item_activated_2, android.R.id.text1, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

        Category source = super.getItem(position);
        text1.setText(source.getName());
        text2.setText("RSS Sources: " + source.getRssSources().size());

        return view;
    }


}
