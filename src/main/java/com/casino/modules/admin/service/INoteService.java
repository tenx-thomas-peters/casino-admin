package com.casino.modules.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;

public interface INoteService extends IService<Note> {

	long totalCount(Note note);

	List<Note> noteList(Integer pageNo, Integer pageSize, Note note);
	
	IPage<Note> getNoteList(Page<Note> page, NoteListForm form);

	boolean getNoteContentBySeq(String seq);
	
	IPage<Note> getNoticeList(Page<Note> page, Note note);
	
	boolean changeReadStatusAll();
	
	boolean removeAll();

}
