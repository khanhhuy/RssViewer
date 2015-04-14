package assignment.rssviewer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class MessageDialog extends DialogFragment
{
    private static final MessageDialog instance = new MessageDialog();
    private String title;
    private String content;

    public static void show(FragmentManager fragmentManager, String title, String content)
    {
        instance.title = title;
        instance.content = content;
        instance.show(fragmentManager, "message");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
               .setMessage(content)
               .setNeutralButton("OK", null);
        return builder.create();
    }
}
