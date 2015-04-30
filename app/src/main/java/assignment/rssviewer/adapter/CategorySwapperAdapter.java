package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.utils.TwoTextHolder;

/**
 * Created by Huy on 4/21/2015.
 */
public class CategorySwapperAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> categoryList;

    public CategorySwapperAdapter(Context ctx, int txtViewResourceId, List<Category> categoryList) {
        super(ctx, txtViewResourceId, categoryList);
        this.context = ctx;
        this.categoryList = categoryList;
    }

    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup parent){
        return getCustomView(pos, convertView, parent);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        return getCustomView(pos, convertView, parent);
    }

    public  View getCustomView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View categorySpinner = inflater.inflate(R.layout.list_item_category_spinner, parent, false);

        TextView categoryTitle = (TextView) categorySpinner.findViewById(R.id.categoryTitle);
        categoryTitle.setText(categoryList.get(pos).getName());

        return categorySpinner;
    }
}
