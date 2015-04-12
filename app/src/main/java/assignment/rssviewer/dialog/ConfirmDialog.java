package assignment.rssviewer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDialog extends DialogFragment
{
    private static final String TITLE_KEY = "title";
    private static final String CONTENT_KEY = "content";
    private String title;
    private String content;
    private OnClosedListener onClosedListener;

    public static Bundle createArgs(String title, String content)
    {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        bundle.putString(CONTENT_KEY, content);
        return bundle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
               .setMessage(content)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       if (onClosedListener != null)
                       {
                           onClosedListener.onAccepted();
                       }
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       if (onClosedListener != null)
                       {
                           onClosedListener.onCanceled();
                       }
                   }
               });
        return builder.create();
    }

    @Override
    public void setArguments(Bundle args)
    {
        title = args.getString(TITLE_KEY);
        content = args.getString(CONTENT_KEY);
    }

    public void setOnClosedListener(OnClosedListener value)
    {
        this.onClosedListener = value;
    }

    public interface OnClosedListener
    {
        public void onAccepted();

        public void onCanceled();
    }
}
