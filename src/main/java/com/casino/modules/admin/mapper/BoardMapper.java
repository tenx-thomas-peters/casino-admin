package com.casino.modules.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;

import io.lettuce.core.dynamic.annotation.Param;

public interface BoardMapper extends BaseMapper<Note> {

	IPage<Note> getNotePageList(Page<Note> page, @Param("form")NoteListForm form, 
			@Param("selectType1") Integer selectType1,
			@Param("selectType2") Integer selectType2, 
			@Param("selectType3") Integer selectType3, 
			@Param("classificationMPost") Integer classificationMPost);

	IPage<Note> getQuestionPageList(Page<Note> page, NoteListForm form, 
			@Param("classification") Integer classification,
			@Param("selectType0") Integer selectType0,
			@Param("selectType1") Integer selectType1,
			@Param("selectType2") Integer selectType2);

	List<String> getDomainList();

	List<Note> getReceiverList();

	List<Note> getCommentList();

	Note getCommentById(@Param("seq")String seq);

	Note getQuestionBySeq(@Param("seq") String seq);

	boolean changeAdminReadStatusAll(@Param("type") Integer type, @Param("classification") Integer classification, @Param("userType") Integer userType);
}
