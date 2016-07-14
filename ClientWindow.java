//package ht.hy.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-7-12.
 */
public class ClientWindow {

    private Socket socket = null;
    private int type=0;

    //定义该图形中所需的组件的引用
    private Frame frame;
    private Label sendL;
    private Label getL;
    private TextArea send;
    private TextArea get;
    private Button button;
    private JRadioButton select1;
    private JRadioButton select2;
    private JRadioButton select3;
    private TextField IPT;
    private TextField port;

    private Label IPL;
    private Label portL;

    private Dialog dialog;
    private Label d_label;

    ClientWindow(){
        init();
    }

    public void init(){
        //窗体初始化
        frame = new Frame("hy test");
        frame.setLayout(new FlowLayout());
        frame.setSize(450,600);//设置窗体大小 setBounds
        frame.setLocation(600,100);//设置窗体出现在屏幕的位置

        //组件初始化
        button = new Button("     Send      ");
        sendL = new Label("Send Content");
        send = new TextArea();
        getL = new Label("Get Content");
        get = new TextArea();
        select1 = new JRadioButton("      apple      ");
        select1.setSelected(true);
        select2 = new JRadioButton("      banana     ");
        select3 = new JRadioButton("      pear       ");
        IPL = new Label("IP Address");
        IPT = new TextField(45);
        IPT.setText("127.0.0.1");
        portL = new Label("Port            ");
        port = new TextField(45);
        port.setText("8888");

        dialog = new Dialog(frame,"Message");
        dialog.setLocation(700,200);
        dialog.setSize(200,200);
        dialog.setLayout(new FlowLayout());
        d_label = new Label();
        d_label.setForeground(Color.red);

        ButtonGroup group = new ButtonGroup();
        group.add(select1);
        group.add(select2);
        group.add(select3);
        JPanel panel = new JPanel();
        panel.add(select1);
        panel.add(select2);
        panel.add(select3);

        //添加组件

        frame.add(sendL);
        frame.add(send);
        frame.add(getL);
        frame.add(get);
        frame.add(panel);
        frame.add(button);
        frame.add(IPL);
        frame.add(IPT);
        frame.add(portL);
        frame.add(port);

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

        //Send事件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String IP = IPT.getText();
                String rgx = new String("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Pattern pattern = Pattern.compile(rgx);
                Matcher m = pattern.matcher(IP);
                boolean b = m.matches();

                if (!b){
                    d_label.setText("wrong ip");
                    dialog.setVisible(true);
                    return;
                }

                String port_str = port.getText();

                try {
                    int port_num = Integer.parseInt(port_str);
                    socket = new Socket(IP,port_num);
                } catch (NumberFormatException | IOException ex) {
                    wrongPort();
                    return;
                }


                if (select1.isSelected())
                    type=0;
                if (select2.isSelected())
                    type=1;
                if (select3.isSelected())
                    type = 2;

                //发送文本
                String text ="===begin==="+"\n"+ type+"\n" + send.getText();
                OutputStream outputStream = null;
                try {
                    outputStream = socket.getOutputStream();
                    outputStream.write(text.getBytes());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }finally {
                    try {
                        outputStream.flush();
                        socket.shutdownOutput();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


                InputStream in = null;
                try {
                    in = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String str = null;
                    while((str=br.readLine())!=null){
                        sb.append(str).append("\r\n");
                    }
                    get.append(sb.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });


    }

    public void wrongPort(){
        d_label.setText("inavailable port!");
        dialog.setVisible(true);
    }

    //加密方法类型匹配
    public String encrypt(String getContent){
        String finContent = null;
        switch (type){
            case 0:
                finContent = encrypt0(getContent);
                break;
            case 1:
                finContent = encrypt1(getContent);
                break;
            case 2:
                finContent = encrypt2(getContent);
                break;
            default:
                finContent = encrypt3(getContent);

        }
        return finContent;
    }

    //解密算法
    public String encrypt0(String getContent){
        return getContent;
    }

    public String encrypt1(String getContent){
        return null;
    }

    public String encrypt2(String getContent){
        return null;
    }

    public String encrypt3(String getContent){
        return null;
    }


    public TextArea getGet() {
        return get;
    }

    public static void main(String[] args) throws IOException {
        ClientWindow clientWindow = new ClientWindow();

//        Socket s = new Socket("192.168.56.1",8888);
//        OutputStream out =s.getOutputStream();
//        out.write("hello".getBytes());
//        s.close();
    }

}
