package com.example.maciejmysinski.vernierheartratemonitorapp;

import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.button)
    void OnClick_F3() {
        Disconnect();
    }

    @BindView(R.id.txtView)
    TextView txtView;

    @BindView(R.id.graph1)
    GraphView graph1;

    @BindView(R.id.graph2)
    GraphView graph2;

    @BindView(R.id.graph3)
    GraphView graph3;

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter btAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "MY_APP_DEBUG_TAG";

    Handler bluetoothIn;
    final int handlerState = 0;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;

    private LineGraphSeries<DataPoint> lSeries;
    private PointsGraphSeries<DataPoint> pSeries;
    private BarGraphSeries<DataPoint> bSeries0;
    private BarGraphSeries<DataPoint> bSeries1;
    private BarGraphSeries<DataPoint> bSeries2;
    private BarGraphSeries<DataPoint> bSeries3;
    private BarGraphSeries<DataPoint> bSeries4;
    private BarGraphSeries<DataPoint> bSeries5;
    private BarGraphSeries<DataPoint> bSeries6;
    private BarGraphSeries<DataPoint> bSeries7;
    private BarGraphSeries<DataPoint> bSeries8;
    private BarGraphSeries<DataPoint> bSeries9;
    private BarGraphSeries<DataPoint> bSeries10;
    private BarGraphSeries<DataPoint> bSeries11;
    private BarGraphSeries<DataPoint> bSeries12;
    private BarGraphSeries<DataPoint> bSeries13;
    private BarGraphSeries<DataPoint> bSeries14;
    private BarGraphSeries<DataPoint> bSeries15;
    private BarGraphSeries<DataPoint> bSeries16;
    private BarGraphSeries<DataPoint> bSeries17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final double[] signal = {0};
        final int[] bpm = {0};
        final int[] tabHR = new int[100];
        final int[] counter = {0};
        final int[] hist = new int[100];

        bluetoothIn = new Handler() {
            public void handleMessage(Message msg) {
                String[] splitData;
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    int endOfLineIndex = readMessage.indexOf("*");
                    if (endOfLineIndex > 0) {
                        String dataInPrint = readMessage.substring(0, endOfLineIndex);
                        if (dataInPrint.charAt(0) == '#')
                        {
                            final String data = dataInPrint.substring(1, endOfLineIndex);
                            splitData = data.split(":");

                            if (Integer.parseInt(splitData[1]) >= 50 && Integer.parseInt(splitData[1]) <= 140) {
                                txtView.setText(splitData[1]);

                                if (counter[0] < 99){
                                    tabHR[counter[0]] = Integer.parseInt(splitData[1]);

                                        for(int i = 0; i < hist.length; i++) {
                                            hist[i] = 0;
                                        }

                                        for(int i = 0; i < hist.length; i++) {

                                            if (50 <= tabHR[i] && tabHR[i] < 55) {
                                                hist[0] += 1;
                                            } else if (55 <= tabHR[i] && tabHR[i] < 60) {
                                                hist[1] += 1;
                                            } else if (60 <= tabHR[i] && tabHR[i] < 65) {
                                                hist[2] += 1;
                                            } else if (65 <= tabHR[i] && tabHR[i] < 70) {
                                                hist[3] += 1;
                                            } else if (70 <= tabHR[i] && tabHR[i] < 75) {
                                                hist[4] += 1;
                                            } else if (75 <= tabHR[i] && tabHR[i] < 80) {
                                                hist[5] += 1;
                                            } else if (80 <= tabHR[i] && tabHR[i] < 85) {
                                                hist[6] += 1;
                                            } else if (85 <= tabHR[i] && tabHR[i] < 90) {
                                                hist[7] += 1;
                                            } else if (90 <= tabHR[i] && tabHR[i] < 95) {
                                                hist[8] += 1;
                                            } else if (95 <= tabHR[i] && tabHR[i] < 100) {
                                                hist[9] += 1;
                                            } else if (100 <= tabHR[i] && tabHR[i] < 105) {
                                                hist[10] += 1;
                                            } else if (105 <= tabHR[i] && tabHR[i] < 110) {
                                                hist[11] += 1;
                                            } else if (110 <= tabHR[i] && tabHR[i] < 115) {
                                                hist[12] += 1;
                                            } else if (115 <= tabHR[i] && tabHR[i] < 120) {
                                                hist[13] += 1;
                                            } else if (120 <= tabHR[i] && tabHR[i] < 125) {
                                                hist[14] += 1;
                                            } else if (125 <= tabHR[i] && tabHR[i] < 130) {
                                                hist[15] += 1;
                                            } else if (130 <= tabHR[i] && tabHR[i] < 135) {
                                                hist[16] += 1;
                                            } else if (135 <= tabHR[i] && tabHR[i] < 140) {
                                                hist[17] += 1;
                                            } else {
                                            }
                                        }
                                    counter[0] += 1;
                                }
                                else {counter[0] = 0;
                                }
                            }
                            bpm[0] = Integer.parseInt(splitData[1]);
                            signal[0] = Double.parseDouble(splitData[0]);
                        }
                    }
                }
            }
        };

        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 1d;
                lSeries.appendData(new DataPoint(graphLastXValue, signal[0]), true, 100);
                pSeries.appendData(new DataPoint(graphLastXValue, bpm[0]), true, 2000);

                bSeries0.appendData(new DataPoint(1, hist[0]), true, 1);
                bSeries1.appendData(new DataPoint(2, hist[1]), true, 1);
                bSeries2.appendData(new DataPoint(3, hist[2]), true, 1);
                bSeries3.appendData(new DataPoint(4, hist[3]), true, 1);
                bSeries4.appendData(new DataPoint(5, hist[4]), true, 1);
                bSeries5.appendData(new DataPoint(6, hist[5]), true, 1);
                bSeries6.appendData(new DataPoint(7, hist[6]), true, 1);
                bSeries7.appendData(new DataPoint(8, hist[7]), true, 1);
                bSeries8.appendData(new DataPoint(9, hist[8]), true, 1);
                bSeries9.appendData(new DataPoint(10, hist[9]), true, 1);
                bSeries10.appendData(new DataPoint(11, hist[10]), true, 1);
                bSeries11.appendData(new DataPoint(12, hist[11]), true, 1);
                bSeries12.appendData(new DataPoint(13, hist[12]), true, 1);
                bSeries13.appendData(new DataPoint(14, hist[13]), true, 1);
                bSeries14.appendData(new DataPoint(15, hist[14]), true, 1);
                bSeries15.appendData(new DataPoint(16, hist[15]), true, 1);
                bSeries16.appendData(new DataPoint(17, hist[16]), true, 1);
                bSeries17.appendData(new DataPoint(18, hist[17]), true, 1);

                mHandler.postDelayed(this, 1);
            }
        };

        mHandler.postDelayed(mTimer, 1);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        initGraph1(graph1);
        initGraph2(graph2);
        initGraph3(graph3);
    }

    public void initGraph1(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(80);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(6);

        graph.getGridLabelRenderer().setLabelVerticalWidth(80);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(Color.RED);

        lSeries = new LineGraphSeries<>();
        lSeries.setDrawDataPoints(false);
        lSeries.setDrawBackground(false);
        lSeries.setColor(Color.YELLOW);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        graph.addSeries(lSeries);
    }

    public void initGraph2(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(2000);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(140);

        graph.getGridLabelRenderer().setLabelVerticalWidth(55);
        //graph.getGridLabelRenderer().setLabelHorizontalHeight(55);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setGridColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Heart rate [bpm]");
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(21);

        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);


        pSeries = new PointsGraphSeries<>();
        pSeries.setColor(Color.YELLOW);
        pSeries.setShape(PointsGraphSeries.Shape.POINT);
        pSeries.setSize(3);

        StaticLabelsFormatter staticLabelFormatter = new StaticLabelsFormatter(graph);
        staticLabelFormatter.setVerticalLabels(new String[]{"50","60","70","80",
                "90","100","110","120","130","140"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelFormatter);
        graph.addSeries(pSeries);
    }

    public void initGraph3(GraphView graph) {

        bSeries0 = new BarGraphSeries<>();
        graph.addSeries(bSeries0);
        bSeries1 = new BarGraphSeries<>();
        graph.addSeries(bSeries1);
        bSeries2 = new BarGraphSeries<>();
        graph.addSeries(bSeries2);
        bSeries3 = new BarGraphSeries<>();
        graph.addSeries(bSeries3);
        bSeries4 = new BarGraphSeries<>();
        graph.addSeries(bSeries4);
        bSeries5 = new BarGraphSeries<>();
        graph.addSeries(bSeries5);
        bSeries6 = new BarGraphSeries<>();
        graph.addSeries(bSeries6);
        bSeries7 = new BarGraphSeries<>();
        graph.addSeries(bSeries7);
        bSeries8 = new BarGraphSeries<>();
        graph.addSeries(bSeries8);
        bSeries9 = new BarGraphSeries<>();
        graph.addSeries(bSeries9);
        bSeries10 = new BarGraphSeries<>();
        graph.addSeries(bSeries10);
        bSeries11 = new BarGraphSeries<>();
        graph.addSeries(bSeries11);
        bSeries12 = new BarGraphSeries<>();
        graph.addSeries(bSeries12);
        bSeries13 = new BarGraphSeries<>();
        graph.addSeries(bSeries13);
        bSeries14 = new BarGraphSeries<>();
        graph.addSeries(bSeries14);
        bSeries15 = new BarGraphSeries<>();
        graph.addSeries(bSeries15);
        bSeries16 = new BarGraphSeries<>();
        graph.addSeries(bSeries16);
        bSeries17 = new BarGraphSeries<>();
        graph.addSeries(bSeries17);

        bSeries0.setColor(Color.YELLOW);
        bSeries1.setColor(Color.YELLOW);
        bSeries2.setColor(Color.YELLOW);
        bSeries3.setColor(Color.YELLOW);
        bSeries4.setColor(Color.YELLOW);
        bSeries5.setColor(Color.YELLOW);
        bSeries6.setColor(Color.YELLOW);
        bSeries7.setColor(Color.YELLOW);
        bSeries8.setColor(Color.YELLOW);
        bSeries9.setColor(Color.YELLOW);
        bSeries10.setColor(Color.YELLOW);
        bSeries11.setColor(Color.YELLOW);
        bSeries12.setColor(Color.YELLOW);
        bSeries13.setColor(Color.YELLOW);
        bSeries14.setColor(Color.YELLOW);
        bSeries15.setColor(Color.YELLOW);
        bSeries16.setColor(Color.YELLOW);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-1);
        graph.getViewport().setMaxX(19);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setGridColor(Color.RED);

        StaticLabelsFormatter staticLabelFormatter = new StaticLabelsFormatter(graph);
        staticLabelFormatter.setHorizontalLabels(new String[]{"","50","","60","","70","","80","",
                "90","","100","","110","","120","","130","","140",""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelFormatter);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(120);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Number of occurrences");
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(21);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(myUUID);
    }

    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "Error", e2);
            }
        }

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        mConnectedThread.write("x");
    }

    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[512];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            btSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error", e);

        }
        mHandler.removeCallbacks(mTimer);
    }

    private void Disconnect() {
        if (btSocket != null)
        {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}