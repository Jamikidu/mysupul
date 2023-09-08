package supul.service;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import supul.model.HomepageDTO;

@Mapper
public interface HomepageMapper {

	List<HomepageDTO> selectHomepageIntro();	
	HomepageDTO selectHomepageById(int id);
    void updateHomepage(HomepageDTO homepage);
    
    
    

}
