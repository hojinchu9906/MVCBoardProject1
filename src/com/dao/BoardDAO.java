package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjw on 2016-03-31.
 * 디비에 CRUD를 실제 요청하는 클래스
 */
public class BoardDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private final String URL="jdbc:oracle:thin:@211.238.142.20:1521:ORCL";

    //1.드라이버 등록-오라클 디비를 쓰기위해 드라이버 객체를 생성자에서 초기화함.
    public BoardDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //2. 이제 해당 드라이버에서 제공하는 여러 기능을 쓰기위해 디비와 접속함.
    public void getConnection(){
        try{
            connection= DriverManager.getConnection(URL,"scott","tiger");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //3. 당근 다 사용하고나선 커넥션을 끊어줘야 하므로 디비와의 연결해제 기능 정의.
    public void disConnection(){
        try{
            //연결해제에는 연결하기위해 필요한 리소스를 종료해주면 됨.
            //1) 쿼리문을 전송하는 스트림 객체인 preparestatement
            //2) 디비와의 커녁션 객체자체
            if(preparedStatement!=null) preparedStatement.close();
            if(connection!=null) connection.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //4. 기능.
    //1)목록보이기 ==>SELECT
    public List<BoardDTO> boardListData(int page){
        List<BoardDTO> list=new ArrayList<>();

        try{
            getConnection();

            String sql="SELECT no,subject,name,regdate,hit,group_tab,TO_CHAR(regdate,'YYYY-MM-DD') "
                        +"FROM board ORDER BY group_id DESC,group_step ASC";

            //스트림 객체 생성.
            preparedStatement=connection.prepareStatement(sql);

            //쿼리문 실행
            ResultSet resultSet=preparedStatement.executeQuery();

            //위 실행결과를 셋팅.
            int rowSize=5;      //페이지당 로갯수
            int i=0,j=0;

            int pageCnt=(page*rowSize)-rowSize;     //현재 페이지카운트

            //쿼리문 실행한 결과 내용 셋팅.
            while(resultSet.next()){
                if(i<rowSize && j>=pageCnt){
                    //한행-로단위
                    //VO는 BoardDTO이므로 객체 생성해서, 쿼리문 실행결과 내용을 설정함.
                    BoardDTO boardDTO=new BoardDTO();

                    boardDTO.setNo(resultSet.getInt(1));
                    boardDTO.setSubject(resultSet.getString(2));
                    boardDTO.setName(resultSet.getString(3));
                    boardDTO.setRegdate(resultSet.getDate(4));
                    boardDTO.setHit(resultSet.getInt(5));
                    boardDTO.setGroup_tab(resultSet.getInt(6));
                    boardDTO.setDbday(resultSet.getString(7));

                    //설정했으니  최종 리턴하는 내용을 담을 arrayList에 담아줌.
                    list.add(boardDTO);

                    //한 로우 설정했으니 다음 로우 설정하기 위해 줄바꿈
                    i++;
                }
                //페이지 변경시 증가
                j++;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            disConnection();
        }
        return list;
    }

    //총페이지 구하기 ==> CEIL(COUNT(*)/10
    public int boardTotal(){
        int total=0;

        try{
            getConnection();
            //쿼리문
            String sql="SELECT CEIL(COUNT(*)/5) FROM board";

            //스트림 객체 생성.
            preparedStatement=connection.prepareStatement(sql);

            //쿼리 실행.
            ResultSet resultSet=preparedStatement.executeQuery();

            resultSet.next();

            total=resultSet.getInt(1);

            resultSet.close();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            disConnection();
        }
        return total;
    }

    //총 개시글 카운팅.
    public int boardCount(){
        int total=0;

        try{
            getConnection();

            //쿼리문 작성.
            String sql="SELECT COUNT(*) FROM board";

            //스트림 객체 생성
            preparedStatement=connection.prepareStatement(sql);

            //해당 스트림 객체로 쿼리문 실행.
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();

            total=resultSet.getInt(1);

            //값을 구했으므로 리소스는 종료함.
            resultSet.close();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            disConnection();
        }

        return total;
    }
}




















































