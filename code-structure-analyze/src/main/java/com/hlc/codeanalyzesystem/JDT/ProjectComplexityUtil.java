package com.hlc.codeanalyzesystem.JDT;

import com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap.Ex_caculate_test;
import com.hlc.codeanalyzesystem.entity.Graph;
import com.hlc.codeanalyzesystem.util.PowFitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectComplexityUtil {
    public static String calProjectComplex(Graph graph) throws Exception {
        List<ArrayList<String>> dpyg = new ArrayList<>();
        int [][]g = graph.getGraph();
        Map<Integer,String> map2 = graph.getIdNameMapping();
        for(int i = 0 ; i < g.length;i++)
        {
            for (int j = 0 ; j < g[0].length; j++)
            {
                if(g[i][j] == 1)
                {
                    String from = map2.get(i);
                    String to = map2.get(j);
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(from);
                    temp.add(to);
                    dpyg.add(temp);
                }
            }
        }
        List<List<Double>> datas = Ex_caculate_test.mmCalculate(dpyg);
        return PowFitter.complexityFit(datas);
    }
}
