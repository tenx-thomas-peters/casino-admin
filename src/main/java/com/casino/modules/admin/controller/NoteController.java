package com.casino.modules.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.casino.modules.admin.service.IBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Level;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.NoteListForm;
import com.casino.modules.admin.service.ILevelService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.INoteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/memo")
@Slf4j
public class NoteController {
    @Autowired
    private INoteService noteService;
    
    @Autowired
    private ILevelService levelService;
    
    @Autowired
    private IMemberService memberService;

	@Autowired
	private IBoardService boardService;

    @GetMapping(value = "/sendlist")
    public String noteList(@ModelAttribute("form") NoteListForm form,
    		@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
			Model model) {
        try {
        	Page<Note> page = new Page<Note>(pageNo, pageSize);
        	form.setType(CommonConstant.TYPE_NOTE);
        	form.setSendType(CommonConstant.TYPE_SEND_NOTE);
        	IPage<Note> pageList = noteService.getNoteList(page, form);
			
			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
            model.addAttribute("form", form);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("url", "memo/sendlist");
        } catch (Exception e) {
            log.error("url: /memo/sendlist --- method: sendlist --- error: " + e.toString());
        }
        return "views/admin/note/list";
    }
    
    @GetMapping(value = "/sendPList")
    public String pNoteList(@ModelAttribute("form") NoteListForm form,
    		@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
			Model model) {
        try {
        	Page<Note> page = new Page<Note>(pageNo, pageSize);
        	form.setType(CommonConstant.TYPE_P_NOTE);
        	form.setSendType(CommonConstant.TYPE_SEND_NOTE);
        	IPage<Note> pageList = noteService.getNoteList(page, form);
			
			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
            model.addAttribute("form", form);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("url", "memo/sendPList");
        } catch (Exception e) {
            log.error("url: /memo/sendPList --- method: sendPList --- error: " + e.toString());
        }
        return "views/admin/partner/pNoteList";
    }

	@GetMapping(value = "/pInboxList")
	public String pInboxList(@ModelAttribute("form") NoteListForm form,
							@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
							@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
							Model model) {
		try {
			Page<Note> page = new Page<Note>(pageNo, pageSize);
			form.setType(CommonConstant.TYPE_P_NOTE);
			form.setSendType(CommonConstant.TYPE_RECEIVE_NOTE);
			IPage<Note> pageList = noteService.getNoteList(page, form);

			boardService.changeAdminReadStatusAll(CommonConstant.TYPE_P_NOTE, 0);

			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
			model.addAttribute("form", form);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("url", "memo/pInboxList");
		} catch (Exception e) {
			log.error("url: /memo/pInboxList --- method: sendPList --- error: " + e.toString());
		}
		return "views/admin/partner/pInboxList";
	}
    
    @PostMapping(value = "/batchDelete")
    @ResponseBody
	public Result<Note> delete(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
		Result<Note> result = new Result<>();
		List<String> idList = Arrays.asList(ids.split(","));
		try {
			if (noteService.removeByIds(idList)) {
				result.success("operation success");
			} else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /memo/delete --- method: delete--- " + e.toString());
			result.error500("please confirm");
		}
		return result;
	}
    
    @GetMapping(value = "/popup_adminwrite")
    public String popupAdminwrite(Model model) {
    	try {
    		QueryWrapper<Level> qw = new QueryWrapper<>();
            qw.eq("correction", CommonConstant.CORRECTION_APPLY);//0: not apply, 1: apply
            List<Level> levelList = levelService.list(qw);
            
            List<Map<String, String>> domainList = memberService.getSiteList();

			List<Member> storeList = this.getPartnerList(1);
            model.addAttribute("levelList", levelList);
            model.addAttribute("domainList", domainList);
            model.addAttribute("storeList", storeList);
            model.addAttribute("url", "memo/popup_adminwrite");
    		
    	} catch (Exception e) {
    		log.error("url: /memo/popup_adminwrite --- method: popupAdminwrite --- error: " + e.toString());
    	}
    	return "views/admin/note/writeAnote";
    }

	public List<Member> getPartnerList(Integer userType){
		QueryWrapper<Member> qw = new QueryWrapper<>();
		qw.eq("user_type", userType);
		return memberService.list(qw);
	}
    
    @PostMapping(value = "getRecipient")
	@ResponseBody
    public Result<Member> getRecipient(HttpServletRequest request) {
    	Result<Member> result = new Result<Member>();
    	try {
    		String levelSeq = request.getParameter("levelSeq");
    		String userType = request.getParameter("userType");
    		String siteSeq = request.getParameter("siteSeq");
    		
    		Member recipient = memberService.getRecipient(levelSeq, userType, siteSeq);
    		result.setResult(recipient);
    		result.success("success!");
    		
    	} catch (Exception e) {
    		log.error("url: /memo/getRecipient --- method: popupAdminwrite --- error: " + e.toString());
    	}
    	return result;
    }

	/**
	 * page: /memo/popup_adminwrite
	 * noteForm submit to send note to members
	 * @param note
	 * @param model
	 * @return
	 */
    @PostMapping(value = "/send")
    @ResponseBody
    public Result<Note> send(@ModelAttribute("note") Note note, Model model) {
		System.out.println("NoteController==send==");
		System.out.println("API: /memo/send ======================");

    	Result<Note> result = new Result<Note>();
    	try {
    		if(note != null) {
				String storeSeq = note.getStoreSeq();
				String levelSeq = note.getLevelSeq();
				QueryWrapper<Member> qw = new QueryWrapper<>();
				qw.eq("store_seq", storeSeq);
				qw.eq("level_seq", levelSeq);
				List<Member> memberListByLevelAndStore = memberService.list(qw);
				List<Note> noteSaveList = new ArrayList<>();

				if(memberListByLevelAndStore.size()!=0){
					for(Member member : memberListByLevelAndStore){

						System.out.println("*************** note info to send for member ***************");

						System.out.println("\t*** seq : "+UUIDGenerator.generate());
						System.out.println("\t*** Sender : "+note.getSender());
						System.out.println("\t*** Receiver : "+member.getSeq());
						System.out.println("\t*** ReadStatus : "+CommonConstant.STATUS_UN_READ);
						System.out.println("\t*** RecommendStatus : "+CommonConstant.STATUS_UN_RECOMMEND);
						System.out.println("\t*** LookUp : "+0);
						System.out.println("\t*** Type : "+CommonConstant.TYPE_NOTE);
						System.out.println("\t*** SendType : "+CommonConstant.TYPE_SEND_NOTE);
						System.out.println("\t*** Site : "+note.getSite());
						System.out.println("\t*** Title : "+note.getTitle());
						System.out.println("\t*** Content : "+note.getContent());
						System.out.println("*******************************************");


						Note noteSave = new Note();
						noteSave.setSeq(UUIDGenerator.generate());
						noteSave.setSender(note.getSender());
						noteSave.setReceiver(member.getSeq());
						noteSave.setReadStatus(CommonConstant.STATUS_UN_READ);
						noteSave.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
						noteSave.setLookUp(0);
						noteSave.setType(CommonConstant.TYPE_NOTE);
						noteSave.setSendType(CommonConstant.TYPE_SEND_NOTE);
						noteSave.setContent(note.getContent());
						noteSave.setSite(note.getSite());
						noteSave.setTitle(note.getTitle());

						noteSaveList.add(noteSave);
					}
					if(noteService.saveBatch(noteSaveList)) {
						result.success("Operate Success");
					} else {
						result.error500("Operate Faild");
					}
				}
				else
					result.error500("보내려는 회원이 없습니다.\n <받는이>을 확인해주세요");

    		} else {
    			result.error500("Operate Faild");
    		}
    	} catch (Exception e) {
    		log.error("url: /memo/send --- method: send --- error: " + e.toString());
    	}
    	return result;
    }
    
    @GetMapping(value = "/getMemberList")
    @ResponseBody
    public List<Member> getMembersByUserTypeAndLevel(@ModelAttribute("member") Member member, HttpServletRequest request) {
    	List<Member> memberList = new ArrayList<>();
    	try {
    		QueryWrapper<Member> qw = new QueryWrapper<>();
    		qw.eq("user_type", member.getUserType());
//    		qw.eq("level_seq", member.getLevelSeq());
    		memberList = memberService.list(qw);
    	} catch (Exception e) {
    		log.error("url: /member/getMemberList --- method: getMembersByUserTypeAndLevel --- message: " + e.toString());
    	}
    	return memberList;
    }

	@GetMapping(value = "/popup_memo")
	public String popupMemo(@ModelAttribute("form") NoteListForm form,
						   @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
						   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						   Model model) {
		try {
			Page<Note> page = new Page<Note>(pageNo, pageSize);
			form.setType(CommonConstant.TYPE_NOTE);
			form.setSendType(CommonConstant.TYPE_SEND_NOTE);
			IPage<Note> pageList = noteService.getNoteList(page, form);

			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
			model.addAttribute("form", form);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("url", "memo/popup_memo");
		} catch (Exception e) {
			log.error("url: /memo/popup_memo --- method: popupMemo --- error: " + e.toString());
		}
		return "views/admin/member/popupSentInbox";
	}
    
	@GetMapping(value = "/getNoteContentBySeq")
	@ResponseBody
	public Result<Map<String, Object>> getNoteContentBySeq(@RequestParam(value="seq") String seq, Model model, HttpServletRequest request) {
		Result<Map<String, Object>> result = new Result<>();
		try {
			QueryWrapper<Note> nqw = new QueryWrapper<>();
			nqw.eq("seq", seq);
            List<Note> noteContentList = noteService.list(nqw);
            
            Map<String, Object> map = new HashMap<>();
            map.put("noteContentList", noteContentList);
            result.success("success");
            result.setResult(map);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
