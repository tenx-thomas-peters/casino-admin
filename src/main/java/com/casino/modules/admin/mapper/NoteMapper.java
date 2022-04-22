package com.casino.modules.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;

public interface NoteMapper extends BaseMapper<Note> {

	long totalCount(@Param("note") Note note);

	List<Note> noteList(@Param("pageNo") Integer pageNo, 
			@Param("pageSize") Integer pageSize, 
			@Param("note") Note note);

	IPage<Note> getSendList(Page<Note> page, @Param("form") NoteListForm form,
			@Param("selectType1") Integer selectType1,
			@Param("selectType2") Integer selectType2,
			@Param("selectType3") Integer selectType3);

	IPage<Note> getInboxList(Page<Note> page,
							@Param("selectType1") Integer selectType1,
							@Param("selectType2") Integer selectType2,
							@Param("selectType3") Integer selectType3);

	boolean getNoteContentBySeq(@Param("seq") String seq);
	
	IPage<Note> getNoticeList(Page<Note> page, @Param("note") Note note);
	
	boolean changeReadStatusAll();
	
	boolean removeAll();
}
