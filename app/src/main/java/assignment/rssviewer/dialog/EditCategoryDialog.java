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

public class EditCategoryDialog extends DialogFragment
{
    private static final String TITLE_KEY = "title";
    private static final String CONTENT_KEY = "content";
    private static final String IS_NEW_KEY = "isNew";
    private static final String ID_KEY = "id";

    private String title;
    private String content;
    private boolean isNew;
    private long id;
    private OnClosedListener listener;

    public static Bundle createArgs(String title, String content, boolean isNew, long id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        bundle.putString(CONTENT_KEY, content);
        bundle.putBoolean(IS_NEW_KEY, isNew);
        bundle.putLong(ID_KEY, id);
        return bundle;
    }

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
