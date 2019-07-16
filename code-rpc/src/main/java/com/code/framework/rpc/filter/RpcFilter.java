package com.code.framework.rpc.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.code.framework.rpc.common.RpcExporterContext;
import com.code.framework.rpc.common.RpcFactory;
import com.code.framework.rpc.common.RpcRequest;
import com.code.framework.rpc.common.RpcResponse;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * RPC拦截器
 * @author SONGJIUHUA386
 * @since 2019/5/13
 */
public class RpcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * 1、RPC服务端监听到特定rpc地址时，doFilter解析客户端的bean名称、接口、以及接口参数
     * 2、调用相应的bean接口，并得到返回结果写回response
     * @param req
     * @param resp
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try{
            inputStreamReader = new InputStreamReader(request.getInputStream(), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String requestBody = "";
            String temp ;
            while ((temp = reader.readLine()) != null) {
                requestBody += temp;
            }
            //获取请求参数
            RpcRequest rpcRequest = JSONObject.parseObject(requestBody, RpcRequest.class);
            if (StringUtils.isEmpty(rpcRequest.getMethod())){
                throw new RuntimeException("rpc method parameter can not be null");
            }
            //获取当前rpc请求的服务端配置信息：bean和method
            RpcExporterContext exporterContext = RpcFactory.getExporterMap(rpcRequest.getMethod());
            //通过反射调用rpc服务端具体的接口方法
            Object result = exporterContext.getMethod().invoke(((Class) exporterContext.getBean()).newInstance(), rpcRequest.getParams().toArray());
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setResult(result);
            response.getWriter().write(JSON.toJSONString(rpcResponse));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                reader.close();
            }
            if (inputStreamReader != null){
                inputStreamReader.close();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
