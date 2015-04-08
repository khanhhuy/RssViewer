package assignment.rssviewer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import assignment.rssviewer.R;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class EditCategoryDialog extends DialogFragment
{
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_KEY = "content";
    public static final String IS_NEW_KEY = "isNew";
    public static final String ID_KEY = "id";

    private String title;
    private String content;
    private boolean isNew;
    private long id;
    private OnClosedListener listener;

    public void setOnClosedListener(OnClosedListener value)
    {
        this.listener = value;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.new_category_dialog_layout, null);
        final EditText txtContent = (EditText) contentView.findViewById(R.id.txtName);
        txtContent.setText(content);

        builder.setTitle(this.title)
               .setView(contentView)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       if (listener != null)
                           listener.onAccepted(isNew, id, txtContent.getText().toString());
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       if (listener != null)
                           listener.onCanceled(isNew, id, txtContent.getText().toString());
                   }
               });

        return builder.create();
    }

    @Override
    public void setArguments(Bundle args)
    {
        title = args.getString(TITLE_KEY);
        content = args.getString(CONTENT_KEY);
        isNew = args.getBoolean(IS_NEW_KEY);
        id = args.getLong(ID_KEY);
    }

    public interface OnClosedListener
    {
        public void onAccepted(boolean isNew, long id, String content);

        public void onCanceled(boolean isNew, long id, String content);
    }
}
