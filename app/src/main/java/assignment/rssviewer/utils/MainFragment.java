package assignment.rssviewer.utils;

import android.support.v4.app.Fragment;

import java.util.List;

public abstract class MainFragment extends Fragment
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
