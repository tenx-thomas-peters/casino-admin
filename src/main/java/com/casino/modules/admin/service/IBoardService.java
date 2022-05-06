package com.casino.modules.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;

public interface IBoardService extends IService<Note> {

	IPage<Note> getNotePageList(Page<Note> page, NoteListForm form);

	IPage<Note> getQuestionPageList(Page<Note> page, NoteListForm form);

	List<String> getDomainList();

	boolean hits(String ids);

	List<Note> getReceiverList();

	List<Note> getCommentList();

	Note getCommentById(String seq);

	Note getQuestionBySeq(String seq);

	boolean changeAdminReadStatusAll(Integer type, Integer classification, Integer userType);
}
