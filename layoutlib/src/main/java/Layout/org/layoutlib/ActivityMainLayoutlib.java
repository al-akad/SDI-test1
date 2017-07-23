package Layout.org.layoutlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by user on 24.05.2017.
 */

public class ActivityMainLayoutlib extends LinearLayout {
    private Toolbar toolbar_1;

    public ActivityMainLayoutlib(Context context) {
        super(context);
        InitMain();
    }

    public ActivityMainLayoutlib(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitMain();
    }

    public ActivityMainLayoutlib(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitMain();
    }

    public ActivityMainLayoutlib(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitMain();
    }
    private void InitMain() {
        inflate(getContext(), R.layout.activimainlayoutlib, this);
        this.toolbar_1 = (Toolbar) findViewById(R.id.app_bar);



    }
}
