package com.ProjetoWeb.Sorveteria.controller;

import com.ProjetoWeb.Sorveteria.model.Sorvete;
import com.ProjetoWeb.Sorveteria.service.SorveteService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private SorveteService sorveteService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        List<Sorvete> sorvetes = sorveteService.listarAtivos();
        model.addAttribute("sorvetes", sorvetes);

        @SuppressWarnings("unchecked")
        List<Sorvete> carrinho = (List<Sorvete>) session.getAttribute("carrinho");
        int quantidadeCarrinho = (carrinho != null) ? carrinho.size() : 0;
        model.addAttribute("quantidadeCarrinho", quantidadeCarrinho);

     
        Cookie cookie = new Cookie("visita", LocalDateTime.now().toString());
        cookie.setMaxAge(24 * 60 * 60); 
        model.addAttribute("cookieVisit", cookie);

        return "index";
    }

    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam("id") Long id, HttpSession session) {
        Sorvete sorvete = sorveteService.findById(id).orElse(null);
        if (sorvete != null) {
            @SuppressWarnings("unchecked")
            List<Sorvete> carrinho = (List<Sorvete>) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new ArrayList<>();
                session.setAttribute("carrinho", carrinho);
            }
            carrinho.add(sorvete);
        }
        return "redirect:/index";
    }

    @GetMapping("/verCarrinho")
    public String verCarrinho(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Sorvete> carrinho = (List<Sorvete>) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.isEmpty()) {
            model.addAttribute("message", "Não existem itens no carrinho.");
            return "redirect:/index";
        }
        model.addAttribute("carrinho", carrinho);
        return "carrinho";
    }

    @GetMapping("/finalizarCompra")
public String finalizarCompra(HttpSession session, RedirectAttributes redirectAttributes) {
    @SuppressWarnings("unchecked")
    List<Sorvete> carrinho = (List<Sorvete>) session.getAttribute("carrinho");
    if (carrinho == null || carrinho.isEmpty()) {
        redirectAttributes.addFlashAttribute("message", "Seu carrinho está vazio.");
        return "redirect:/index";
    }

    for (Sorvete sorvete : carrinho) {
        Sorvete sorveteExistente = sorveteService.findById(sorvete.getId()).orElse(null);
        if (sorveteExistente != null && sorveteExistente.getQuantidadeEstoque() > 0) {
            sorveteExistente.setQuantidadeEstoque(sorveteExistente.getQuantidadeEstoque() - 1);
            sorveteService.update(sorveteExistente);
        }
    }

    
    session.setAttribute("carrinho", new ArrayList<>());

   
    redirectAttributes.addFlashAttribute("message", "Compra realizada com sucesso!");
    return "redirect:/verCarrinho";
}

}
