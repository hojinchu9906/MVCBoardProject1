package com.model;

import com.dao.BoardDAO;
import com.dao.BoardDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by cjw on 2016-03-31.
 */
public class ListModel implements Model {
    @Override
    public String handlerRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        //모델에서 해당 기능을 갖고 있는 DAO단의 메소드를 호출하기 위해 DAO객체 생성;
        BoardDAO boardDAO=new BoardDAO();

        //해당 dao기능 호출하기 위해 몇가지 파라미터 값 할당함.
        //해당 파라미터는 요청하는 jsp 페이지에서 파라미터를 설정해서 넘겨줌.
        String startPage=httpServletRequest.getParameter("page");       //이 페이지는 그때그때마다 달라짐.

        if(startPage==null)
            startPage="1";

        //사용자가 그때그때 선택해서 요청한 페이지가 현재페이지가 됨.
        int curPage=Integer.parseInt(startPage);

        //dao 호출
        List<BoardDTO> boardDTOList=boardDAO.boardListData(curPage);
        //위 dao 실행결과를 jsp 뷰단에 넘겨주면 됨.
        //그 설정을 함.

        //전체 페이지수 구함.
        int totalPage=boardDAO.boardTotal();
        //전체 로우, 개시된 글수 구함.
        int count= boardDAO.boardCount();

        //개시된 글수중 현재 페이지에서의 개시글 수 구함.
        count=count-((curPage*10)-10);

        /*
        번호 	제목 	이름 	작성일 	조회수
        56 	답변형게시판 제작 	홍길동 	2016-03-31 	1
        55 	답변형게시판 제작 	홍길동 	2016-03-31 	0
        54 	답변형게시판 제작 	홍길동 	2016-03-31 	0
        53 	답변형게시판 제작 	홍길동 	2016-03-31 	0
        52 	답변형게시판 제작 	홍길동 	2016-03-31 	0
                [ 1 ] [ 2 ] [ 3 ] [ 4 ] [ 5 ]      3 page / 14 pages
         */
        String today=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int block=5;
        int fromPage=((curPage-1)/block*block)+1;
        int toPage=((curPage-1)/block*block)+block;

        if(toPage>totalPage)
            toPage=totalPage;


        String msg="관리자가 삭제한 게시물입니다.";


        //DAO 기능 실행후 디비로부터의 결과값을 뷰단에 넘겨주기위한 설정.(앞의것이 넘겨주는것임)
        httpServletRequest.setAttribute("list",boardDTOList);
        httpServletRequest.setAttribute("curpage",curPage);
        httpServletRequest.setAttribute("totalpage",totalPage);
        httpServletRequest.setAttribute("count",count);
        httpServletRequest.setAttribute("today",today);
        httpServletRequest.setAttribute("block",block);
        httpServletRequest.setAttribute("fromPage",fromPage);
        httpServletRequest.setAttribute("toPage",toPage);
        httpServletRequest.setAttribute("msg",msg);

        return "board/list.jsp";
    }
}













































