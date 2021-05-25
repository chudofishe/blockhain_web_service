package com.diplom.controllers;

import com.diplom.dto.AssetForm;
import com.diplom.services.EncryptionService;
import com.diplom.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TransferController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ModelAndView transferTx(AssetForm assetForm, String id) {
        ModelAndView modelAndView = new ModelAndView();
        transactionService.sendAsset(assetForm, false, id);
        modelAndView.addObject("order", id);
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }
}
