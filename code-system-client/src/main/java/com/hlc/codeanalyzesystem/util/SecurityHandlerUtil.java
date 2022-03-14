package com.hlc.codeanalyzesystem.util;

import cn.hutool.json.JSONUtil;
import com.hlc.codeanalyzesystem.entity.ResultJSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SecurityHandlerUtil {
    public static void responseHandler(HttpServletResponse response, ResultJSON result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(result));
        writer.flush();
        writer.close();
    }
}
