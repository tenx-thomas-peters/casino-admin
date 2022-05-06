package com.casino.modules.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;
import com.casino.modules.admin.mapper.BoardMapper;
import com.casino.modules.admin.service.IBoardService;

@Service
public class BoardServiceImpl extends ServiceImpl<BoardMapper, Note> implements IBoardService {
	
	@Autowired
	private BoardMapper boardMapper;

	@Override
	public IPage<Note> getNotePageList(Page<Note> page, NoteListForm form) {
		return boardMapper.getNotePageList(page, form, 
				CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2, CommonConstant.SELECT_TYPE_3, CommonConstant.CLASSIFICATION_M_POST);
	}

	@Override
	public IPage<Note> getQuestionPageList(Page<Note> page, NoteListForm form) {
		return boardMapper.getQuestionPageList(page, form, CommonConstant.CLASSIFICATION_CUSTOMER,
					CommonConstant.SELECT_TYPE_0, CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2);
	}

	@Override
	public List<String> getDomainList() {
		return baseMapper.getDomainList();
	}

	@Override
	@Transactional(readOnly = false)
	public boolean hits(String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		Integer lookup = 0;
		for (String id : idList) {
			Note note = getById(id);
			lookup = note.getLookUp()==null?0:note.getLookUp() + 1;
			note.setLookUp(lookup);
			boardMapper.updateById(note);
		}
		return true;
	}

	@Override
	public List<Note> getReceiverList() {
		return boardMapper.getReceiverList();
	}

	@Override
	public List<Note> getCommentList() {
		return boardMapper.getCommentList();
	}

	@Override
	public Note getCommentById(String seq) {
		return boardMapper.getCommentById(seq);
	}

	@Override
	public Note getQuestionBySeq(String seq) {
		return boardMapper.getQuestionBySeq(seq);
	}

	@Override
	public boolean changeAdminReadStatusAll(Integer type, Integer classification, Integer userType) {
		return boardMapper.changeAdminReadStatusAll(type, classification, userType);
	}
}
