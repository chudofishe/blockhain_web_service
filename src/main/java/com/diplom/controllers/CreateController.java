package com.diplom.controllers;

import com.diplom.dto.AssetForm;
import com.diplom.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CreateController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView();
        AssetForm assetForm = new AssetForm();
        modelAndView.addObject("order", assetForm);
        modelAndView.setViewName("create");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createTx(AssetForm assetForm) {
        ModelAndView modelAndView = new ModelAndView();
        transactionService.sendAsset(assetForm, true, "");
        modelAndView.addObject("order", assetForm);
        modelAndView.setViewName("create");
        return modelAndView;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView find() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView find(String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search");
        return modelAndView;
    }
}
