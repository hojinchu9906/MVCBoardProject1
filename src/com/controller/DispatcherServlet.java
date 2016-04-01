package com.controller;

import com.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjw on 2016-03-31.
 *      프런트컨트롤러 패턴 적용한 디스패처 서블릿 컨트롤러 클래스
 *                                                 --------
 */
public class DispatcherServlet extends HttpServlet{
    //HandlerMapping 역할하는 맵 생성
    //---------------
    private Map classMap=new HashMap();

    //모델을 담은 배열 생성
   String[] strClassName={"com.model.ListModel"};

    //뷰단 JSP을 담을 배열 생성
    String[] strCmd={"list"};

    @Override
    public void init(ServletConfig config) throws ServletException {
        try{
            for(int i=0;i<strCmd.length;i++){
                //사용자 요청에 따른 해당 모델 클래스 정보를 읽어오기 위해 해당 모델 객체 생성.(리플랙션)==>메소드,멤버변수,생성자 제어가 가능함.
                Class className=Class.forName(strClassName[i]);
                //메모리 할당
                Object object=className.newInstance();

                //이제 HandlerMapping 역할하는 맵객체에 모델을 넣어둔다.
                //이것은 DI와도 관계가 있어 미리 만들어둔것임.
                classMap.put(strClassName[i],object);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String cmdUri=req.getRequestURI();

            //      /MVCBoardProject/list.do
            cmdUri=cmdUri.substring(req.getContextPath().length()+1, cmdUri.lastIndexOf('.'));
            //     list


            //이제 해당 모델에 이 요청된 url을 던진다.
            //역시 DI와 관련되어 미리 모델 객체 생성함.

            //해시맴에 add되어 있는 것중에서 해당 url요청을 모델에 할당함.
            Model model =(Model)classMap.get(cmdUri);

            //해당 결과를 뷰단인 jsp에서 보여줌.
            String jsp=model.handlerRequest(req,resp);

            //핸들러매핑처리위함(확장자 얻음)
            String temp=jsp.substring(jsp.lastIndexOf('.')+1);
            if(temp.equals("do")){
                resp.sendRedirect(jsp);
            } else{
                RequestDispatcher requestDispatcher=req.getRequestDispatcher(jsp);
                requestDispatcher.forward(req,resp);
            }


        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}



















































