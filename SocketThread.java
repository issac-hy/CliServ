//package ht.hy.server;

//import ht.hy.server.ServerWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Administrator on 2016/7/13.
 */
public class SocketThread implements Runnable {
    private Socket socket;
    private ServerWindow serverWindow;
    private int type = 0;

    SocketThread(Socket socket,ServerWindow serverWindow){
        this.socket = socket;
        this.serverWindow = serverWindow;
    }


    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String str;
            StringBuilder sb =new StringBuilder();
            while ((str = br.readLine())!=null){
                //数据开始，并且接下来传送加密方式
                if (str.equals("===begin===")){
                    type = Integer.parseInt(br.readLine());
                    continue;
                }else{
                    sb.append(str).append("\n");
                }
            }

            String getContent = sb.toString();
            System.out.println(getContent);

            String finContent = decode(getContent);

            synchronized (serverWindow.getClass()){
                serverWindow.getGet().append(finContent);
            }

            socket.getOutputStream().write(("get it\n").getBytes());
            socket.shutdownOutput();

        } catch (IOException e) {
            e.printStackTrace();}
        //}finally {
        //    try {
        //        socket.close();
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //}
    }

    //解密方法类型匹配
    public String decode(String getContent){
        String finContent = null;
        switch (type){
            case 0:
                finContent = decode0(getContent);
                break;
            case 1:
                finContent = decode1(getContent);
                break;
            case 2:
                finContent = decode2(getContent);
                break;
            default:
                finContent = decode3(getContent);

        }
        return finContent;
    }


    //解密算法
    public String decode0(String getContent){
        return getContent;
    }

    public String decode1(String getContent){
        return null;
    }

    public String decode2(String getContent){
        return null;
    }

    public String decode3(String getContent){
        return null;
    }
}
