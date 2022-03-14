package com.hlc.codeanalyzesystem.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PythonCallUtil {
    static String pythonPath = "C:\\Users\\hasaki\\anaconda3\\envs\\py38\\python.exe";
    public static List<String> executePythonCode(String path, String []args) throws Exception{
        Process proc;
        List<String> ast = new ArrayList<>();
        try {
            String commandStr = pythonPath + " " + path;
            for(String arg : args)
            {
                commandStr += " " + arg;
            }
            proc = Runtime.getRuntime().exec(commandStr);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                ast.add(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            System.out.println("exception");
        } catch (InterruptedException e) {
            System.out.println("exception2");
        }
        return ast;
    }

    public static void main(String[] args) throws Exception {
        String path = "D:\\pythonProject\\pythonProject\\astgenerator.py";
        String []arguments = new String[]{"D:\\leetCodePractice\\src\\com\\hlc\\alibaba\\ali1.java"};
        List<String> res = executePythonCode(path,arguments);
        for(String r : res)
            System.out.println(r);
    }
}
