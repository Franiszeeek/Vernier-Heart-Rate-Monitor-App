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
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
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

    @OnClick(R.id.button3)
    void OnClick_F3() {
        Disconnect();
    }


    @BindView(R.id.txtView1)
    TextView txtView1;

    @BindView(R.id.txtView2)
    TextView txtView2;

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
    private LineGraphSeries<DataPoint> mSeries1;
    private PointsGraphSeries<DataPoint> mSeries2;
    private BarGraphSeries<DataPoint> mSeries3;
    private BarGraphSeries<DataPoint> mSeries4;
    private BarGraphSeries<DataPoint> mSeries5;
    private BarGraphSeries<DataPoint> mSeries6;
    private BarGraphSeries<DataPoint> mSeries7;
    private BarGraphSeries<DataPoint> mSeries8;
    private BarGraphSeries<DataPoint> mSeries9;
    private BarGraphSeries<DataPoint> mSeries10;
    private BarGraphSeries<DataPoint> mSeries11;
    private BarGraphSeries<DataPoint> mSeries12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final double[] signal = {0};
        final int[] bpm = {0};

        final int[] tabHR = new int[100];
        final int[] counter = {0};
        final int[] hist = new int[10];


        bluetoothIn = new Handler() {
            public void handleMessage(Message msg) {
                String[] splitData;
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    int endOfLineIndex = readMessage.indexOf("C");
                    if (endOfLineIndex > 0) {
                        String dataInPrint = readMessage.substring(0, endOfLineIndex);
                        if (dataInPrint.charAt(0) == 'A')								//if it starts with # we know it is what we are looking for
                        {
                            final String data = dataInPrint.substring(1, endOfLineIndex);
                            splitData = data.split("B");

                            //System.out.println(data); //
                            if (Integer.parseInt(splitData[1]) != 0) {
                                txtView2.setText(splitData[1]);
                                if (counter[0] < 99){
                                    tabHR[counter[0]] = Integer.parseInt(splitData[1]);
                                    //System.out.println(tabHR[counter[0]]);

                                        for(int i = 0; i < hist.length; i++) {
                                            hist[i] = 0;
                                        }

                                        for(int i = 0; i < 100; i++) {

                                            if (120 <= tabHR[i] && tabHR[i] > 110) {
                                                hist[9] += 1;
                                            } else if (110 <= tabHR[i] && tabHR[i] > 105) {
                                                hist[8] += 1;
                                            } else if (105 <= tabHR[i] && tabHR[i] > 100) {
                                                hist[7] += 1;
                                            } else if (100 <= tabHR[i] && tabHR[i] > 95) {
                                                hist[6] += 1;
                                            } else if (95 <= tabHR[i] && tabHR[i] > 90) {
                                                hist[5] += 1;
                                            } else if (90 <= tabHR[i] && tabHR[i] > 85) {
                                                hist[4] += 1;
                                            } else if (85 <= tabHR[i] && tabHR[i] > 80) {
                                                hist[3] += 1;
                                            } else if (80 <= tabHR[i] && tabHR[i] > 75) {
                                                hist[2] += 1;
                                            } else if (75 <= tabHR[i] && tabHR[i] > 70) {
                                                hist[1] += 1;
                                            } else if (70 <= tabHR[i] && tabHR[i] > 60) {
                                                hist[0] += 1;
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
                mSeries1.appendData(new DataPoint(graphLastXValue, signal[0]), true, 80);

                if (bpm[0] > 60) {
                    mSeries2.appendData(new DataPoint(graphLastXValue, bpm[0]), true, 2000);
                }


                mSeries3.appendData(new DataPoint(1,hist[0]),true,1);
                mSeries4.appendData(new DataPoint(2,hist[1]),true,1);
                mSeries5.appendData(new DataPoint(3,hist[2]),true,1);
                mSeries6.appendData(new DataPoint(4,hist[3]),true,1);
                mSeries7.appendData(new DataPoint(5,hist[4]),true,1);
                mSeries8.appendData(new DataPoint(6,hist[5]),true,1);
                mSeries9.appendData(new DataPoint(7,hist[6]),true,1);
                mSeries10.appendData(new DataPoint(8,hist[7]),true,1);
                mSeries11.appendData(new DataPoint(9,hist[8]),true,1);
                mSeries12.appendData(new DataPoint(10,hist[9]),true,1);


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
/*
    public int[] histTab(int[] tabHR){
        int[] hist = new int[3];


        for(int i = 0; i < 100; i++){
            if(tabHR[i] >= 100){
                hist[0] += 1;
            }
            else if (tabHR[i] >= 80){
                hist[1] += 1;
            }
            else{
                hist[2] += 1;
            }
        }
        return hist;
    }
*/


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


        mSeries1 = new LineGraphSeries<>();
        mSeries1.setDrawDataPoints(false);
        mSeries1.setDrawBackground(false);
        mSeries1.setColor(Color.RED);


        graph.addSeries(mSeries1);
    }

    public void initGraph2(GraphView graph) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(2000);
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setMinY(60);
        graph.getViewport().setMaxY(120);

        //graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.RED);
        graph.getGridLabelRenderer().setGridColor(Color.RED);


        mSeries2 = new PointsGraphSeries<>();
        mSeries2.setColor(Color.RED);
        mSeries2.setShape(PointsGraphSeries.Shape.POINT);
        mSeries2.setSize(5);

        graph.addSeries(mSeries2);
    }



    public void initGraph3(GraphView graph) {


        mSeries3 = new BarGraphSeries<>();
        graph.addSeries(mSeries3);
        mSeries4 = new BarGraphSeries<>();
        graph.addSeries(mSeries4);
        mSeries5 = new BarGraphSeries<>();
        graph.addSeries(mSeries5);
        mSeries6 = new BarGraphSeries<>();
        graph.addSeries(mSeries6);
        mSeries7 = new BarGraphSeries<>();
        graph.addSeries(mSeries7);
        mSeries8 = new BarGraphSeries<>();
        graph.addSeries(mSeries8);
        mSeries9 = new BarGraphSeries<>();
        graph.addSeries(mSeries9);
        mSeries10 = new BarGraphSeries<>();
        graph.addSeries(mSeries10);
        mSeries11 = new BarGraphSeries<>();
        graph.addSeries(mSeries11);
        mSeries12 = new BarGraphSeries<>();
        graph.addSeries(mSeries12);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(11);
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(60);
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
                Log.e(TAG, "Error", e2);            }
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
            byte[] buffer = new byte[1024];
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
}
