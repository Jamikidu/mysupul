package supul.controll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Thema;
import supul.repository.PayRepository;
import supul.repository.ThemaRepository;
import supul.service.SaleService;
import supul.service.ThemaMapper;

@Controller
@RequestMapping("/thema")
public class ThemaController {
	 
	//테마관리
	@Autowired
	@Qualifier("themaMapper")
	ThemaMapper themaMapper;
	@Autowired
	ThemaRepository themaRepository;
	@Autowired
	private SaleService stat;
	
	PageData pageData ;
	
	//===================유진==========================//
	
    // 테마 소개 페이지
    @RequestMapping("introduce")
    public String introduceForm(Model model,@RequestParam(name="branchId",defaultValue = "1") int branchId , Branch bd,@RequestParam(name = "sortBy",defaultValue = "themaId" ) String sortBy,
			@RequestParam(value = "date", required = false) LocalDate date, @PageableDefault(size = 8,sort = "date" , 
			direction = Direction.DESC ) Pageable pageable, Reservation rv
			) {
    	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	            Sort.by(Sort.Direction.ASC, sortBy)); 
    	// 테마 데이터, 매장 데이터
    	List<Thema> themaList = themaRepository.findAll();
        List<Branch> brnList = themaMapper.brList(bd);
       
        
        model.addAttribute("rv",rv);
        model.addAttribute("date",date);
        model.addAttribute("themaList", themaList);
        model.addAttribute("brnList", brnList);
      
        model.addAttribute("reserveExist",themaMapper.chkrvstatus(date));
        
 
        
        
        return "thema/introduceForm";
    }
    
    //===================정현부분=========================//
    
    @RequestMapping("/reservation/OK")
	String reservation_OK(Model mm,
			@RequestParam(value="picktime")LocalTime picktime,
			Thema thema,@RequestParam(value = "date", required = false) LocalDate date,
			Reservation rv) {
		System.out.println("reservation_OK() 진입");
		
		LocalTime rvtime = picktime;
 
		System.out.println("theme 초기값 => "+thema);
		
		//예약번호 생성을 위한 포맷작업 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datenum = (String)LocalDate.now().format(formatter);
		
		System.out.println("datenum:" +datenum);
 
		Random ran = new Random();
		// 중복 없으면 예약번호 생성 아니면 재생성
		while(true) {
			int i = ran.nextInt(1, 10000);
			
			//String rvnum = datenum+Integer.toString(i);
			String rvnum = datenum+String.format("%04d", i); //int i 5여도 0005 로 나오게 변환
			//System.out.println("중복처리 전 rvnum: "+rvnum);
			int cnt = themaMapper.chkrvnum(rvnum);
			if(cnt==0) {
				//System.out.println("중복처리 후 rvnum: "+rvnum);
				rv.setRvNum(rvnum);
				break;
			}
		}
		
		rv.setDate(date);
		rv.setTime(rvtime);
		rv.setRvDate(LocalDateTime.now());
		rv.setRvPeople(thema.getPeople());
		
		/*
		 * if(rvtime.equals(thema.getTimetable()[0]) ||
		 * rvtime.equals(thema.getTimetable()[thema.getTimetable().length-1])) {
		 * rv.setPrice((int)(thema.getPrice()*0.8/1000*1000)); }else {
		 * rv.setPrice(thema.getPrice()); }
		 */
		
		rv.setThemaName(thema.getTitle());
		
//		System.out.println("rv: "+rv);
//		System.out.println(picktime);
//		System.out.println(theme.getDate());
		themaMapper.reserve(rv);
		String Msg = date+" "+picktime+" 시간 예약 완료!";
		mm.addAttribute("msg", Msg);
		mm.addAttribute("goUrl", "/thema/introduce");
		
		return "rv/alert";
	}
    
     
    
    

    //==================기은 부분===================//
    
    @GetMapping("/themaList")
    public String listThemas(Model model) {
        List<Thema> themas = themaMapper.selectList();
        model.addAttribute("themas", themas);
        return "thema/themaList";
    }
    
	@RequestMapping("/detail/{id}")
	String detail(Model model, @PathVariable int id, PageData pageData) {
		
		Thema thema = themaMapper.detail(id);
		model.addAttribute("thema", thema);
		model.addAttribute("pageData", pageData);
		return "thema/themaDetail";
	}

    @GetMapping("/form")
    public String showThemaForm(Model model) {
        model.addAttribute("thema", new Thema());
        return "thema/themaReg";
    }

    @PostMapping("/add")
    public String submitThema(@ModelAttribute Thema thema, HttpServletRequest request) {
    	 
    	fileSave(thema,request);
    	themaMapper.insertThema(thema);
        return "redirect:/thema/themaList";
    }
    
    // 지점 정보 수정 폼 보기
    @GetMapping("/update/{themaId}")
    public String showThemaUpdateForm(@PathVariable("themaId") int themaId, Model model) {
    	//Thema thema = themaMapper.selectById(themaId);
       Thema thema = themaMapper.detail(themaId);
    	model.addAttribute("thema", thema);
        return "thema/themaUpdate";
    }

    // 지점 정보 수정 처리
    @PostMapping("/update")
    public String submitThemaUpdate(@ModelAttribute Thema thema, HttpServletRequest request,PageData pageData) {
    	System.out.println("테마야"+ thema);
    	themaMapper.updateThema(thema);
    	
    	int cnt = themaMapper.idPwChk(thema);
    	System.out.println("cnt : "+cnt);
    	
    	if(cnt>0) {
			if(thema.getPoster()==null) {
				//새파일을 업로드하는 함수
				fileSave(thema,request);
			}
			//수정한 내용 db에 저장
			themaMapper.updateThema(thema);
			
			pageData.setMsg("수정되었습니다.");
		}
    	
        return "redirect:/thema/themaList";
    }

    // 지점 정보 삭제 폼 보기
    @GetMapping("/delete/{themaId}")
    public String showThemaDeleteForm(@PathVariable("themaId") int themaId, Model model) {
    	Thema thema = themaMapper.selectById(themaId);
        model.addAttribute("thema", thema);
        return "thema/themaDelete";
    }

    //테마 정보 삭제 처리
    @PostMapping("/delete")
    public String submitThemaDelete(@ModelAttribute Thema thema, HttpServletRequest request) {
    	Thema delThema = themaMapper.detail(thema.getThemaId());
		
		int cnt = themaMapper.fileDelete(thema);
		System.out.println("modifyReg:"+cnt);
		if(cnt>0) {
			fileDeleteModule(delThema, request);
		}
 	
    	return "redirect:/thema/themaList";
    }
    
	@PostMapping("fileDelete")
	//게시판 글에 첨부된 파일을 삭제하는 동작을 처리
	String fileDelete(Thema thema,  HttpServletRequest request, PageData pageData, Model model) {
		
		Thema delThema = themaMapper.detail(thema.getThemaId());
		pageData.setMsg("파일 삭제실패");
		//실패시 갈 경로 설정
		pageData.setGoUrl("/thema/update/"+thema.getThemaId());
		int cnt = themaMapper.fileDelete(thema);
		System.out.println("modifyReg:"+cnt);
		if(cnt>0) {
			fileDeleteModule(delThema, request);
			pageData.setMsg("파일 삭제되었습니다.");
		}
		model.addAttribute("pageData", pageData);
		
		return "thema/alert";
	}
    
    //파일 관리
	void fileSave(Thema thema, HttpServletRequest request) {
		
		//파일 업로드 유무 확인
		if(thema.getMmff().isEmpty()) {
			
			return;
		}
		
		String path = request.getServletContext().getRealPath("up");
		path = "D:\\Supul\\supul\\src\\main\\webapp\\up\\thema";
		
		
		int dot = thema.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = thema.getMmff().getOriginalFilename().substring(0, dot);
		String ext = thema.getMmff().getOriginalFilename().substring(dot);
		
		
		
		thema.setPoster(fDomain+ext); 
		File ff = new File(path+"\\"+thema.getPoster());
		int cnt = 1;
		while(ff.exists()) {
			 
			thema.setPoster(fDomain+"_"+cnt+ext);
			ff = new File(path+"\\"+thema.getPoster());
			cnt++;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(ff);
			
			fos.write(thema.getMmff().getBytes());
			
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@GetMapping("download/{ff}")
	void download(@PathVariable String ff, 
			HttpServletRequest request,
			HttpServletResponse response) {
		
		String path = request.getServletContext().getRealPath("up");
		path = "C:\\green_works\\spring_work\\supul\\src\\main\\webapp\\up";
		
		
		try {
			FileInputStream fis = new FileInputStream(path+"\\"+ff);
			String encFName = URLEncoder.encode(ff,"utf-8");
			System.out.println(ff+"->"+encFName);
			response.setHeader("Content-Disposition", "attachment;filename="+encFName);
			
			ServletOutputStream sos = response.getOutputStream();
			
			byte [] buf = new byte[1024];
			
			//int cnt = 0;
			while(fis.available()>0) { //읽을 내용이 남아 있다면
				int len = fis.read(buf);  //읽어서 -> buf 에 넣음
											//len : 넣은 byte 길이
				
				sos.write(buf, 0, len); //보낸다 :  buf의 0부터 len 만큼
				
				//cnt ++;
				//System.out.println(cnt+":"+len);
			}
			
			sos.close();
			fis.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void fileDeleteModule(Thema delThema, HttpServletRequest request) {
		if(delThema.getPoster()!=null) {
			String path = request.getServletContext().getRealPath("up");
			path = "D:\\Supul\\supul\\src\\main\\webapp\\up\\thema";
			
			new File(path+"\\"+delThema.getPoster()).delete();
		}
	}

}
