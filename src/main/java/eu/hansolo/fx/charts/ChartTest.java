package eu.hansolo.fx.charts;

import eu.hansolo.fx.charts.data.XYData;
import eu.hansolo.fx.charts.data.XYDataObject;
import eu.hansolo.fx.charts.data.XYZData;
import eu.hansolo.fx.charts.data.XYZDataObject;
import eu.hansolo.fx.charts.data.YData;
import eu.hansolo.fx.charts.data.YDataObject;
import eu.hansolo.fx.charts.model.XYZChartModel;
import eu.hansolo.fx.charts.model.YChartModel;
import eu.hansolo.fx.charts.model.XYChartModel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by hansolo on 16.07.17.
 */
public class ChartTest extends Application {
    private static final Color[]         COLORS           = { Color.RED, Color.BLUE, Color.CYAN, Color.LIME };
    private static final Random          RND              = new Random();
    private XYChartModel<XYDataObject>   xyChartModel;
    private XYChart<XYDataObject>        scatterChart;
    private XYChart<XYDataObject>        lineChart;
    private XYChart<XYDataObject>        areaChart;
    private YChartModel<YDataObject>     yChartModel;
    private DonutChart<YDataObject>      donutChart;
    private XYZChartModel<XYZDataObject> xyzChartModel;
    private XYZChart<XYZDataObject>      bubbleChart;

    private long                         lastTimerCall;
    private AnimationTimer               timer;


    @Override public void init() {
        List<XYDataObject> xyData   = new ArrayList<>(1000);
        List<YDataObject>  yData    = new ArrayList<>(20);
        List<XYZDataObject> xyzData = new ArrayList<>(20);
        for (int i = 0 ; i < 1000 ; i++) {
            xyData.add(new XYDataObject(i, RND.nextDouble() * 20, "P" + i, COLORS[RND.nextInt(3)]));
        }
        for (int i = 0 ; i < 20 ; i++) {
            yData.add(new YDataObject(RND.nextDouble() * 10, "P" + i, COLORS[RND.nextInt(3)]));
            xyzData.add(new XYZDataObject(RND.nextDouble() * 10, RND.nextDouble() * 10, RND.nextDouble() * 25,"P" + i, COLORS[RND.nextInt(3)]));
        }

        xyChartModel  = new XYChartModel(xyData);
        scatterChart  = new XYChart(xyChartModel, ChartType.SCATTER);
        lineChart     = new XYChart(xyChartModel, ChartType.LINE);
        areaChart     = new XYChart(xyChartModel, ChartType.AREA, Color.BLACK, Color.rgb(255, 0, 0, 0.5));

        yChartModel   = new YChartModel(yData);
        donutChart    = new DonutChart(yChartModel);

        xyzChartModel = new XYZChartModel<>(xyzData);
        bubbleChart   = new XYZChart<>(xyzChartModel, ChartType.BUBBLE);

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now > lastTimerCall) {
                    List<XYData> xyItems = xyChartModel.getItems();
                    xyItems.forEach(item -> item.setY(RND.nextDouble() * 20));

                    List<YData> yItems = yChartModel.getItems();
                    yItems.forEach(item -> item.setY(RND.nextDouble() * 20));

                    List<XYZData> xyzItems = xyzChartModel.getItems();
                    xyzItems.forEach(item -> item.setZ(RND.nextDouble() * 25));

                    xyChartModel.refresh();
                    yChartModel.refresh();
                    xyzChartModel.refresh();
                    lastTimerCall = now;
                }
            }
        };
    }

    @Override public void start(Stage stage) {
        HBox pane = new HBox(scatterChart, lineChart, areaChart, donutChart, bubbleChart);
        pane.setSpacing(10);

        Scene scene = new Scene(new StackPane(pane));

        stage.setTitle("Charts");
        stage.setScene(scene);
        stage.show();

        timer.start();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}