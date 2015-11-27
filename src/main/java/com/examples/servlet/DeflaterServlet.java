package com.examples.servlet;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.Deflater;

/**
 * Created by chao.qianc on 2015/11/27.
 */
@WebServlet("/online")
public class DeflaterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deflateOutput(resp, createData(Integer.parseInt(req.getParameter("size"))));
    }

    private static Random random = new Random();

    private String createData(int size) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("notify", true);
        Set<String> beatData = new HashSet<String>();
        for (int i = 0; i < size; i++) {
            beatData.add("i-xxxxxxx" + random.nextLong() + "xxxxxxxx" + random.nextLong());
        }
        returnData.put("beatData", beatData);
        return JSON.toJSONString(returnData);
    }


    private void deflateOutput(HttpServletResponse response, String result) throws IOException {
        byte[] input = result.getBytes("UTF-8");
        int outputLength = getOutputLength(input.length);
        System.out.println(outputLength);

        byte[] output = new byte[outputLength];
        Deflater deflater = new Deflater(5, true);
        deflater.setInput(input);
        deflater.finish();
        int compressedDataLength = deflater.deflate(output);
        response.addHeader("Content-Encoding", "deflate");
        response.addHeader("Content-Type", "text/html");
        response.setContentLength(compressedDataLength);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(output, 0, compressedDataLength);
        servletOutputStream.flush();
    }

    private int getOutputLength(int strLength) {
        return strLength / 2 < 10240 ? 10240 : strLength / 2;
    }
}
