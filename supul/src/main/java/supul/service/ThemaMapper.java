package supul.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Thema;



@Mapper
public interface ThemaMapper {

	// 매장 데이터 조회
	List<Branch> brList(Branch bd);

	// 테마 데이터 조회
	List<Thema> tmList(Thema td);
	
	// 매장과 동일한 아이디의 테마들 가져올때 사용
	List<Thema> brnList(int branchId);
	
	// 테마명 검색
	String tmTitle(int themaId);

	// 매장명 검색
	String brName(int branchId);
	
	 List<LocalTime> timetableList(@Param("themaId") int themaId);
	
	//==================테마소개=====================//
    List<Thema> selectList(); 
    
    Thema detail(int thema_id);   
    Thema selectById(int thema_id);
    void insertThema(Thema thema); 
    void updateThema(Thema thema);
    void deleteThema(int thema_id);
    
    int listCnt();
    int maxId();
    void addCount(int thema_id);
    int fileDelete(Thema thema);
    int idPwChk(Thema thema);
    
    //===================정형부분=======================
    ArrayList<String> chkrvstatus(LocalDate date );
  //예약번호 중복체크
  	int chkrvnum(String rvnum);
  //예약 하기
  	int reserve(Reservation rvDTO);
    
    
}
