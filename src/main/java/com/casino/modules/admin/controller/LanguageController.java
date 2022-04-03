package com.casino.modules.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class LanguageController {

	@RequestMapping(value = {"/setLocale"})
    public ModelAndView setLocale(
            @RequestParam(value = "lang") String langName,
            ModelAndView mav,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Locale locale = Locale.forLanguageTag(langName);
        
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpServletRequest);
        localeResolver.setLocale(httpServletRequest, httpServletResponse, locale);
		String URL = httpServletRequest.getHeader("referer");
    	mav.setViewName("redirect:" + URL);
		return mav;
    }
}
