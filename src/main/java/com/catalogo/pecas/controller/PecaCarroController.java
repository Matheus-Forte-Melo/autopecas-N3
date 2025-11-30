package com.catalogo.pecas.controller;

import com.catalogo.pecas.model.Compatibilidade;
import com.catalogo.pecas.model.Peca;
import com.catalogo.pecas.repository.CompatibilidadeRepository;
import com.catalogo.pecas.repository.PecaRepository;
import com.catalogo.pecas.repository.CarroRepository;
import com.catalogo.pecas.model.Carro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PecaCarroController {
    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private CompatibilidadeRepository compatibilidadeRepository;

    @GetMapping("/")
    public String listarIndex(Model model) {
        model.addAttribute("listaPecas", pecaRepository.findAll());
        model.addAttribute("listaCarros", carroRepository.findAll());
        return "index";
    };

    @GetMapping("/add/peca")
    public String mostrarFormularioPeca(Model model) {
        model.addAttribute("peca", new Peca());
        model.addAttribute("listaCarros", carroRepository.findAll());
        model.addAttribute("idsCarrosCompativeis", List.of());
        return "formPeca";
    }

    @GetMapping("/add/carro")
    public String mostrarFormularioCarro(Model model) {
        model.addAttribute("carro", new Carro());
        return "formCarro";
    }

    @PostMapping("/peca/salvar")
    public String salvarPeca(@ModelAttribute Peca peca, @RequestParam(required = false) List<Long> carrosIds) {
        pecaRepository.save(peca);
        compatibilidadeRepository.deleteByPeca_Id(peca.getId());

        if (carrosIds != null && !carrosIds.isEmpty()) {
            for (Long carroId : carrosIds) {
                Carro carro = carroRepository.findById(carroId).orElse(null);

                if (carro != null) {
                    Compatibilidade novaCompatibilidade = new Compatibilidade();
                    novaCompatibilidade.setCarro(carro);
                    novaCompatibilidade.setPeca(peca);
                    compatibilidadeRepository.save(novaCompatibilidade);
                }
            }
        }

        return "redirect:/";
    }

    @PostMapping("carro/salvar")
    public String salvarCarro(@ModelAttribute Carro carro) {
        carroRepository.save(carro);
        return "redirect:/";
    }

    @GetMapping("/editar/peca/{id}")
    public String editarPeca(@PathVariable Long id, Model model) {
        Peca peca = pecaRepository.findById(id).orElse(null);
        model.addAttribute("peca", peca);
        model.addAttribute("listaCarros", carroRepository.findAll());
        assert peca != null;
        model.addAttribute("idsCarrosCompativeis", peca.getListIdsCarrosCompativeis());
        return "formPeca";
    }

    @GetMapping("/editar/carro/{id}")
    public String editarCarro(@PathVariable Long id, Model model) {
        Carro carro = carroRepository.findById(id).orElse(null);
        model.addAttribute("carro", carro);
        return "formCarro";
    }

    @GetMapping("deletar/peca/{id}")
    public String deletarPeca(@PathVariable Long id) {
        compatibilidadeRepository.deleteByPeca_Id(id);
        pecaRepository.deleteById(id);
        return  "redirect:/";
    }

    @GetMapping("deletar/carro/{id}")
    public String deletarCarro(@PathVariable Long id) {
        compatibilidadeRepository.deleteByCarro_Id(id);
        carroRepository.deleteById(id);
        return  "redirect:/";
    }



}
