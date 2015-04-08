package assignment.rssviewer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Prozacs on 26/03/2015.
 */
public class ConfirmDialog extends DialogFragment
{
    private String title;
    private String content;
    private OnClosedListener onClosedListener;

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
        title = args.getString("title");
        content = args.getString("content");
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
