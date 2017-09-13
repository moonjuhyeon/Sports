package com.sports.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sports.dto.UserDTO;
import com.sports.service.IUserService;
import com.sports.util.CmmUtil;
import com.sports.util.MailUtil;

@Controller
public class UserController {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="UserService")
	private IUserService userService;
	
	@RequestMapping(value="main")
	public String main() throws Exception{
		log.info(this.getClass() + "mainPage Start!!");
		
		log.info(this.getClass() + "mainPage End!!");
		return "jsp/main";
	}
	@RequestMapping(value="login")
	public String login() throws Exception{
		log.info(this.getClass() + " loginPage Start!!");
		
		
		log.info(this.getClass() + " loginPage End!!");
		return "login";
	}
	@RequestMapping(value="loginProc")
	public String loginProc(HttpServletRequest req, HttpSession session, Model model)throws Exception{
		log.info(this.getClass() + " loginProc Start!!");
		String id = CmmUtil.nvl(req.getParameter("id"));
		String password = CmmUtil.nvl(req.getParameter("password"));
		
		log.info("id : " + id);
		UserDTO uDTO = new UserDTO();
		uDTO.setUser_id(id);
		uDTO.setPassword(password);
		uDTO = userService.getLoginInfo(uDTO);
		
		if(uDTO ==null){
			String msg = "아이디, 비밀번호를 확인해주세요.";
			String url = "login.do";
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
			return "alert/alert";
		}else{
			session.setAttribute("ss_user_no", uDTO.getUser_no());
			session.setAttribute("ss_user_id", uDTO.getUser_id());
			session.setAttribute("ss_user_name", uDTO.getUser_name());
			uDTO = null;
			log.info(this.getClass() + " loginProc End!!");
			return "redirect:main.do";
		}
	}
	@RequestMapping(value="logout")
	public String logout(HttpSession session, Model model) throws Exception{
		log.info(this.getClass() + " logout Start!!");
		
		log.info("id : "+session.getAttribute("ss_user_id"));
		
		session.setAttribute("ss_user_no", "");
		session.setAttribute("ss_user_name", "");
		session.setAttribute("ss_user_id", "");
		session.invalidate();
		model.addAttribute("msg", "로그아웃되었습니다");
		model.addAttribute("url", "main.do");
		log.info(this.getClass() + " logout End!!");
		return "alert/alert";
	}
	
	@RequestMapping(value="userReg")
	public String userReg(HttpSession session) throws Exception{
		log.info(this.getClass() + "userReg Start!!");
		
		log.info(this.getClass() + "userReg End!!");
		return "userReg";
	}
	
	@RequestMapping(value="idCheck")
	public void idCheck(@RequestParam(value="id") String id, HttpServletResponse resp) throws Exception{
		log.info(this.getClass() + " idCheck Start!!");
		
		log.info("id : "+id);
		UserDTO uDTO = new UserDTO();
		uDTO.setUser_id(id);
		int check = userService.getIdCheck(uDTO);
		
		
		resp.getWriter().print(check);
		resp.getWriter().flush();
		resp.getWriter().close();
		id = null;
		uDTO = null;
		
		log.info(this.getClass() + " idCheck End!!");
	}
	
	@RequestMapping(value="userRegProc")
	public String userRegProc(HttpServletRequest req, HttpSession session, Model model) throws Exception{
		log.info(this.getClass() + "userRegProc Start!!");
		
		
		String userId = CmmUtil.nvl(req.getParameter("id"));
		String password = CmmUtil.nvl(req.getParameter("password"));
		String userName = CmmUtil.nvl(req.getParameter("userName"));
		String email = CmmUtil.nvl(req.getParameter("email"));
		String tel = CmmUtil.nvl(req.getParameter("tel"));
		String postcode = CmmUtil.nvl(req.getParameter("postcode"));
		String address1 = CmmUtil.nvl(req.getParameter("address1"));
		String address2 = CmmUtil.nvl(req.getParameter("address2"));
		
		log.info("userId : "+userId);
		log.info("password : "+password);
		log.info("userName : "+userName);
		log.info("email : "+email);
		log.info("tel : "+tel);
		log.info("postcode : "+postcode);
		log.info("address1 : "+address1);
		log.info("address2 : "+address2);
		
		UserDTO uDTO = new UserDTO();
		
		uDTO.setUser_name(userName);
		uDTO.setUser_id(userId);
		uDTO.setPassword(password);
		uDTO.setTel(tel);
		uDTO.setEmail(email);
		uDTO.setPostcode(postcode);
		uDTO.setAddress1(address1);
		uDTO.setAddress2(address2);
		
		userService.insertUserInfo(uDTO);
		
		uDTO = null;
		
		log.info(this.getClass() + "userRegProc End!!");
		return "redirect:main.do";
	}
	
	@RequestMapping(value="idPwSearch")
	public String idPwSearch() throws Exception{
		log.info(this.getClass() + " idPwSearch Start!!");
		
		
		
		log.info(this.getClass() + " idPwSearch End!!");
		return "idPwSearch";
	}
	
	@RequestMapping(value="certify")
	public void certify(@RequestParam("email") String email, @RequestParam("name") String name, HttpServletResponse resp) throws Exception{
		log.info(this.getClass() + " certifyEmail Start!!");
		
		log.info("email : " +email);
		log.info("name : " +name);
		
		UserDTO uDTO = new UserDTO();
		uDTO.setEmail(email);
		uDTO.setUser_name(name);
		userService.updateEmailCode(uDTO);
		
		MailUtil.sendMail(email, "모두의 스포츠 인증번호입니다.","인증번호는 " +uDTO.getEmail_code()+"입니다.");
		resp.getWriter().print("success");
		resp.getWriter().flush();
		resp.getWriter().close();
		
		uDTO = null;
		log.info(this.getClass() + " certifyEmail End!!");
	}
	@RequestMapping(value="idSearch")
	public String idSearch(HttpServletRequest req, Model model) throws Exception{
		log.info(this.getClass() + " idSearch Start!!");
		
		String name = CmmUtil.nvl(req.getParameter("name"));
		String email = CmmUtil.nvl(req.getParameter("email"));
		String emailCode = CmmUtil.nvl(req.getParameter("code"));
		
		log.info("name : "+name);
		log.info("email : "+email);
		log.info("emailCode : "+emailCode);
		UserDTO uDTO = new UserDTO();
		uDTO.setUser_name(name);
		uDTO.setEmail(email);;
		uDTO.setEmail_code(emailCode);
		
		uDTO = userService.getUserId(uDTO);
		
		if(uDTO==null){
			String msg = "회원정보가 일피하지 않습니다.";
			String url = "idPwSearch.do";
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
			log.info(this.getClass() + " idSearch End!!");
			return "alert/alert";
		}else{
			model.addAttribute("uDTO", uDTO);
			uDTO = null;
			log.info(this.getClass() + " idSearch End!!");
			return "idSearch";
		}
	}
	
	@RequestMapping(value="pwSearch")
	public String pwSearch(HttpServletRequest req, Model model) throws Exception{
		log.info(this.getClass() + " pwSearch Start!!");
		
		String userId = CmmUtil.nvl(req.getParameter("id"));
		String userName = CmmUtil.nvl(req.getParameter("name"));
		String email = CmmUtil.nvl(req.getParameter("email"));
		String emailCode = CmmUtil.nvl(req.getParameter("code"));
		
		log.info(this.getClass() + " userId : "+ userId);
		log.info(this.getClass() + " userName : "+ userName);
		log.info(this.getClass() + " email : "+ email);

		UserDTO uDTO = new UserDTO();
		uDTO.setUser_id(userId);
		uDTO.setUser_name(userName);
		uDTO.setEmail(email);
		uDTO.setEmail_code(emailCode);
		
		uDTO = userService.getUserId(uDTO);
		
		if(uDTO==null){
			String msg = "회원정보가 일치하지 않습니다.";
			String url = "idPwSearch.do";
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
			log.info(this.getClass() + " idSearch End!!");
			return "alert/alert";
		}else{
			model.addAttribute("uDTO", uDTO);
			uDTO = null;
			log.info(this.getClass() + " idSearch End!!");
			return "pwSearch";
		}
	}
	
	@RequestMapping(value="pwSearchProc")
	String pwSearchProc(HttpServletRequest req, Model model) throws Exception{
		String userNo = CmmUtil.nvl(req.getParameter("uNo"));
		String password = CmmUtil.nvl(req.getParameter("password"));
		
		log.info(this.getClass() + " userNo : "+userNo);
		
		UserDTO uDTO = new UserDTO();
		
		uDTO.setUser_no(userNo);
		uDTO.setPassword(password);
		
		userService.updatePassword(uDTO);
		
		model.addAttribute("msg", "변경이 완료되었습니다.");
		model.addAttribute("url", "main.do");
		
		return "alert/alert";
	}
}
