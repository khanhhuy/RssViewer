package assignment.rssviewer.activity.main;

import android.app.Fragment;

public abstract class BaseMainFragment extends Fragment
{
    private String title;
    private int iconResource;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String value)
    {
        this.title = value;
    }

    public int getIconResource()
    {
        return iconResource;
    }

    public void setIconResource(int value)
    {
        this.iconResource = value;
    }

    public abstract boolean isStatic();
}
