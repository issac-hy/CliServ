//package ht.hy.server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016-7-12.
 */
public class ServerWindow {

    public static boolean isWork_status() {
        return work_status;
    }

    private volatile static boolean work_status = false;
    private ServerSocket serverSocket = null;

    //定义该图形中所需的组件的引用
    private Frame frame;
    private Label getL;
    private TextArea get;
    private TextField port;

    private Label portL;
    private Button open;
    private Button close;

    private Dialog dialog;
    private Label d_label;

    ServerWindow(){
        init();
    }

    public void init(){
        //窗体初始化
        frame = new Frame("hy test");
        frame.setLayout(new FlowLayout());
        frame.setSize(450,600);//设置窗体大小 setBounds
        frame.setLocation(300,100);//设置窗体出现在屏幕的位置

        //组件初始化
        getL = new Label("Get Content");
        get = new TextArea();
        portL = new Label("Port            ");
        port = new TextField(45);
        port.setText("8888");
        open = new Button("open");
        open.setEnabled(true);
        close = new Button("close");
        close.setEnabled(false);
        dialog = new Dialog(frame,"Message");
        dialog.setLocation(400,200);
        dialog.setSize(200,200);
        dialog.setLayout(new FlowLayout());
        d_label = new Label();
        //d_label.setFont(new Font("Monospaced",Font.BOLD,10));
        d_label.setForeground(Color.red);

        //添加组件

        frame.add(getL);
        frame.add(get);
        frame.add(portL);
        frame.add(port);
        frame.add(open);
        frame.add(close);

        dialog.add(d_label);

        //加载事件
        myEvent();

        //窗体可见
        frame.setVisible(true);
    }

    private void myEvent(){

        //关闭窗体
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //关闭对话框
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.setVisible(false);
            }
        });

        //open按钮事件 client
        /*open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String IP = IPL.getText();
                String rgx = new String("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Pattern pattern = Pattern.compile(rgx);
                Matcher m = pattern.matcher(IP);
                boolean b = m.matches();

                if (!b){
                    d_label.setText("wrong ip");
                    dialog.setVisible(true);
                }
            }
        });*/

        //open按钮事件 server
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String port_str = port.getText();

                try {
                    int port_num = Integer.parseInt(port_str);
                    serverSocket = new ServerSocket(port_num);
                } catch (NumberFormatException | IOException ex) {
                    wrongPort();
                    return;
                }
                work_status = true;
                open.setEnabled(false);
                close.setEnabled(true);
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                work_status = false;
                open.setEnabled(true);
                close.setEnabled(false);
            }
        });

    }

    public void wrongPort(){
        d_label.setText("inavailable port!");
        dialog.setVisible(true);
    }

    public TextArea getGet() {
        return get;
    }

    public static void main(String[] args) throws IOException {
        ServerWindow serverWindow = new ServerWindow();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //ServerSocket serverSocket = new ServerSocket(8888);
        while(true){
            while(work_status){
                Socket socket = serverWindow.serverSocket.accept();
                //Socket socket = serverSocket.accept();
//                InputStream in = socket.getInputStream();
//                byte[] buf = new byte[1024];
//                int num = in.read(buf);
//                String str = new String(buf,0,num);
//                System.out.println(str);


                executorService.execute(new SocketThread(socket,serverWindow));
                //Thread thread = new Thread(new SocketThread(socket,serverWindow));
                //thread.start();
            }


        }
    }
}
