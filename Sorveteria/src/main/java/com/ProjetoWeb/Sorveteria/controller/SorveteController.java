package com.ProjetoWeb.Sorveteria.controller;

import com.ProjetoWeb.Sorveteria.model.Sorvete;
import com.ProjetoWeb.Sorveteria.service.SorveteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class SorveteController {

    @Autowired
    private SorveteService sorveteService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("sorvetes", sorveteService.listarAtivos());
        return "admin";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("sorvete", new Sorvete());
        return "cadastro";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam("id") Long id, Model model) {
        Sorvete sorvete = sorveteService.findById(id).orElse(null);
        if (sorvete == null) {
            return "redirect:/admin";
        }
        model.addAttribute("sorvete", sorvete);
        return "editar";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("sorvete") Sorvete sorvete, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cadastro";
        }

        if (sorvete.getId() == null) {
            sorvete.setIsDeleted(null);
            sorveteService.create(sorvete);
            redirectAttributes.addFlashAttribute("message", "Sorvete cadastrado com sucesso!");
        } else {
            Sorvete existingSorvete = sorveteService.findById(sorvete.getId()).orElse(null);
            if (existingSorvete != null) {
                sorvete.setIsDeleted(existingSorvete.getIsDeleted());
                sorveteService.update(sorvete);
                redirectAttributes.addFlashAttribute("message", "Sorvete atualizado com sucesso!");
            }
        }

        return "redirect:/admin";
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        sorveteService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Sorvete deletado com sucesso!");
        return "redirect:/admin";
    }
}
