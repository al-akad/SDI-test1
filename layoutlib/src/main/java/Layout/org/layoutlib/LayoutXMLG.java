package Layout.org.layoutlib;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 03.07.2017.
 */

public class LayoutXMLG extends AutoLayoutlib{

    private Button LeftClieck;
    private Button RightClieck;
    private Button FrontClieck;
    private Button BackClieck;
    private TextView Lenkrad;
    private TextView Lenkrad1;
    private EditText et;





    public LayoutXMLG(Context context) {
        super(context);
      initLayout();
    }

    void initLayout() {
        inflate(getContext(), R.layout.layoutxmlgenerieren, this);


   //     this.LeftClieck = (Button) findViewById(R.id.button23);
   //     this.RightClieck = (Button) findViewById(R.id.button33);
   //     this.FrontClieck = (Button) findViewById(R.id.button55);
   //     this.BackClieck = (Button) findViewById(R.id.button44);



    }
}
