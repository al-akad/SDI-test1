package Layout.org.layoutlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 25.05.2017.
 */

public class MpchartActivityLib extends AppCompatActivity {
    GraphView graph;
    private LineGraphSeries<DataPoint> series;
    DBConnections db;
    private XYPlot plot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpchartlayoutlib);
       graph = (GraphView) findViewById(R.id.graph);
        db = new DBConnections(this);
        series = new LineGraphSeries<>(generateData());
        Calendar calendar = Calendar.getInstance();
       Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        graph.addSeries(series);
        plot = (XYPlot) findViewById(R.id.plot);


        XYSeries Rpm;
        Rpm = new SimpleXYSeries(Arrays.asList(db.arry2()),
                Arrays.asList(db.arry()),"Rpm");

        XYSeries Current = new SimpleXYSeries(
                Arrays.asList(db.arry2()), Arrays.asList(db.arry1()), "Current");

        XYSeries Voltage = new SimpleXYSeries(
                Arrays.asList(db.arry2()), Arrays.asList(db.arry3()), "Voltage");


        XYSeries Temperatur = new SimpleXYSeries(
                Arrays.asList(db.arry2()), Arrays.asList(db.arry4()), "Temperature");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);

        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));


        // add a new series' to the xyplot:
        plot.addSeries(Rpm, new LineAndPointFormatter(Color.GREEN,Color.GREEN,null,null));
        plot.addSeries(Current, new LineAndPointFormatter(Color.RED,Color.RED,null,null));
        plot.addSeries(Voltage, new LineAndPointFormatter(Color.YELLOW,Color.YELLOW,null,null));
        plot.addSeries(Temperatur, new LineAndPointFormatter(Color.BLUE,Color.BLUE,null,null));


        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new MyDataFormat());



        // customize our domain/range labels
        plot.setDomainLabel("Time");
        plot.setRangeLabel("Value");
        // get rid of decimal points in our range labels:





        // create a simple date format that draws on the year portion of our timestamp.
        PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.STRETCH_HORIZONTAL);








        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1000);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
//        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
//        graph.getViewport().setScrollableY(true); // enables vertical scrolling




        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
               } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + "";
                }
            }
        });

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

      // set manual x bounds to have nice stEEWWEEEEEEEEWEEEWEWeps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

     // as we use dates as labels, the human rounding to nice readable numbers
     // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }













    private DataPoint[] generateData() {
        Integer[] rpmData = db.arry();
        Integer[] TimeData = db.arry2();
        int count = rpmData.length;


        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
        // Wert von Timestamp
            double x = TimeData[i];

            double y = rpmData[i];
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


    public Context getActivity() {
        return this;
    }
}
