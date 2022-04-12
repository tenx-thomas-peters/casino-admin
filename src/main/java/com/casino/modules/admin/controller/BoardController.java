package com.casino.modules.admin.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

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
import com.casino.modules.admin.service.IBoardService;
import com.casino.modules.admin.service.ILevelService;
import com.casino.modules.admin.service.IMemberService;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping(value = "/board")
@Slf4j
public class BoardController {
	
	@Value(value = "${uploadPath}")
	private String uploadPath;

	@Autowired
	private IBoardService boardService;

	@Autowired
	private ILevelService levelService;

	@Autowired
	private IMemberService memberService;

	@GetMapping(value = "/list")
	public String notelList(Model model, @ModelAttribute("form") NoteListForm form,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

		try {
			Page<Note> page = new Page<Note>(pageNo, pageSize);
			IPage<Note> pageList = boardService.getNotePageList(page, form);
			
			model.addAttribute("pageList", pageList);
			
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("form", form);
			model.addAttribute("url", "board/list?province=" + form.getProvince());
		} catch (Exception e) {
			log.error("url: /board/list --- method: list --- error: " + e.toString());
		}
		return "views/admin/board/list";
	}

	@PostMapping(value = "hits")
	@ResponseBody
	public Result<String> hits(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
		Result<String> result = new Result<>();
		try {
			if(boardService.hits(ids)) {
				result.success("success");
			}else {
				result.error500("fail");
			}
		} catch (Exception e) {
			log.error("url: /board/hits --- method: hits --- error: " + e.toString());
			result.error500("fail");
		}
		return result;
	}

	@PostMapping(value = "/delete")
	@ResponseBody
	public Result<Note> delete(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
		Result<Note> result = new Result<>();
		List<String> idList = Arrays.asList(ids.split(","));
		try {
			if (boardService.removeByIds(idList)) {
				result.success("operation success");
			} else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /board/delete --- method: delete--- " + e.toString());
			result.error500("please confirm");
		}
		return result;
	}

	@GetMapping(value = "/write")
	public String write(@RequestParam(name = "seq", defaultValue="") String seq, Model model) {
		try {
			Note note = new Note();
			Member member = new Member();
			Level level = new Level();
			if(StringUtils.isNotEmpty(seq)) {
				note = boardService.getById(seq);
				member = memberService.getById(note.getSender());
				note.setNickname(member.getNickname());
				level = levelService.getById(member.getLevelSeq());
				note.setLevelName(level.getLevelName());
			}
			
			QueryWrapper<Level> qw = new QueryWrapper<>();
			qw.eq("correction", CommonConstant.CORRECTION_APPLY);
			List<Level> levelList = levelService.list(qw);
			
			List<Note> receiverList = boardService.getReceiverList();
			
			List<Note> commentList = boardService.getCommentList();
			
			model.addAttribute("levelList", levelList);
			model.addAttribute("receiverList", receiverList);
			model.addAttribute("commentList", commentList);
			model.addAttribute("levelName", level.getLevelName());
			model.addAttribute("levelSeq", member.getLevelSeq());
			model.addAttribute("note", note);
			model.addAttribute("url", "board/write");

		} catch (Exception e) {
			log.error("url: /board/write --- method: write--- " + e.toString());
		}
		return "views/admin/board/write";
	}

	@PostMapping(value = "/enroll")
	@ResponseBody
	public Result<Note> enroll(@ModelAttribute("note") Note note) {
		Result<Note> result = new Result<Note>();
		try {
			if (note != null) {
				String content = note.getContent();
				content = content.substring(1, content.length());
				note.setContent(content);
				note.setReadStatus(CommonConstant.STATUS_UN_READ);
				note.setType(CommonConstant.TYPE_POST);

				if(StringUtils.isNotBlank(note.getSeq())) {
					if(boardService.updateById(note) ) {
						result.success("Operate Success");
					} else {
						result.error500("Operate Faild");
					}
				} else {
					note.setSeq(UUIDGenerator.generate());
					if (boardService.save(note)) {
						result.success("Operate Success");
					} else {
						result.error500("Operate Faild");
					}
				}
			} else {
				result.error500("Operation Failed");
			}
		} catch (Exception e) {
			log.error("url: /board/enroll --- method: enroll--- " + e.toString());
		}
		return result;
	}

	@GetMapping(value = "/questionlist")
	public String questionlist(Model model, @ModelAttribute("form") NoteListForm form,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

		try {
			Page<Note> page = new Page<Note>(pageNo, pageSize);
			IPage<Note> pageList = boardService.getQuestionPageList(page, form);
			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("form", form);
			model.addAttribute("url", "board/questionlist");
		} catch (Exception e) {
			log.error("url: /board/questionlist --- method: questionlist --- error: " + e.toString());
		}
		return "views/admin/board/questionlist";
	}
	
	@GetMapping(value = "/getQuestionBySeq")
	public String getQuestionBySeq(Model model, @ModelAttribute("seq") String seq) {
		try {
			Note note = boardService.getQuestionBySeq(seq);
			model.addAttribute("note", note);
		} catch (Exception e) {
			log.error("url: /board/getQuestionBySeq --- method: getQuestionBySeq --- error: " + e.toString());
		}
		return "views/admin/board/questionAnswer";
	}
	
	@PostMapping(value="questionSendAnswer")
	@ResponseBody
	public Result<Note> questionSendAnswer(@ModelAttribute("note") Note note){
		Result<Note> result = new Result<Note>();
		try {
			if(note !=null) {
				note.setReadStatus(CommonConstant.STATUS_UN_READ);
				note.setAnswerStatus(CommonConstant.STATUS_ANSWER);
				note.setType(CommonConstant.TYPE_POST);
				if(boardService.updateById(note)) {
					result.success("operation success");
				}else {
					result.error500("operation fail");
				}
			}
		}catch(Exception e) {
			log.error("url: /board/questionSendAnswer --- method: questionSendAnswer --- error: " + e.toString());
			result.error500("operation fail");
		}
		
		return result;
	}

	@GetMapping(value = "/getNickNameList")
	@ResponseBody
	public List<Member> getNickNameList(@RequestParam("levelSeq") String levelSeq) {
		QueryWrapper<Member> qw = new QueryWrapper<>();
		qw.eq("level_seq", levelSeq);
		List<Member> nickNameList = memberService.list(qw);
		return nickNameList;
	}

	@GetMapping(value = "getSiteList")
	@ResponseBody
	public List<Map<String, String>> getSiteList() {
		List<Map<String, String>> siteList = memberService.getSiteList();
		return siteList;
	}
	
	@PostMapping(value="answer")
	@ResponseBody
	public Result<Note> answer(@ModelAttribute("note") Note note){
		Result<Note> result = new Result<Note>();
		try {
			if(note !=null) {
				note.setSeq(UUIDGenerator.generate());
				note.setReadStatus(CommonConstant.STATUS_UN_READ);
				note.setAnswerStatus(CommonConstant.STATUS_ANSWER);
				note.setType(CommonConstant.TYPE_POST);
				if(boardService.save(note)) {
					result.success("operation success");
				}else {
					result.error500("operation fail");
				}
			}
		}catch(Exception e) {
			log.error("url: /board/answer --- method: answer --- error: " + e.toString());
			result.error500("operation fail");
		}
		
		return result;
	}
	
	@PostMapping(value = "uploadFile")
	public Map<String, Object> uploadFile(HttpServletRequest req, ModelAndView mav) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String ctxPath = uploadPath;
		String fileName = null;
		String bizPath = "board";
		try {
			String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
			File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
			if (!file.exists()) {
				file.mkdirs();
			}
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
			MultipartFile mf = multipartRequest.getFile("uploadPath");
			String orgName = mf.getOriginalFilename();
			fileName = orgName.substring(0, orgName.lastIndexOf(".")) + orgName.substring(orgName.indexOf("."));
			String savePath = file.getPath() + File.separator + fileName;

			File savefile = new File(savePath);
			FileCopyUtils.copy(mf.getBytes(), savefile);

			String filePath = bizPath + File.separator + nowday + File.separator + fileName;
			if (filePath.contains("\\")) {
				filePath = filePath.replace("\\", "/");
			}
			resultMap.put("uploadPath", filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@GetMapping(value="/getCommentById")
	public String getCommentById(@RequestParam("seq") String seq, Model model, HttpServletRequest request) {
		try {
			Note note = boardService.getCommentById(seq);
			model.addAttribute("note", note);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "views/admin/board/writeComment";
	}
	
	@PostMapping(value="updateCommentById")
	@ResponseBody
	public Result<Note> updateCommentById(@ModelAttribute("note") Note note, HttpServletRequest request){
		Result<Note> result = new Result<Note>();
		try {
			if(boardService.updateById(note)) {
				result.success("operation success");
			}else {
				result.error500("operation failed");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
